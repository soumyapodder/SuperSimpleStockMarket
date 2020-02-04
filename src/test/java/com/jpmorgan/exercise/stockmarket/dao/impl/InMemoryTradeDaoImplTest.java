package com.jpmorgan.exercise.stockmarket.dao.impl;

import com.jpmorgan.exercise.stockmarket.dao.TradeDao;
import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.CommonStock;
import com.jpmorgan.exercise.stockmarket.model.Trade;
import org.junit.Before;
import org.junit.Test;

import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultCommonStockBuilder;
import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultTrade;
import static org.junit.Assert.assertEquals;

public class InMemoryTradeDaoImplTest {

    private TradeDao testee = new InMemoryTradeDaoImpl();

    @Before
    public void setUp() {
        testee.clearAllTrades();
    }

    @Test
    public void givenNewTradeAdded_ensureItAddedSuccessfully() {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        Trade trade = aDefaultTrade(commonStock, BuySellIndicator.BUY).build();

        testee.addTrade(trade);
        assertEquals(1, testee.getAllTrades().size());
    }

    @Test
    public void givenTradesAlreadyExists_ensureGetAllTradesReturnSuccessfully() {
        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        Trade trade = aDefaultTrade(commonStock, BuySellIndicator.BUY).build();
        testee.addTrade(trade);

        Trade trade2 = aDefaultTrade(commonStock, BuySellIndicator.SELL).build();
        testee.addTrade(trade2);

        assertEquals(2, testee.getAllTrades().size());
    }

}