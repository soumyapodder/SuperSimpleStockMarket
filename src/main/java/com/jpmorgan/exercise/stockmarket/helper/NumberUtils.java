package com.jpmorgan.exercise.stockmarket.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class NumberUtils {

    public static final int DEFAULT_SCALE = 6;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static final UnaryOperator<BigDecimal> formatter = value -> value.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);

    public static final BinaryOperator<BigDecimal> divide = (value, divisor) ->
            value.divide(divisor, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);


    private NumberUtils() {
    }
}
