package com.jpmorgan.exercise.stockmarket.model;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDividendException;
import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import org.junit.Test;

import java.math.BigDecimal;

import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultCommonStockBuilder;
import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.formatter;
import static org.junit.Assert.assertEquals;


public class CommonStockTest {

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsNull_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        CommonStock commonStock = aDefaultCommonStockBuilder().price(null).build();

        commonStock.calculateDividendYield();
    }

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsZero_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        CommonStock commonStock = aDefaultCommonStockBuilder().price(BigDecimal.ZERO).build();

        commonStock.calculateDividendYield();
    }

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsNegative_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        CommonStock commonStock = aDefaultCommonStockBuilder().price(new BigDecimal(-100)).build();

        commonStock.calculateDividendYield();
    }

    @Test
    public void givenValidPrice_whenCalculatingDividendYield_EnsureDividendCalculatedCorrectly() throws InvalidPriceException {
        CommonStock commonStock = aDefaultCommonStockBuilder().price(new BigDecimal(5)).build();

        assertEquals("Dividend calculated for valid price", formatter.apply(new BigDecimal(5)), commonStock.calculateDividendYield());
    }

    @Test(expected = InvalidDividendException.class)
    public void givenLastDividendIsNegative_whenCalculatingPERatio_throwException() throws InvalidPriceException, InvalidDividendException {
        CommonStock commonStock = aDefaultCommonStockBuilder().lastDividend(new BigDecimal(-100)).build();

        commonStock.calculatePERatio();
    }

    @Test(expected = InvalidDividendException.class)
    public void givenLastDividendIsZero_whenCalculatingPERatio_throwException() throws InvalidPriceException, InvalidDividendException {
        CommonStock commonStock = aDefaultCommonStockBuilder().lastDividend(BigDecimal.ZERO).build();

        commonStock.calculatePERatio();
    }

    @Test
    public void givenValidPriceAndLastDividend_whenCalculatingPERatio_EnsurePERatioCalculatedCorrectly() throws InvalidPriceException, InvalidDividendException {
        CommonStock commonStock = aDefaultCommonStockBuilder().price(new BigDecimal(5)).build();

        assertEquals("PE Ratio calculated for valid price", formatter.apply(new BigDecimal(1))
                , commonStock.calculatePERatio());
    }
}