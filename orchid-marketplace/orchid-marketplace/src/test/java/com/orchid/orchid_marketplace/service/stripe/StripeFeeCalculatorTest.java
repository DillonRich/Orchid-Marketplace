package com.orchid.orchid_marketplace.service.stripe;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class StripeFeeCalculatorTest {

    @Test
    void calculate_appliesPercentToSubtotalOnly_andCapsApplicationFee() {
        long subtotalCents = 10_00; // $10.00
        long shippingCents = 5_00;  // $5.00 (seller revenue)
        BigDecimal platformPercent = BigDecimal.valueOf(10);
        long outstandingListingFeesCents = 25; // $0.25

        StripeFeeBreakdown b = StripeFeeCalculator.calculate(subtotalCents, shippingCents, platformPercent, outstandingListingFeesCents);

        assertEquals(100, b.getPlatformFeeCents());
        assertEquals(25, b.getListingFeeAppliedCents());
        // app fee should be platform fee + listing fee recovery
        assertEquals(125, b.getApplicationFeeCents());
    }
}
