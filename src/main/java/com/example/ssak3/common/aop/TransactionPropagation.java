package com.example.ssak3.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 락이 걸린 상태에서 새로운 트랜잭션을 시작해주는 배달원 역할
 */
@Component
public class TransactionPropagation {

    // 기존 트랜잭션 유무와 상관없이 락 안에서 새로운 트랜잭션을 보장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
