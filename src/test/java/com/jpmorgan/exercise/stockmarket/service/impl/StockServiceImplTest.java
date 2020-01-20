package com.jpmorgan.exercise.stockmarket.service.impl;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDataException;
import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.CommonStock;
import com.jpmorgan.exercise.stockmarket.model.Trade;
import com.jpmorgan.exercise.stockmarket.service.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static com.jpmorgan.exercise.stockmarket.TestConstants.*;
import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultCommonStockBuilder;
import static com.jpmorgan.exercise.stockmarket.TestUtils.createTradeDataSet;
import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.formatter;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {

    @Mock
    private TradeService tradeService;

    private StockServiceImpl testee = new StockServiceImpl();

    @Before
    public void init() {
        testee.setTradeService(tradeService);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullStock_whenBuyTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        testee.buyStock(null, TEST_QUANTITY, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullQuantity_whenBuyTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.buyStock(commonStock, null, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenZeroQuantity_whenBuyTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.buyStock(commonStock, BigInteger.ZERO, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullPrice_whenBuyTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.buyStock(commonStock, TEST_QUANTITY, null);
    }

    @Test(expected = InvalidDataException.class)
    public void givenZeroPrice_whenBuyTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.buyStock(commonStock, TEST_QUANTITY, BigDecimal.ZERO);
    }

    @Test
    public void givenValidTrade_whenBuyTradeRecordRequestExecuted_ensureRequestExecutedSuccessfully() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.buyStock(commonStock, TEST_QUANTITY, TEST_PRICE);

        verify(tradeService, times(1)).recordTrade(commonStock, BuySellIndicator.BUY,
                TEST_QUANTITY, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullStock_whenSellTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        testee.sellStock(null, TEST_QUANTITY, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullQuantity_whenSellTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.sellStock(commonStock, null, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenZeroQuantity_whenSellTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.sellStock(commonStock, BigInteger.ZERO, TEST_PRICE);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullPrice_whenSellTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.sellStock(commonStock, TEST_QUANTITY, null);
    }

    @Test(expected = InvalidDataException.class)
    public void givenZeroPrice_whenSellTradeRecordRequestExecuted_ThrowsException() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.sellStock(commonStock, TEST_QUANTITY, BigDecimal.ZERO);
    }

    @Test
    public void givenValidTrade_whenSellTradeRecordRequestExecuted_ensureRequestExecutedSuccessfully() throws InvalidDataException {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        testee.sellStock(commonStock, TEST_QUANTITY, TEST_PRICE);

        verify(tradeService, times(1)).recordTrade(commonStock, BuySellIndicator.SELL,
                TEST_QUANTITY, TEST_PRICE);
    }


    @Test(expected = InvalidDataException.class)
    public void givenNoTradesExists_whenCalculateVolumeWeightedStockPriceExecuted_ThrowsException() throws InvalidDataException {
        testee.calculateVolumeWeightedStockPrice(TEST_STOCK_SYMBOL, 10);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNullStockSymbol_whenCalculateVolumeWeightedStockPriceExecuted_ThrowsException() throws InvalidDataException {
        testee.calculateVolumeWeightedStockPrice(null, 10);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNegativeTimeInterval_whenCalculateVolumeWeightedStockPriceExecuted_ThrowsException() throws InvalidDataException {
        testee.calculateVolumeWeightedStockPrice(TEST_STOCK_SYMBOL, -1);
    }

    @Test(expected = InvalidDataException.class)
    public void givenNegativeTimestamp_whenCalculateVolumeWeightedStockPriceExecuted_ThrowsException() throws InvalidDataException {
        testee.calculateVolumeWeightedStockPrice(TEST_STOCK_SYMBOL, -1);
    }

    @Test
    public void givenInvalidStockSymbol_whenCalculateVolumeWeightedStockPriceExecuted_ensureCalculationIsDefaultedToZero() throws InvalidDataException {
        Set<Trade> trades = new LinkedHashSet<>();

        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        Instant createTimestamp = Instant.now();
        Trade trade = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(TEST_PRICE).quantity(TEST_QUANTITY).stock(commonStock)
                .timestamp(createTimestamp).tradeId(UUID.randomUUID()).build();
        trades.add(trade);

        when(tradeService.getAllRecordedTrades()).thenReturn(trades);

        assertEquals(BigDecimal.ZERO, testee.calculateVolumeWeightedStockPrice("ABC", 10));
    }

    @Test
    public void givenValidData_whenCalculateVolumeWeightedStockPriceExecuted_ensureCalculationIsSuccessful() throws InvalidDataException {
        Set<Trade> trades = createTradeDataSet();

        when(tradeService.getAllRecordedTrades()).thenReturn(trades);

        assertEquals(formatter.apply(new BigDecimal("20.3")),
                testee.calculateVolumeWeightedStockPrice(TEST_STOCK_SYMBOL, 10));
    }


    @Test
    public void givenValidData_whenCalculateVolumeWeightedStockPriceExecuted_ensureCalculationExcludeTradesBeyordIntervalLimit() throws InvalidDataException {
        Set<Trade> trades = new LinkedHashSet<>();

        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        Instant createTimestamp = Instant.now();

        Trade trade = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(TEST_PRICE).tradeId(UUID.randomUUID())
                .quantity(TEST_QUANTITY).stock(commonStock).timestamp(createTimestamp.minus(Duration.ofMinutes(20))).build();
        trades.add(trade);

        Trade trade2 = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(new BigDecimal("30.5"))
                .quantity(new BigInteger("15")).stock(commonStock).timestamp(createTimestamp).tradeId(UUID.randomUUID()).build();
        trades.add(trade2);

        when(tradeService.getAllRecordedTrades()).thenReturn(trades);

        assertEquals(formatter.apply(new BigDecimal("30.5")),
                testee.calculateVolumeWeightedStockPrice(TEST_STOCK_SYMBOL, 10));
    }

    @Test(expected = InvalidDataException.class)
    public void givenNoTradeExist_whenCalculateGBCEAllShareIndexExecuted_throwException() throws InvalidDataException {
        testee.calculateGBCEAllShareIndex();
    }

    @Test
    public void givenValidTradesExist_whenCalculateGBCEAllShareIndexExecuted_ensureCalculationIsSuccessful() throws InvalidDataException {
        Set<Trade> trades = createTradeDataSet();
        when(tradeService.getAllRecordedTrades()).thenReturn(trades);

        assertEquals(formatter.apply(new BigDecimal("11.586618")), testee.calculateGBCEAllShareIndex());
    }
}