package com.jpmorgan.exercise.stockmarket.service.impl;

import com.jpmorgan.exercise.stockmarket.dao.TradeDao;
import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.CommonStock;
import com.jpmorgan.exercise.stockmarket.model.Trade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.jpmorgan.exercise.stockmarket.TestConstants.TEST_PRICE;
import static com.jpmorgan.exercise.stockmarket.TestConstants.TEST_QUANTITY;
import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultCommonStockBuilder;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceImplTest {

    private TradeServiceImpl testee = new TradeServiceImpl();

    @Mock
    private TradeDao tradeDao;

    @Before
    public void setUp() {
        testee.setTradeDao(tradeDao);
    }

    @Test
    public void givenValidTradeDetailsPassed_whenRecordTradeCalled_ensureTradeCreatedCorrectly() {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();

        Trade actualTrade = testee.recordTrade(commonStock, BuySellIndicator.SELL, TEST_QUANTITY, TEST_PRICE);

        assertEquals(TEST_QUANTITY, actualTrade.getQuantity());
        assertEquals(TEST_PRICE, actualTrade.getPrice());
        assertEquals(BuySellIndicator.SELL, actualTrade.getBuySellIndicator());
        assertEquals(commonStock, actualTrade.getStock());
    }
}