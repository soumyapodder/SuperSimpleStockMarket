package com.jpmorgan.exercise.stockmarket.dao.impl;

import com.jpmorgan.exercise.stockmarket.dao.TradeDao;
import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.util.LinkedHashSet;
import java.util.Set;

public class InMemoryTradeDaoImpl implements TradeDao {

    private static final Set<Trade> tradeCollector = new LinkedHashSet<>();

    @Override
    public void addTrade(Trade trade) {
        tradeCollector.add(trade);
    }

    @Override
    public Set<Trade> getAllTrades() {
        return tradeCollector;
    }

    @Override
    public void clearAllTrades() {
        tradeCollector.clear();
    }
}
