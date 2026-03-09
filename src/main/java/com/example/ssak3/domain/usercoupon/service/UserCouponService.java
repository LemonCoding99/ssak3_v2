package com.example.ssak3.domain.usercoupon.service;

import com.example.ssak3.common.aop.DistributedLock;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.model.response.CouponListForUserGetResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponDeleteResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponIssueResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponListGetResponse;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponCacheService userCouponCacheService;

    /**
     * 쿠폰 목록 조회 로직 (사용자용)
     */
    @Transactional(readOnly = true)
    public PageResponse<CouponListForUserGetResponse> getCouponListForUser(Pageable pageable) {

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        PageResponse<CouponListForUserGetResponse> cachedResponse = userCouponCacheService.getUserCouponListCache(pageNumber, pageSize);

        if (cachedResponse != null) {
            return cachedResponse;
        }

        Page<CouponListForUserGetResponse> couponPage = couponRepository
                .findAllAvailableCoupons(LocalDateTime.now(), pageable)
                .map(CouponListForUserGetResponse::from);

        PageResponse<CouponListForUserGetResponse> response = PageResponse.from(couponPage);

        userCouponCacheService.saveUserCouponListCache(pageNumber, pageSize, response);

        return response;
    }

    /**
     * 쿠폰 발급 로직
     */
    @DistributedLock(key = "#couponId")
    @Transactional
    public UserCouponIssueResponse issueCoupon(Long userId, Long couponId) {
        long start = System.currentTimeMillis();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        List<UserCouponStatus> restrictedStatuses = List.of(
                UserCouponStatus.AVAILABLE,
                UserCouponStatus.USED,
                UserCouponStatus.USED_DELETED
        );

        if (user.getRole() == UserRole.ADMIN) {
            throw new CustomException(ErrorCode.ADMIN_CANNOT_ISSUE_COUPON);
        }

        if (userCouponRepository.existsByUserAndCouponAndStatusIn(user, coupon, restrictedStatuses)) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_EXISTS);
        }

        if (coupon.isDeleted()) {
            throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
        }

        coupon.increaseIssuedQuantity();

        LocalDateTime expiredAt = (coupon.getValidDays() != null) ? LocalDateTime.now().plusDays(coupon.getValidDays()) : coupon.getIssueEndDate();

        UserCoupon userCoupon = new UserCoupon(
                user,
                coupon,
                expiredAt,
                UserCouponStatus.AVAILABLE
        );

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        log.info("issueCoupon 처리시간: {}ms", System.currentTimeMillis() - start);
        return UserCouponIssueResponse.from(savedUserCoupon);

    }

    /**
     * 내 쿠폰 목록 조회 로직
     */
    @Transactional
    public PageResponse<UserCouponListGetResponse> getMyCouponList(Long userId, Pageable pageable, UserCouponStatus status) {

        Page<UserCoupon> userCouponPage = userCouponRepository.findAllActiveCouponsByUserId(userId, pageable, status);

        if (status == UserCouponStatus.AVAILABLE) {

            LocalDateTime now = LocalDateTime.now();

            userCouponPage.getContent().forEach(uc -> {
                if (uc.getExpiredAt() != null && uc.getExpiredAt().isBefore(now)) {
                    uc.changeStatus(UserCouponStatus.EXPIRED);
                    userCouponRepository.save(uc);
                }
            });
        }

        return PageResponse.from(userCouponPage.map(UserCouponListGetResponse::from));
    }

    /**
     * 내 쿠폰 삭제
     */
    @Transactional
    public UserCouponDeleteResponse deleteUserCoupon(Long userId, Long userCouponId) {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (!userCoupon.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_COUPON_ACCESS);
        }

        if (userCoupon.getStatus() == UserCouponStatus.UNUSED_DELETED || userCoupon.getStatus() == UserCouponStatus.USED_DELETED) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_DELETED);
        }

        if (userCoupon.getStatus() == UserCouponStatus.AVAILABLE) {
            userCoupon.getCoupon().decreaseIssuedQuantity();
            userCoupon.changeStatus(UserCouponStatus.UNUSED_DELETED);
        } else if (userCoupon.getStatus() == UserCouponStatus.USED) {
            userCoupon.changeStatus(UserCouponStatus.USED_DELETED);
        }

        return UserCouponDeleteResponse.from(userCoupon);
    }
}