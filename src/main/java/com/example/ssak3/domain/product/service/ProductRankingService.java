package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.s3.service.S3Uploader;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private static final String PRODUCT_DAILY_RANKING_PREFIX = "product:ranking:";
    private static final String PRODUCT_WEEKLY_RANKING_KEY = "product:ranking:weekly";
    private static final String PRODUCT_VIEW_CHECK_PREFIX = "product:view:check:ip:";
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;
    private final S3Uploader s3Uploader;

    /**
     * 조회수 증가 메소드
     */
    public void increaseViewCount(Long productId, String ip) {

        LocalDateTime now = LocalDateTime.now();

        String key = PRODUCT_VIEW_CHECK_PREFIX + ip + ":productId:" + productId;
        LocalDateTime midnight = LocalDate.now().plusDays(1).atStartOfDay();
        Boolean isFirstView = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.between(now, midnight).getSeconds(), TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(isFirstView)) {
            return;
        }

        LocalDate nowDay = LocalDate.now();

        Double score = redisTemplate.opsForZSet().incrementScore(PRODUCT_DAILY_RANKING_PREFIX + nowDay, productId.toString(), 1);

        if (score != null && score == 1.0) {
            // 해당 랭킹 키에 대한 Redisson 객체 가져오기
            RScoredSortedSet<String> dailyRanking = redissonClient.getScoredSortedSet(PRODUCT_DAILY_RANKING_PREFIX + nowDay, StringCodec.INSTANCE);

            // 10일 뒤 자정까지 남은 시간 계산
            LocalDateTime midnightTenDaysLater = nowDay.plusDays(10).atStartOfDay();
            Duration durationUntilMidnight = Duration.between(LocalDateTime.now(), midnightTenDaysLater);

            // Redisson의 expire 메서드 호출 (expiredAt은 stack over flow 발생)
            dailyRanking.expire(durationUntilMidnight);
        }

    }

    /**
     * 조회수 TOP 10 상품 조회
     */
    public List<ProductGetPopularResponse> getPopularProductTop10() {

        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores(PRODUCT_WEEKLY_RANKING_KEY, 0, 9);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIdList = result.stream().map(id -> Long.parseLong(id.getValue())).toList();

        List<Product> productList = productRepository.findAllByIdInAndIsDeletedFalse(productIdList);

        List<TimeDeal> timeDealList = timeDealRepository.findAllByProductIdInAndStatusAndIsDeletedFalse(productIdList, TimeDealStatus.OPEN);

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Map<Long, TimeDeal> timeDealMap = timeDealList.stream()
                .collect(Collectors.toMap(timeDeal -> timeDeal.getProduct().getId(), timeDeal -> timeDeal));

        return productIdList.stream()
                .map(id -> {
                    TimeDeal timeDeal = timeDealMap.get(id);

                    if (timeDeal != null) {
                        Product product = timeDeal.getProduct();
                        String timeDealImageUrl = s3Uploader.createPresignedGetUrl(timeDeal.getImage(), 5);
                        return ProductGetPopularResponse.from(product, timeDeal, null, timeDealImageUrl);
                    }

                    Product product = productMap.get(id);

                    if (product == null || (product.getStatus() != ProductStatus.FOR_SALE && product.getStatus() != ProductStatus.SOLD_OUT)) {
                        return null;
                    }

                    String productImageUrl = s3Uploader.createPresignedGetUrl(product.getImage(), 5);
                    return ProductGetPopularResponse.from(product, null, productImageUrl, null);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 주간 인기 집계
     */
    public void updateWeeklyRanking() {
        LocalDate now = LocalDate.now();

        List<String> otherKeys = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            otherKeys.add(PRODUCT_DAILY_RANKING_PREFIX + now.minusDays(i + 1));
        }

        redisTemplate.opsForZSet().unionAndStore(
                PRODUCT_DAILY_RANKING_PREFIX + now,
                otherKeys,
                PRODUCT_WEEKLY_RANKING_KEY);
    }
}
