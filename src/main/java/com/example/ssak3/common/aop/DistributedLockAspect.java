package com.example.ssak3.common.aop;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.utils.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Order(1) // 트랜잭션보다 먼저 실행되도록 우선순위 설정
public class DistributedLockAspect {
    private final RedissonClient redissonClient;

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
