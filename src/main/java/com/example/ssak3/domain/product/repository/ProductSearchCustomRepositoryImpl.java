package com.example.ssak3.domain.product.repository;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.model.response.ProductGetSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.product.entity.QProduct.product;
import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@Repository
@RequiredArgsConstructor
public class ProductSearchCustomRepositoryImpl implements ProductSearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductGetSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {

        Long total = queryFactory
                .select(product.countDistinct())
                .from(product)
                .leftJoin(timeDeal)
                .on(
                        product.id.eq(timeDeal.product.id),
                        timeDeal.isDeleted.eq(false),
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.startAt.before(LocalDateTime.now()),
                        timeDeal.endAt.after(LocalDateTime.now())
                )
                .where(
                        product.isDeleted.eq(false),
                        product.status.ne(ProductStatus.BEFORE_SALE),
                        nameContains(keyword),
                        priceGoe(minPrice),
                        priceLoe(maxPrice)
                )
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(queryFactory
                .select(Projections.constructor(ProductGetSearchResponse.class,
                        product.id,
                        timeDeal.id,
                        product.category.id,
                        product.name,
                        product.information,
                        product.price,
                        timeDeal.dealPrice,
                        product.status,
                        product.image,
                        timeDeal.image,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .leftJoin(timeDeal)
                .on(
                        product.id.eq(timeDeal.product.id),
                        timeDeal.isDeleted.eq(false),
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.startAt.before(LocalDateTime.now()),
                        timeDeal.endAt.after(LocalDateTime.now())
                )
                .where(
                        product.isDeleted.eq(false),
                        product.status.ne(ProductStatus.BEFORE_SALE),
                        nameContains(keyword),
                        priceGoe(minPrice),
                        priceLoe(maxPrice)
                )
                .orderBy(product.price.asc(), timeDeal.dealPrice.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(),
                pageable,
                totalCount);

    }

//    private BooleanExpression nameContains(String keyword) {
//
//        if (keyword == null || keyword.isBlank()) {
//            return null;
//        }
//
//        // Expression.numberTemplate을 사용하여 DB 전용 함수를 호출
//        return Expressions.numberTemplate(
//                Double.class,
//                "function('match_against', {0}, {1})",
//                product.name,
//                keyword + "*"
//        ).gt(0); // Boolean Mode에서는 와일드카드(*)를 자주 씀
//    }

    private BooleanExpression nameContains(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1})",
                product.name,
                keyword
        ).gt(0);
    }

    private BooleanExpression priceGoe(Integer minPrice) {

        if (minPrice == null) return null;

        return timeDeal.dealPrice.coalesce(product.price).goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) {

        if (maxPrice == null) return null;

        return timeDeal.dealPrice.coalesce(product.price).loe(maxPrice);
    }
}
