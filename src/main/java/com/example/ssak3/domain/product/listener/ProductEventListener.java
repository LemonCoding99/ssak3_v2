package com.example.ssak3.domain.product.listener;

import com.example.ssak3.domain.product.event.ProductViewEvent;
import com.example.ssak3.domain.product.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final ProductRankingService productRankingService;

    // @Async로 별도의 스레드 스택 사용
    @Async
    @EventListener
    public void handleProductViewEvent(ProductViewEvent event) {
        productRankingService.increaseViewCount(event.getProductId(), event.getIp());
    }
}
