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
    public Object orderDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
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
            } else if (map.get("productId") != null) { // 케이스 B: 단일 상품 주문
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

    @Around("@annotation(com.example.ssak3.common.aop.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock distributedLock = signature.getMethod().getAnnotation(DistributedLock.class);

        // Spring EL을 사용하여 메서드 파라미터에서 키 값을 추출 (#couponId)
        String key = "LOCK:" + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            // waitTime 동안 기다리고, 획득 시 leaseTime 동안 점유
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            // 정해진 시간 내에 락을 얻지 못하면 비즈니스 로직을 수행하지 않고 예외 발생
            if (!available) {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }
            return joinPoint.proceed(); // 락 획득 성공하여 실제 비즈니스 로직(issueCoupon) 실행

        } finally {
            // 어떤 상황에서도(성공/실패/에러) 락은 반드시 풀어주기
            try {
                // 내가 잡은 락이 맞는지 확인 후 안전하게 해제
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            } catch (IllegalMonitorStateException e) {
                // 이미 만료된 락을 해제하려 할 때 예외 처리
            }
        }
    }
}
