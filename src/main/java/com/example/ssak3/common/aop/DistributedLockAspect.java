package com.example.ssak3.common.aop;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.utils.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Order(1) // Hibernate의 @Transactional 보다 먼저 실행 되도록 설정
@Slf4j
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final TransactionPropagation transactionPropagation; // 트랜잭션 전용 클래스

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 1. 키 이름 파싱 (SpEL 등 이용하여 request.getProductId 가져오기)
        // 유저 ID와 상품 ID 리스트를 모두 포함한 객체를 파싱
        Object lockKey = CustomSpringELParser.getValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.key()
        );

        // 기본적으로 redissonClient.getLock(key)는 문자열 하나를 받음
        // 어노테이션에서 넘겨준 값이 리스트일 경우 MultiLock으로 처리 (ex. cartProductIdList)
        // 여러 개의 RLock 객체를 하나의 리스트로 묶어 관리
        // 모든 락을 성공적으로 획득해야만 다음 로직으로 넘어가며, 하나라도 실패하면 나머지 락들을 모두 해제하여 원자성 보장
        List<RLock> rLocks = new ArrayList<>();

        // 2. 키 타입에 따른 락 생성 로직
        // 예시: key = "{userId: 1, productIds: [101, 102]}" 형태
        if (lockKey instanceof Map<?, ?> map) {

            // 공통: 유저 락 (결제 중복 방지 정책)
            Long userId = (Long) map.get("userId");

            if (userId != null) {
                rLocks.add(redissonClient.getLock("lock:user:" + userId));
            }

            // 케이스 A: 장바구니 주문 (여러 상품 리스트)
            if (map.get("productIds") instanceof List<?> productIds) {
                ((List<?>) productIds).stream()
                        .map(Object::toString)
                        .sorted() // 데드락 방지
                        .forEach(id -> rLocks.add(redissonClient.getLock("lock:product:" + id)));
            }

            // 케이스 B: 단일 상품 주문
            else if (map.get("productId") != null) {
                Long productId = (Long) map.get("productId");
                rLocks.add(redissonClient.getLock("lock:product:" + productId));
            }

            // 3. MultiLock 생성
            RLock multiLock = redissonClient.getMultiLock(rLocks.toArray(new RLock[0]));

            try {
                // 4. 모든 락을 동시에 획득 시도
                boolean isLocked = multiLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

                if (!isLocked) {
                    throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
                }

                return transactionPropagation.proceed(joinPoint);
            } finally {
                // 5. 멀티락 해제 (개별 락이 아닌 multiLock 객체를 해제하면 묶인 락이 모두 풀림
                if (multiLock.isHeldByCurrentThread()) {
                    multiLock.unlock();
                }
            }
        }

        return joinPoint.proceed();
    }
}
