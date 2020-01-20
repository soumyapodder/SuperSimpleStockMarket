package com.jpmorgan.exercise.stockmarket.service;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDataException;
import com.jpmorgan.exercise.stockmarket.model.Stock;
import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface StockService {
    Trade buyStock(Stock stock, BigInteger quantity, BigDecimal price) throws InvalidDataException;
    Trade sellStock(Stock stock, BigInteger quantity, BigDecimal price) throws InvalidDataException;
    BigDecimal calculateVolumeWeightedStockPrice(final String stockSymbol, int intervalInMinutes) throws InvalidDataException;
    BigDecimal calculateGBCEAllShareIndex() throws InvalidDataException;
}
