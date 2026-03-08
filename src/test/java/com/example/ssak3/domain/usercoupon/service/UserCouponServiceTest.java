package com.example.ssak3.domain.usercoupon.service;

import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserCouponServiceTest {
    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private List<Long> testUserIds = new ArrayList<>();
    private Long testCouponId;

    @BeforeEach
    void setUp() {
        // 1. 기존 데이터 초기화 (Unique 제약 조건 충돌 방지)
        userCouponRepository.deleteAll();
        userRepository.deleteAll();
        couponRepository.deleteAll();
        testUserIds.clear();

        // 2. 100명의 테스트 유저 생성 (Entity 구조 반영)
        for (int i = 1; i <= 1000; i++) {
            User user = new User(
                    "테스터" + i,                         // name
                    "nick" + i,                          // nickname (Unique)
                    "test" + i + "@ssak3.com",           // email (Unique)
                    "password123!",                      // password
                    LocalDate.of(1995, 1, 1),            // birth
                    "010-0000-" + String.format("%04d", i), // phone (Unique)
                    "서울시 강남구"                        // address
            );
            User savedUser = userRepository.save(user);
            testUserIds.add(savedUser.getId());
        }

        // 3. 테스트용 쿠폰 생성 (Entity 구조 반영)
        Coupon coupon = new Coupon(
                "선착순 100명 할인 쿠폰",                 // name
                1000,                                   // discountValue
                100,                                    // totalQuantity
                LocalDateTime.now().minusDays(1),       // issueStartDate
                LocalDateTime.now().plusDays(7),        // issueEndDate
                10000,                                  // minOrderPrice
                30                                      // validDays
        );
        Coupon savedCoupon = couponRepository.save(coupon);
        testCouponId = savedCoupon.getId();
    }

    @Test
    @DisplayName("100명이 동시에 쿠폰 발급을 요청했을 때, 정확히 100개의 수량이 차감되어야 한다")
    void issueCouponConcurrencyTest() throws InterruptedException {
        // Given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            Long userId = testUserIds.get(i);
            executorService.submit(() -> {
                try {
                    // 서비스 로직 호출 (DistributedLock 적용된 메서드)
                    userCouponService.issueCoupon(userId, testCouponId);
                } catch (Exception e) {
                    System.err.println("발급 실패 [User:" + userId + "]: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 작업 완료 대기

        // Then
        Coupon coupon = couponRepository.findById(testCouponId)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));

        System.out.println("최종 발급 수량: " + coupon.getIssuedQuantity());

        // 결과 검증: 분산락이 정상 작동한다면 정확히 100이어야 함
        assertEquals(100, coupon.getIssuedQuantity());
    }
}
