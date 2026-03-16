package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.product.event.ProductViewEvent;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.response.ProductGetResponse;
import com.example.ssak3.domain.product.model.response.ProductListGetResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.s3.service.S3Uploader;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUserService {

    private final ProductRepository productRepository;
    private final S3Uploader s3Uploader;
    private final TimeDealRepository timeDealRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 상품 상세 조회 (사용자)
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProduct(Long productId, String ip) {

        // 상품 조회
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 이벤트 발행
        eventPublisher.publishEvent(new ProductViewEvent(productId, ip));

        if (foundProduct.getStatus().equals(ProductStatus.STOP_SALE) || foundProduct.getStatus().equals(ProductStatus.BEFORE_SALE)) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_VIEWABLE);
        }

        String viewImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getImage(), 5);
        String viewDetailImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getDetailImage(), 5);

        return ProductGetResponse.from(foundProduct, viewImageUrl, viewDetailImageUrl);
    }

    /**
     * 상품 목록 조회 (사용자)
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductListGetResponse> getProductList(Long categoryId, Pageable pageable) {

        Page<Product> productList = productRepository.findProductListByCategoryId(categoryId, pageable);

        Page<ProductListGetResponse> mapped = productList.map(product -> {

            if (product.getStatus().equals(ProductStatus.STOP_SALE)) {
                TimeDeal timeDeal = timeDealRepository.findByProductId(product.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

                String timeDealImageUrl = s3Uploader.createPresignedGetUrl(timeDeal.getImage(), 5);
                String productImageUrl = s3Uploader.createPresignedGetUrl(product.getImage(), 5);

                return ProductListGetResponse.from(product, timeDeal, productImageUrl, timeDealImageUrl);
            }

            String imageUrl = s3Uploader.createPresignedGetUrl(product.getImage(), 5);
            return ProductListGetResponse.from(product, null, imageUrl, null);
        });

        return PageResponse.from(mapped);
    }
}
