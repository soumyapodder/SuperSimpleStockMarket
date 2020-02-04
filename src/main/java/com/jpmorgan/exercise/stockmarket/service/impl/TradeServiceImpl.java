package com.jpmorgan.exercise.stockmarket.service.impl;

import com.jpmorgan.exercise.stockmarket.dao.TradeDao;
import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.Stock;
import com.jpmorgan.exercise.stockmarket.model.Trade;
import com.jpmorgan.exercise.stockmarket.service.TradeService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public class TradeServiceImpl implements TradeService {

    private TradeDao tradeDao = null;

    public void setTradeDao(TradeDao tradeDao) {
        this.tradeDao = tradeDao;
    }

    @Override
    public Trade recordTrade(Stock stock, BuySellIndicator buySellIndicator, BigInteger quantity, BigDecimal price) {
        Trade trade = Trade.builder().buySellIndicator(buySellIndicator).price(price).quantity(quantity).stock(stock)
                .timestamp(Instant.now()).tradeId(UUID.randomUUID()).build();
        tradeDao.addTrade(trade);
        return trade;
    }

    @Override
    public Collection<Trade> getAllRecordedTrades() {
        return tradeDao.getAllTrades();
    }
}
