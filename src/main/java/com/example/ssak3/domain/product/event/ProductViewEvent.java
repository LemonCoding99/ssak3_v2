package com.example.ssak3.domain.product.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductViewEvent {

    private final Long productId;
    private final String ip;

}
