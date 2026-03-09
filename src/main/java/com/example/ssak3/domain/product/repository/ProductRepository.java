package com.example.ssak3.domain.product.repository;

import com.example.ssak3.domain.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    @Query("""
                SELECT p
                FROM Product p
                WHERE p.isDeleted = false
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                  AND p.status NOT IN (
                  com.example.ssak3.common.enums.ProductStatus.BEFORE_SALE)
            """)
    Page<Product> findProductListByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select p from Product p where p.id = :productId and p.isDeleted = false")
//    Optional<Product> findByIdForLock(@Param("productId") Long productId);

    List<Product> findAllByIdInAndIsDeletedFalse(List<Long> productIdList);
}
