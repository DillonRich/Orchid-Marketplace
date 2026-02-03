package com.orchid.orchid_marketplace.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public final class InputSanitizer {
    private InputSanitizer() {}

    public static String sanitize(String input) {
        if (input == null) return null;
        return Jsoup.clean(input, Safelist.basic());
    }
}
