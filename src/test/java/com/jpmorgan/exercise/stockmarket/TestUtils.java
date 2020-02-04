package com.jpmorgan.exercise.stockmarket;

import com.jpmorgan.exercise.stockmarket.model.BuySellIndicator;
import com.jpmorgan.exercise.stockmarket.model.CommonStock;
import com.jpmorgan.exercise.stockmarket.model.PreferredStock;
import com.jpmorgan.exercise.stockmarket.model.Trade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static com.jpmorgan.exercise.stockmarket.TestConstants.*;

public class TestUtils {

    public static PreferredStock.PreferredStockBuilder aDefaultPreferredStockBuilder() {
        return PreferredStock.preferredStockBuilder().stockSymbol(TEST_STOCK_SYMBOL).parValue(TEST_PAR_VALUE)
                .lastDividend(TEST_LAST_DIVIDEND).fixedDividend(TEST_FIXED_DIVIDEND).price(TEST_PRICE);
    }


    public static CommonStock.CommonStockBuilder aDefaultCommonStockBuilder() {
        return CommonStock.commonStockBuilder().stockSymbol(TEST_STOCK_SYMBOL).parValue(TEST_PAR_VALUE)
                .lastDividend(TEST_LAST_DIVIDEND).price(TEST_PRICE);
    }

    public static Set<Trade> createTradeDataSet() {
        Set<Trade> trades = new LinkedHashSet<>();

        CommonStock commonStock = aDefaultCommonStockBuilder().build();
        Instant createTimestamp = Instant.now();

        Trade trade = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(TEST_PRICE)
                .quantity(TEST_QUANTITY).stock(commonStock).timestamp(createTimestamp).tradeId(UUID.randomUUID()).build();
        trades.add(trade);

        Trade trade2 = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(new BigDecimal("30.5"))
                .quantity(new BigInteger("15")).stock(commonStock).timestamp(createTimestamp).tradeId(UUID.randomUUID()).build();
        trades.add(trade2);

        CommonStock commonStock2 = aDefaultCommonStockBuilder().stockSymbol("XYZ").build();
        Trade trade3 = Trade.builder().buySellIndicator(BuySellIndicator.SELL).price(new BigDecimal("10.2"))
                .quantity(new BigInteger("44")).stock(commonStock2).timestamp(createTimestamp).tradeId(UUID.randomUUID()).build();
        trades.add(trade3);
        return trades;
    }

    public static Trade.TradeBuilder aDefaultTrade(CommonStock commonStock, BuySellIndicator buySellIndicator) {
        return Trade.builder().stock(commonStock).quantity(TEST_QUANTITY).price(TEST_PRICE).timestamp(Instant.now())
                .buySellIndicator(buySellIndicator).tradeId(UUID.randomUUID());
    }
}
