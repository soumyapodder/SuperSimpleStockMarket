package com.jpmorgan.exercise.stockmarket.model;

import com.jpmorgan.exercise.stockmarket.exception.InvalidDividendException;
import com.jpmorgan.exercise.stockmarket.exception.InvalidPriceException;
import org.junit.Test;

import java.math.BigDecimal;

import static com.jpmorgan.exercise.stockmarket.TestUtils.aDefaultPreferredStockBuilder;
import static com.jpmorgan.exercise.stockmarket.helper.NumberUtils.formatter;
import static org.junit.Assert.assertEquals;


public class PreferredStockTest {

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsNull_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().price(null).build();

        preferredStock.calculateDividendYield();
    }

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsZero_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().price(BigDecimal.ZERO).build();

        preferredStock.calculateDividendYield();
    }

    @Test(expected = InvalidPriceException.class)
    public void givenPriceIsNegative_whenCalculatingDividendYield_throwException() throws InvalidPriceException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().price(new BigDecimal(-100)).build();

        preferredStock.calculateDividendYield();
    }

    @Test
    public void givenValidPrice_whenCalculatingDividendYield_EnsureDividendCalculatedCorrectly() throws InvalidPriceException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().price(new BigDecimal(50)).build();

        assertEquals("Dividend calculated for valid price", formatter.apply(new BigDecimal(4))
                , preferredStock.calculateDividendYield());
    }

    @Test(expected = InvalidDividendException.class)
    public void givenFixedDividendIsNegative_whenCalculatingPERatio_throwException() throws InvalidPriceException, InvalidDividendException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().fixedDividend(new BigDecimal(-100)).build();

        preferredStock.calculatePERatio();
    }

    @Test(expected = InvalidDividendException.class)
    public void givenFixedDividendIsZero_whenCalculatingPERatio_throwException() throws InvalidPriceException, InvalidDividendException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().fixedDividend(BigDecimal.ZERO).build();

        preferredStock.calculatePERatio();
    }

    @Test
    public void givenValidPriceAndLastDividend_whenCalculatingPERatio_EnsurePERatioCalculatedCorrectly() throws InvalidPriceException, InvalidDividendException {
        PreferredStock preferredStock = aDefaultPreferredStockBuilder().price(new BigDecimal(5)).build();

        assertEquals("PE Ratio calculated for valid price", formatter.apply(new BigDecimal("0.125")),
                preferredStock.calculatePERatio());
    }

}