package com.example.ssak3.domain.cartproduct.repository;

import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCartIdOrderByUpdatedAtDesc(Long cartId);

    Optional<CartProduct> findByCartIdAndProductIdAndTimeDealId(Long cartId, Long productId, Long timeDealId);

    Optional<CartProduct> findByCartIdAndProductIdAndTimeDealIsNull(Long cartId, Long productId);

    Optional<CartProduct> findByIdAndCartId(Long cartProductId, Long cartId);

    List<CartProduct> findByCartIdAndIdIn(Long cartId, List<Long> cartProductIdList);

    long countByCartId(Long cartId);

    @Modifying
    @Query("""
                delete from CartProduct cp
                where cp.cart.user.id = :userId
                  and cp.id in :cartProductIds
            """)
    void deletePaidProductsFromCart(Long userId, List<Long> cartProductIds);

    List<CartProduct> findByCartId(Long id);
}
