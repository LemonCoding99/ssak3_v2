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
import java.util.concurrent.CyclicBarrier;
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
        // 기존 데이터 초기화 (Unique 제약 조건 충돌 방지)
        userCouponRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        testUserIds.clear();

        // 100명의 테스트 유저 생성
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            users.add(new User(
                    "테스터" + i,                         // name
                    "nick" + i,                          // nickname (Unique)
                    "test" + i + "@ssak3.com",           // email (Unique)
                    "password123!",                      // password
                    LocalDate.of(1995, 1, 1),            // birth
                    "010-0000-" + String.format("%04d", i), // phone (Unique)
                    "서울시 강남구"                        // address
            ));
        }
        userRepository.saveAll(users);
        users.forEach(u -> testUserIds.add(u.getId()));

        // 테스트용 쿠폰 생성
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
    @DisplayName("150명이 동시에 100개의 쿠폰에 달려들어도 정확히 100개만 발급되어야 한다")
    void issueCouponConcurrencyTest() throws InterruptedException {
        // Given
        int threadCount = 150; // 쿠폰 수량(100)보다 많은 요청을 보내서 경쟁 유도
        ExecutorService executorService = Executors.newFixedThreadPool(150); // 동시 처리 스레드 수
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount); // 150명이 동시에 '땅!' 하고 출발하게 함

        // When
        for (int i = 0; i < threadCount; i++) {
            Long userId = testUserIds.get(i);
            executorService.submit(() -> {
                try {
                    barrier.await(); // 모든 스레드가 준비될 때까지 대기
                    userCouponService.issueCoupon(userId, testCouponId);
                } catch (Exception e) {
                    // 100개가 넘어가면 예외가 발생하는 것이 정상 (로그 확인용)
                    // System.err.println("발급 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업 완료 대기
        executorService.shutdown();

        // Then
        Coupon coupon = couponRepository.findById(testCouponId).orElseThrow();

        System.out.println("결과 - 총 요청: " + threadCount + "건");
        System.out.println("결과 - 최종 발급 수량: " + coupon.getIssuedQuantity());

        // 분산 락이 정상이라면 150명이 요청해도 결과는 반드시 100이어야 함
        assertEquals(100, coupon.getIssuedQuantity());
    }
}
