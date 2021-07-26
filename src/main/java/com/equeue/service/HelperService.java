package com.equeue.service;

import java.util.Arrays;

public class HelperService {
    public static final String PARAM_DIVIDER = "__";

    private HelperService() {
        throw new IllegalStateException("Utility class");
    }

    static String[] getParams(String command) {
        String[] strings = command.split("__");
        return Arrays.copyOfRange(strings, 1, strings.length);
    }
}
