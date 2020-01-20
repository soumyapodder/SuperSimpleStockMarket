package com.jpmorgan.exercise.stockmarket.dao;

import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.util.Set;

public interface TradeDao {
    void addTrade(Trade trade);
    Set<Trade> getAllTrades();
    void clearAllTrades();
}
