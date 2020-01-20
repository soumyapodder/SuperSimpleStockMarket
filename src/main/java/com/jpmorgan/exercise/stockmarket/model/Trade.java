package com.jpmorgan.exercise.stockmarket.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Trade {
    private final UUID tradeId;
    private final Instant timestamp;
    private final BigInteger quantity;
    private final BuySellIndicator buySellIndicator;
    private final BigDecimal price;
    private final Stock stock;
}
