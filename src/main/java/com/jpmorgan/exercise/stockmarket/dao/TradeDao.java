package com.jpmorgan.exercise.stockmarket.dao;

import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.util.Collection;

public interface TradeDao {
    void addTrade(Trade trade);
    Collection<Trade> getAllTrades();
    void clearAllTrades();
}
