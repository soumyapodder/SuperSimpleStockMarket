package com.jpmorgan.exercise.stockmarket.dao.impl;

import com.jpmorgan.exercise.stockmarket.dao.TradeDao;
import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTradeDaoImpl implements TradeDao {

    private static final Map<UUID, Trade> tradeCollector = new ConcurrentHashMap<>();

    @Override
    public void addTrade(Trade trade) {
        tradeCollector.put(trade.getTradeId(), trade);
    }

    @Override
    public Collection<Trade> getAllTrades() {
        return tradeCollector.values();
    }

    @Override
    public void clearAllTrades() {
        tradeCollector.clear();
    }
}
