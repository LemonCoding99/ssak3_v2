package com.example.ssak3.domain.coupon.repository;

import com.example.ssak3.domain.coupon.entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c " +
            "FROM Coupon c " +
            "WHERE c.issueEndDate >= :now " +
            "AND c.isDeleted = false")
    Page<Coupon> findAllAvailableCoupons(LocalDateTime now, Pageable pageable);

    Page<Coupon> findAllByIsDeletedFalse(Pageable pageable);

    boolean existsByNameAndIsDeletedFalse(String name);
}
