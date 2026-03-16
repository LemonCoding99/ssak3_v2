package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderRedissonFacade {

    private final OrderService orderService;
    private final RedissonClient redissonClient;
    private final CartProductRepository cartProductRepository;

    // 단일 상품 주문 Facade
    public OrderCreateResponse createOrderFromProduct(Long userId, OrderCreateFromProductRequest request) {
        RLock lock = redissonClient.getLock("lock:product:" + request.getProductId());

        try {
            // 락 획득 시도 (대기시간 10초, 점유시간 1초)
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            // 여기서 호출하는 서비스는 별도의 @Transactional이 걸려있어야 함
            return orderService.createOrderFromProduct(userId, request);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 장바구니 여러 상품 주문 Facade
    public OrderCreateResponse createOrderFromCart(Long userId, OrderCreateFromCartRequest request) {

        // 1. CartProduct id 들을 통해 실제 Product id 들을 가져옴
        List<CartProduct> cartProducts = cartProductRepository.findAllById(request.getCartProductIdList());

        // 2. 데드락 방지를 위해 리스트를 .stream().sorted().toList() 등을 이용해 정렬된 상태의 락 키 리스트 생성
        List<String> lockKeys = cartProducts.stream()
                .map(cp -> cp.getProduct().getId())
                .distinct()
                .sorted()
                .map(id -> "lock:product:" + id)
                .toList();

        // 3. Redisson의 RLock 객체들을 생성
        RLock[] locks = lockKeys.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        // 4. 멀티 락 획득
        RLock multiLock = redissonClient.getMultiLock(locks);

        try {
            // 단일 락과 동일하게 tryLock 수행 (대기 시간 10초, 점유 시간 -1 WatchDog)
            boolean available = multiLock.tryLock(10, -1, TimeUnit.SECONDS);

            if (!available) {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            return orderService.createOrderFromCart(userId, request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 멀티 락도 unlock 처리
            if (multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }
    }
}
