package com.jpmorgan.exercise.stockmarket.service;

import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.Stock;
import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

public interface TradeService {
    Trade recordTrade(Stock stock, BuySellIndicator buySellIndicator, BigInteger quantity, BigDecimal price);

    Collection<Trade> getAllRecordedTrades();
}
