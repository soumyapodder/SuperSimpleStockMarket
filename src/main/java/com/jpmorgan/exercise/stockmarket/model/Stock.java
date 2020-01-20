package com.jpmorgan.exercise.stockmarket.model;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDividendException;
import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.divide;

@Getter
@Setter
@ToString
public abstract class Stock {

    protected String stockSymbol;
    protected BigDecimal lastDividend;
    protected BigDecimal parValue;
    protected BigDecimal price;
    protected StockType type;

    public abstract BigDecimal calculateDividendYield() throws InvalidPriceException;

    public BigDecimal calculatePERatio() throws InvalidPriceException, InvalidDividendException {
        BigDecimal dividendYield = calculateDividendYield();
        if (BigDecimal.ZERO.compareTo(dividendYield) >= 0) {
            throw new InvalidDividendException("Dividend is Zero or Negative. Dividend = " + dividendYield);
        }
        return divide.apply(price, dividendYield);
    }

}
