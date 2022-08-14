package com.fsquiroz.campsite;

import org.springframework.lang.NonNull;

public class TestUtils {

    public static Exception catchException(@NonNull Catchable c) {
        try {
            c.run();
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    @FunctionalInterface
    public static interface Catchable {
        void run() throws Exception;
    }
}
