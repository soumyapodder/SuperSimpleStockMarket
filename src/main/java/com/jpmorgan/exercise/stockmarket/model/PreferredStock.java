package com.jpmorgan.exercise.stockmarket.model;

import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.divide;

@Getter
@ToString(callSuper = true)
public class PreferredStock extends Stock {

    protected BigDecimal fixedDividend;

    @Builder(builderMethodName = "preferredStockBuilder")
    public PreferredStock(String stockSymbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal price, BigDecimal fixedDividend) {
        this.stockSymbol = stockSymbol;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
        this.price = price;
        this.type = StockType.PREFERRED;
        this.fixedDividend = fixedDividend;
    }

    @Override
    public BigDecimal calculateDividendYield() throws InvalidPriceException {
        if (null == price || BigDecimal.ZERO.compareTo(price) >= 0)
            throw new InvalidPriceException("Price is Null , Zero or negative.Price = " + price);

        return divide.apply(fixedDividend.multiply(parValue), price);
    }
}
