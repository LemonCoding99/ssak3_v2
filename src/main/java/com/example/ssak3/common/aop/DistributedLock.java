package com.example.ssak3.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)  // 메서드 위에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME)  // 프로그램 실행중에도 참조 가능
public @interface DistributedLock {
    String key();
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    long waitTime() default 5L;  // 락 획득 (최대)대기시간
    long leaseTime() default 3L;  // 락 점유 시간
}
