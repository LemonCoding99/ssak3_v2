package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.order.OrderTestDataFixture;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired private OrderTestDataFixture fixture;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private OrderRedissonFacade orderRedissonFacade;

    @BeforeEach
    void clean() {
        orderProductRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        cartProductRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();

        // 외래키 제약 조건 때문에 최상위 부모 테이블을 마지막에 삭제
        userRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("동시에 100명이 주문할 때 재고가 정확히 차감되어야 한다.")
    void stock_concurrency_test() throws InterruptedException {
        // Given: 재고가 100개인 상품 준비
        int threadCount = 100;
        Product product = fixture.createTestProduct(100); // 초기 재고 100개

        OrderCreateFromProductRequest request = new OrderCreateFromProductRequest(
                product.getId(),
                1, // 한 번에 1개씩 주문
                "서울시",
                null
        );

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long startTime = System.nanoTime();

        // When: 100개의 스레드가 동시에 주문 호출
        for (int i = 0; i < threadCount; i++) {
            User user = fixture.createTestUser();

            executorService.submit(() -> {
                try {
                    orderRedissonFacade.createOrderFromProduct(user.getId(), request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기

        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        double durationMs = (double) executionTime / 1_000_000; // ms 단위
        System.out.println("총 실행 시간:" + durationMs);

        // Then: 최종 재고가 0인지 확인
        Product reloaded = productRepository.findById(product.getId()).orElseThrow();
        assertThat(reloaded.getQuantity()).isEqualTo(0);
        assertThat(orderRepository.count()).isEqualTo(100);
    }

    @Test
    @DisplayName("단일 상품 주문 통합 테스트 - 주문/주문상품 저장 + 재고 차감 + 결제대기 상태")
    void createOrderFromProduct_통합테스트_paymentPending() {
        // Given
        User user = fixture.createTestUser();
        Product product = fixture.createTestProduct(10);

        OrderCreateFromProductRequest request = new OrderCreateFromProductRequest(
                product.getId(),
                2,
                "서울시",
                null
        );

        // When
        OrderCreateResponse response = orderService.createOrderFromProduct(user.getId(), request);

        // Then
        assertThat(response.getSubtotal()).isEqualTo(20000);
        assertThat(response.getDeliveryFee()).isEqualTo(3000);
        assertThat(response.getDiscount()).isEqualTo(0);
        assertThat(response.getPaymentUrl()).isNotBlank();

        // Then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        Order savedOrder = orders.get(0);
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(23000);

        List<OrderProduct> ops = orderProductRepository.findByOrderId(savedOrder.getId());
        assertThat(ops).hasSize(1);
        assertThat(ops.get(0).getProduct().getId()).isEqualTo(product.getId());
        assertThat(ops.get(0).getUnitPrice()).isEqualTo(10000);
        assertThat(ops.get(0).getQuantity()).isEqualTo(2);

        Product reloaded = productRepository.findById(product.getId()).orElseThrow();
        assertThat(reloaded.getQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("장바구니 상품 주문 - 상품 재고 부족 예외")
    void createOrderFromCart_통합테스트_상품재고부족() {
        // Given
        User user = new User("test4", "user4", "test4@test.com", "Aa12345678!!", LocalDate.of(2026, 2, 2), "010-0000-0001", "서울");
        userRepository.save(user);

        Category category = new Category("test");
        categoryRepository.save(category);

        Product product = new Product(category, "테스트", 10000, ProductStatus.FOR_SALE, "설명", 1, null, null);
        productRepository.save(product);

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        CartProduct cartProduct = new CartProduct(cart, product, null, 2);
        cartProductRepository.save(cartProduct);

        OrderCreateFromCartRequest request = new OrderCreateFromCartRequest(cart.getId(), List.of(cartProduct.getId()), "서울시", null);

        // When / Then
        assertThatThrownBy(() -> orderService.createOrderFromCart(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_INSUFFICIENT);
    }

    @Test
    @DisplayName("단일 상품 주문 - 쿠폰 최소 금액 미달 예외")
    void createOrderFromProduct_통합테스트_쿠폰최소금액미달() {
        // Given
        User user = new User("test4", "user4", "test4@test.com", "Aa12345678!!", LocalDate.of(2026, 2, 2), "010-0000-0001", "서울");
        userRepository.save(user);

        Category category = new Category("test");
        categoryRepository.save(category);

        Product product = new Product(category, "테스트", 10000, ProductStatus.FOR_SALE, "설명", 1, null, null);
        productRepository.save(product);

        Coupon coupon = new Coupon("테스트 쿠폰", 5000, 100, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 30000, 7);
        couponRepository.save(coupon);

        UserCoupon userCoupon = new UserCoupon(user, coupon, LocalDateTime.now().plusDays(coupon.getValidDays()), UserCouponStatus.AVAILABLE);
        userCouponRepository.save(userCoupon);

        OrderCreateFromProductRequest request = new OrderCreateFromProductRequest(product.getId(), 1, "배송지", userCoupon.getId());

        // When / Then
        assertThatThrownBy(() -> orderService.createOrderFromProduct(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);

        assertThat(orderRepository.findAll()).isEmpty();
        assertThat(userCoupon.getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
    }

    @Test
    @DisplayName("장바구니 상품 주문 통합 테스트 - 주문/주문상품 저장 + 재고 차감 + 결제대기 상태")
    void createOrderFromCart_통합테스트_paymentPending() {
        User user = fixture.createTestUser();
        Cart cart = fixture.createCartWithProducts(user);

        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cart.getId());

        Product p1 = cartProducts.get(0).getProduct();
        Product p2 = cartProducts.get(1).getProduct();
        CartProduct cp1 = cartProducts.get(0);
        CartProduct cp2 = cartProducts.get(1);

        OrderCreateFromCartRequest request = new OrderCreateFromCartRequest(
                cart.getId(),
                List.of(cp1.getId(), cp2.getId()),
                "서울시",
                null
        );

        // When
        OrderCreateResponse response = orderService.createOrderFromCart(user.getId(), request);

        // Then
        assertThat(response.getSubtotal()).isEqualTo(35000);
        assertThat(response.getDeliveryFee()).isEqualTo(0);
        assertThat(response.getDiscount()).isEqualTo(0);
        assertThat(response.getPaymentUrl()).isNotBlank();

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        Order savedOrder = orders.get(0);
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(35000);

        List<OrderProduct> ops = orderProductRepository.findByOrderId(savedOrder.getId());
        assertThat(ops).hasSize(2);

        OrderProduct op1 = ops.stream()
                .filter(op -> op.getProduct().getId().equals(p1.getId()))
                .findFirst().orElseThrow();
        OrderProduct op2 = ops.stream()
                .filter(op -> op.getProduct().getId().equals(p2.getId()))
                .findFirst().orElseThrow();

        assertThat(op1.getUnitPrice()).isEqualTo(10000);
        assertThat(op1.getQuantity()).isEqualTo(2);
        assertThat(op2.getUnitPrice()).isEqualTo(15000);
        assertThat(op2.getQuantity()).isEqualTo(1);
        Product reloaded1 = productRepository.findById(p1.getId()).orElseThrow();
        Product reloaded2 = productRepository.findById(p2.getId()).orElseThrow();
        assertThat(reloaded1.getQuantity()).isEqualTo(8); // 10 - 2
        assertThat(reloaded2.getQuantity()).isEqualTo(4); // 5 - 1
        assertThat(op1.getCartProductId()).isEqualTo(cp1.getId());
        assertThat(op2.getCartProductId()).isEqualTo(cp2.getId());
    }

}
