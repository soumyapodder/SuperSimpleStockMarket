package com.jpmorgan.exercise.stockmarket.service.impl;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDataException;
import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.Stock;
import com.jpmorgan.exercise.stockmarket.model.Trade;
import com.jpmorgan.exercise.stockmarket.service.StockService;
import com.jpmorgan.exercise.stockmarket.service.TradeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.divide;
import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.formatter;

public class StockServiceImpl implements StockService {

    private TradeService tradeService = null;

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public Trade buyStock(Stock stock, BigInteger quantity, BigDecimal price) throws InvalidDataException {
        validateTrade(stock, quantity, price);
        return tradeService.recordTrade(stock, BuySellIndicator.BUY, quantity, price);
    }

    @Override
    public Trade sellStock(Stock stock, BigInteger quantity, BigDecimal price) throws InvalidDataException {
        validateTrade(stock, quantity, price);
        return tradeService.recordTrade(stock, BuySellIndicator.SELL, quantity, price);
    }

    @Override
    public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol, int intervalInMinutes) throws InvalidDataException {
        if (null == stockSymbol || intervalInMinutes < 0) {
            throw new InvalidDataException("Either Stock is null or Interval is Negative. Stock " + stockSymbol +
                    " , Interval in Minutes = " + intervalInMinutes);
        }

        final Set<Trade> trades = tradeService.getAllRecordedTrades();
        if (CollectionUtils.isEmpty(trades)) {
            throw new InvalidDataException("No trades exist for Volume Weighted Stock Price calculation.");
        }

        AtomicInteger sumQuantity = new AtomicInteger(0);
        final Instant cutOffTime = Instant.now().minus(Duration.ofMinutes(intervalInMinutes));

        BigDecimal sumPriceMultiplyQty = trades.parallelStream()
                .filter(trade -> trade.getTimestamp().isAfter(cutOffTime))
                .filter(trade -> trade.getStock().getStockSymbol().equals(stockSymbol))
                .map(trade -> {
                    sumQuantity.addAndGet(trade.getQuantity().intValue());
                    return trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity().longValue()));
                })
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        if (BigDecimal.ZERO.compareTo(sumPriceMultiplyQty) == 0) {
            return BigDecimal.ZERO;
        }

        return divide.apply(sumPriceMultiplyQty, BigDecimal.valueOf(sumQuantity.get()));
    }

    @Override
    public BigDecimal calculateGBCEAllShareIndex() throws InvalidDataException {
        final Set<Trade> trades = tradeService.getAllRecordedTrades();
        if (CollectionUtils.isEmpty(trades)) {
            throw new InvalidDataException("No trades exist for GBCE calculation.");
        }
        Double multipliedPrice = trades.parallelStream().map(Trade::getPrice)
                .map(BigDecimal::doubleValue)
                .filter(price -> (price > 0))
                .reduce(1D, (a, b) -> a * b);

        double result = Math.pow(multipliedPrice, (1D / trades.size()));
        return formatter.apply(BigDecimal.valueOf(result));
    }


    private void validateTrade(Stock stock, BigInteger quantity, BigDecimal price) throws InvalidDataException {
        if (null == stock || StringUtils.isEmpty(stock.getStockSymbol())) {
            throw new InvalidDataException("Invalid request for Trade. Stock = " + stock);
        }

        if (null == quantity || BigInteger.ZERO.compareTo(quantity) >= 0) {
            throw new InvalidDataException("Invalid request for Trade. Quantity = " + quantity);
        }

        if (null == price || BigDecimal.ZERO.compareTo(price) >= 0) {
            throw new InvalidDataException("Invalid request for Trade. Price = " + price);
        }
    }
}
