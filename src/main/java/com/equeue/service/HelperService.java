package com.equeue.service;

import java.util.Arrays;

public class HelperService {
    public static final String PARAM_DIVIDER = "__";

    private HelperService() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] getParams(String command) {
        String[] strings = command.split(PARAM_DIVIDER);
        return Arrays.copyOfRange(strings, 0, strings.length);
    }
}
