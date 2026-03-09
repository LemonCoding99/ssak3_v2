package com.example.ssak3.domain.order;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderTestDataFixture {

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

    private final AtomicInteger counter = new AtomicInteger(1);

    @Transactional
    public User createTestUser() {
        int id = counter.getAndIncrement(); // 호출될 때마다 1, 2, 3... 증가

        return userRepository.save(new User(
                "testUser" + id,
                "userName" + id,
                "test" + id + "@test.com", // 이메일 중복 완벽 방지
                "Aa12345678!!",
                LocalDate.of(2026, 2, 2),
                "010-0000-" + String.format("%04d", id), // 전화번호도 유니크하게
                "서울"
        ));
    }

    @Transactional
    public Product createTestProduct(int quantity) {
        Category category = categoryRepository.save(new Category("카테고리_"));

        return productRepository.save(new Product(category, "상품_", 10000, ProductStatus.FOR_SALE, "설명_", quantity, null, null));
    }

    @Transactional
    public Cart createCartWithProducts(User user) {
        Category category = categoryRepository.save(new Category("카테고리1"));

        Product p1 = new Product(category, "상품1", 10000, ProductStatus.FOR_SALE, "설명", 10, null, null);
        Product p2 = new Product(category, "상품2", 15000, ProductStatus.FOR_SALE, "설명", 5, null, null);

        productRepository.save(p1);
        productRepository.save(p2);

        Cart cart = cartRepository.save(new Cart(user));

        cartProductRepository.save(new CartProduct(cart, p1, null, 2));
        cartProductRepository.save(new CartProduct(cart, p2, null, 1));

        return cart;
    }
}
