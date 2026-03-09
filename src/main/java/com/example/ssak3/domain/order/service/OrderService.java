package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.*;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderCancelRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import com.example.ssak3.domain.order.model.response.OrderGetResponse;
import com.example.ssak3.domain.order.model.response.OrderListGetResponse;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.client.TossPaymentClient;
import com.example.ssak3.domain.payment.entity.Payment;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.product.service.InventoryService;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;
    private final InventoryService inventoryService;
    private final OrderCancelService orderCancelService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    /**
     * 상품 페이지에서 단일 상품 구매
     */
    @Transactional
    public OrderCreateResponse createOrderFromProduct(Long userId, OrderCreateFromProductRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.PAYMENT_PENDING)) {
            throw new CustomException(ErrorCode.ORDER_PAYMENT_PENDING_EXISTS);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        int quantity = request.getQuantity();
        int unitPrice = validatePurchasableReturnUnitPrice(product, quantity);
        long subtotal = unitPrice * quantity;
        long deliveryFee = 0;

        if (subtotal < 30000) {
            deliveryFee = 3000;
        }

        Order order = new Order(user, request.getAddress(), null, subtotal + deliveryFee);

        OrderProduct orderProduct = new OrderProduct(order, product, unitPrice, quantity, null);

        inventoryService.decreaseProductStock(product, quantity);

        long discount = 0L;
        UserCoupon userCoupon = null;

        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findByIdAndUserId(request.getUserCouponId(), userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

            if (!userCoupon.getStatus().equals(UserCouponStatus.AVAILABLE)) {
                throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
            }

            if (userCoupon.getCoupon().getMinOrderPrice() <= subtotal) {
                discount = userCoupon.getCoupon().getDiscountValue();
                userCoupon.use();

                order.applyCoupon(userCoupon, discount);
            } else {
                throw new CustomException(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);
            }
        }

        Order savedOrder = orderRepository.save(order);
        orderProductRepository.save(orderProduct);

        String paymentUrl = null;

        if (savedOrder.getTotalPrice() == 0) {
            savedOrder.updateStatus(OrderStatus.DONE);
        } else {
            savedOrder.updateStatus(OrderStatus.PAYMENT_PENDING);

            String orderName = URLEncoder.encode(product.getName(), StandardCharsets.UTF_8);
            paymentUrl = frontendBaseUrl + "/checkout.html?orderId=" + savedOrder.getId() + "&orderName=" + orderName;
        }

        return OrderCreateResponse.from(savedOrder, subtotal, discount, paymentUrl, deliveryFee);
    }

    /**
     * 장바구니에서 여러 상품 주문
     * 키를 리스트로 받아서 멀티 락 처리
     */
    @Transactional
    public OrderCreateResponse createOrderFromCart(Long userId, OrderCreateFromCartRequest request) {

        // 락이 이미 유저와 상품들을 모두 잡고 있으므로 여기서는 순수 비즈니스 로직과 DB 상태 체크만 수행하면 됨
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.PAYMENT_PENDING)) {
            throw new CustomException(ErrorCode.ORDER_PAYMENT_PENDING_EXISTS);
        }

        Long cartId = request.getCartId();

        List<Long> cartProductIdList = request.getCartProductIdList();
        if (cartProductIdList.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_IS_NULL);
        }

        Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ACCESS_DENIED));

        List<CartProduct> cartProductList = cartProductRepository.findByCartIdAndIdIn(cart.getId(), cartProductIdList);

        if (cartProductList.size() != cartProductIdList.size()) {
            throw new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND);
        }

        long subtotal = 0L;

        Map<Long, Product> lockedProductMap = new HashMap<>();
        Map<Long, Integer> unitPriceMap = new HashMap<>();

        for (CartProduct cartProduct : cartProductList) {
            Product lockedProduct = productRepository.findById(cartProduct.getProduct().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            int quantity = cartProduct.getQuantity();

            int unitPrice = validatePurchasableReturnUnitPrice(lockedProduct, quantity);

            lockedProductMap.put(cartProduct.getId(), lockedProduct);
            unitPriceMap.put(cartProduct.getId(), unitPrice);

            subtotal += (long) unitPrice * quantity;
        }

        long deliveryFee = 0;
        if (subtotal < 30000) {
            deliveryFee = 3000;
        }

        Order order = new Order(user, request.getAddress(), null, subtotal + deliveryFee);
        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProductList = new ArrayList<>();

        for (CartProduct cartProduct : cartProductList) {
            Product lockedProduct = lockedProductMap.get(cartProduct.getId());

            int quantity = cartProduct.getQuantity();
            Integer unitPrice = unitPriceMap.get(cartProduct.getId());

            OrderProduct orderProduct = new OrderProduct(savedOrder, lockedProduct, unitPrice, quantity, cartProduct.getId());
            orderProductList.add(orderProduct);

            inventoryService.decreaseProductStock(lockedProduct, quantity);
        }

        orderProductRepository.saveAll(orderProductList);

        long discount = 0;
        UserCoupon userCoupon = null;

        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findByIdAndUserId(request.getUserCouponId(), userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

            if (!userCoupon.getStatus().equals(UserCouponStatus.AVAILABLE)) {
                throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
            }

            if (userCoupon.getCoupon().getMinOrderPrice() <= subtotal) {
                discount = userCoupon.getCoupon().getDiscountValue();
                userCoupon.use();
                savedOrder.applyCoupon(userCoupon, discount);
            } else {
                throw new CustomException(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);
            }
        }

        String orderName;

        if (cartProductList.size() == 1) {
            orderName = cartProductList.get(0).getProduct().getName();
        } else {
            orderName = cartProductList.get(0).getProduct().getName() + " 외" + (cartProductList.size() - 1) + " 건";
        }

        String paymentUrl = null;

        if (savedOrder.getTotalPrice() == 0) {
            savedOrder.updateStatus(OrderStatus.DONE);
        } else {
            savedOrder.updateStatus(OrderStatus.PAYMENT_PENDING);
            paymentUrl = frontendBaseUrl + "/checkout.html?orderId=" + savedOrder.getId() + "&orderName=" + orderName;
        }

        return OrderCreateResponse.from(savedOrder, subtotal, discount, paymentUrl, deliveryFee);

    }

    /**
     * 판매중/재고 체크(주문 가능 여부 체크) 후 단위 가격(주문 시의 1개당 가격) 반환
     */
    private int validatePurchasableReturnUnitPrice(Product product, int quantity) {

        if (product.isDeleted()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        if (product.getQuantity() < quantity) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }

        if (product.getStatus().equals(ProductStatus.FOR_SALE)) {
            return product.getPrice();
        }

        TimeDeal timeDeal = timeDealRepository.findByProductIdAndStatusAndIsDeletedFalse(product.getId(), TimeDealStatus.OPEN)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE));

        return timeDeal.getDealPrice();
    }

    /**
     * 주문 상세 조회
     */
    @Transactional(readOnly = true)
    public OrderGetResponse getOrder(Long userId, Long orderId) {

        Order order = getUserOrder(userId, orderId);

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);
    }

    /**
     * 내 주문 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<OrderListGetResponse> getOrderList(Long userId, Pageable pageable) {

        Page<OrderListGetResponse> orderListPage = orderRepository.findAllByUserIdAndStatusNotOrderByCreatedAtDesc(userId, pageable, OrderStatus.CREATED)
                .map(OrderListGetResponse::from);

        return PageResponse.from(orderListPage);

    }

    /**
     * 주문 취소
     */
    public OrderGetResponse updateOrderCanceled(Long userId, OrderCancelRequest request) {

        Order order = getUserOrder(userId, request.getOrderId());

        if (!order.getStatus().equals(OrderStatus.DONE)) {
            throw new CustomException(ErrorCode.ORDER_CAN_NOT_BE_CANCELED);
        }

        Payment payment = null;

        if (order.getTotalPrice() != 0) {
            payment = paymentRepository.findByOrder(order)
                    .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

            if (payment.getStatus() == PaymentStatus.CANCELLED) {
                return OrderGetResponse.from(order, order.getOrderProducts());
            }

            tossPaymentClient.cancel(payment.getPaymentKey(), request.getCancelReason());
        }

        orderCancelService.orderCancel(order, payment);

        return OrderGetResponse.from(order, order.getOrderProducts());
    }

    /**
     * 결제 대기 상태(PAYMENT_PENDING)에서 주문 취소(유저)
     */
    @Transactional
    public OrderGetResponse cancelPendingOrder(Long userId, Long orderId) {

        Order order = getUserOrder(userId, orderId);

        if (!order.getStatus().equals(OrderStatus.PAYMENT_PENDING)) {
            throw new CustomException(ErrorCode.ORDER_CAN_NOT_BE_CANCELED);
        }

        for (OrderProduct op : order.getOrderProducts()) {
            op.getProduct().rollbackQuantity(op.getQuantity());
        }

        if (order.getUserCoupon() != null) {
            order.getUserCoupon().rollback();
        }

        order.updateStatus(OrderStatus.CANCELED);

        return OrderGetResponse.from(order, order.getOrderProducts());
    }

    /**
     * 재결제
     */
    @Transactional
    public OrderCreateResponse retryPayment(Long userId, Long orderId) {

        Order order = getUserOrder(userId, orderId);

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new CustomException(ErrorCode.ORDER_NOT_IN_PAYMENT_PENDING);
        }

        List<OrderProduct> ops = orderProductRepository.findByOrderId(order.getId());

        if (ops.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_IS_NULL);
        }

        for (OrderProduct op : ops) {
            Product product = productRepository.findByIdAndIsDeletedFalse(op.getProduct().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            int expectedUnitPrice = 0;

            if (product.getStatus().equals(ProductStatus.STOP_SALE)) {
                TimeDeal timeDeal = timeDealRepository
                        .findByProductIdAndStatusAndIsDeletedFalse(product.getId(), TimeDealStatus.OPEN)
                        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE));

                expectedUnitPrice = timeDeal.getDealPrice();

            } else {
                expectedUnitPrice = product.getPrice();
            }

            if (op.getUnitPrice() != expectedUnitPrice) {
                throw new CustomException(ErrorCode.ORDER_PRICE_CHANGED);
            }
        }

        String orderNameRaw = (ops.size() == 1)
                ? ops.get(0).getProduct().getName()
                : ops.get(0).getProduct().getName() + " 외 " + (ops.size() - 1) + " 건";

        String orderName = URLEncoder.encode(orderNameRaw, StandardCharsets.UTF_8);

        long subtotal = ops.stream()
                .mapToLong(op -> (long) op.getUnitPrice() * op.getQuantity())
                .sum();

        long deliveryFee = (subtotal < 30000) ? 3000 : 0;

        long total = order.getTotalPrice();

        long discount = subtotal - (total - deliveryFee);
        if (discount < 0) discount = 0;

        String paymentUrl = frontendBaseUrl + "/checkout.html?orderId=" + order.getId() + "&orderName=" + orderName;

        return OrderCreateResponse.from(order, subtotal, discount, paymentUrl, deliveryFee);
    }

    private Order getUserOrder(Long userId, Long orderId) {

        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }
}