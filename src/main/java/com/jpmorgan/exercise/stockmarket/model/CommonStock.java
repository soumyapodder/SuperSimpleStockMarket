package com.jpmorgan.exercise.stockmarket.model;

import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.divide;

@Getter
public class CommonStock extends Stock {

    @Builder(builderMethodName = "commonStockBuilder")
    public CommonStock(String stockSymbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal price) {
        this.stockSymbol = stockSymbol;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
        this.price = price;
        this.type = StockType.COMMON;
    }

    @Override
    public BigDecimal calculateDividendYield() throws InvalidPriceException {
        if (null == price || BigDecimal.ZERO.compareTo(price) >= 0)
            throw new InvalidPriceException("Price is Null , zero or negative.Price = " + price);

        return divide.apply(lastDividend, price);
    }
}
