package com.example.ssak3.domain.order;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import com.example.ssak3.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderRedissonFacade {

    private final OrderService orderService;
    private final RedissonClient redissonClient;

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
}
