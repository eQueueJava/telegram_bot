package com.equeue.telegram_bot;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Commands {
    public static final String CREATE_CLIENT = "/create_client";
    public static final String CREATE_PROVIDER = "/create_provider";
    public static final String CREATE_SCHEDULE = "/create_schedule";
    public static final String READ_CLIENT = "/read_client";
    public static final String READ_PROVIDER = "/read_provider";
    public static final String GET_FREE_TIME = "/get_free_time";
    public static final String INPUT_TIME = "/input_time";

    public static Map<String, String> commandMap = new LinkedHashMap<>();

    static {
        commandMap.put("ADD_CLIENT", CREATE_CLIENT);
        commandMap.put("ADD_PROVIDER", CREATE_PROVIDER);
        commandMap.put("ADD_SCHEDULE", CREATE_SCHEDULE);
        commandMap.put("READ_CLIENT", READ_CLIENT);
        commandMap.put("READ_PROVIDER", READ_PROVIDER);
        commandMap.put("GET_FREE_TIME", GET_FREE_TIME);
        commandMap.put("INPUT_TIME", INPUT_TIME);
    }
}
