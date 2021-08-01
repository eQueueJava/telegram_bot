package com.equeue.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HelperService {
    public static final String PARAM_DIVIDER = "__";

    private HelperService() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] getParams(String command) {
        String[] strings = command.split(PARAM_DIVIDER);
        return Arrays.copyOfRange(strings, 0, strings.length);
    }

    public static Map<String, String> parseRequest(String request){
        Map<String, String> result = new HashMap<>();
        String[] lines = request.split("\n");
        result.put("command", lines[0]);
        for (String line: lines) {
            if(line.contains(":")){
                result.put(line.substring(0, line.indexOf(":")).trim(), line.substring(line.indexOf(":") + 1).trim());
            }
        }
        return result;
    }
}
