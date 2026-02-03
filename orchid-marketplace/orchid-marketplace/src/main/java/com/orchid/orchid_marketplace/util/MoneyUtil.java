package com.orchid.orchid_marketplace.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {
    private MoneyUtil() {}

    public static long dollarsToCents(BigDecimal dollars) {
        if (dollars == null) return 0L;
        return dollars
            .multiply(BigDecimal.valueOf(100))
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact();
    }

    public static BigDecimal centsToDollars(long cents) {
        return BigDecimal.valueOf(cents)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public static long percentOfCents(long baseCents, BigDecimal percent) {
        if (percent == null) return 0L;
        return BigDecimal.valueOf(baseCents)
            .multiply(percent)
            .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
            .longValueExact();
    }
}
