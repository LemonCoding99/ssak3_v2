package com.example.ssak3.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key(); // 락 이름 (ex. lock:product:1)
    long waitTime() default 5L; // 락 획득 대기시간
    long leaseTime() default 3L; // 락 점유 시간
    TimeUnit timeUnit() default TimeUnit.SECONDS; // 시간 단위
}
