package com.jpmorgan.exercise.stockmarket;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public class TestConstants {
    public static final String TEST_STOCK_SYMBOL = "TEA";
    public  static final BigDecimal TEST_PAR_VALUE = new BigDecimal(100);
    public static final BigDecimal TEST_LAST_DIVIDEND = new BigDecimal(25);
    public static final BigDecimal TEST_PRICE = new BigDecimal(5);
    public static final BigDecimal TEST_FIXED_DIVIDEND = new BigDecimal(2);
    public static final BigInteger TEST_QUANTITY = new BigInteger("10");
    public static final Instant TEST_CREATE_TIMESTAMP = Instant.now();

}
