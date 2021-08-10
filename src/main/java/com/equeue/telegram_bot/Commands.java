package com.equeue.telegram_bot;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Commands {
    private Commands() {}

    public static final String SHOW_CURRENT_USER_INFO = "/my_info";
    public static final String CREATE_CLIENT = "/reg";
    public static final String SET_CURRENT_USER_TIMEZONE = "/set_my_timezone";
    public static final String CREATE_PROVIDER = "/create_provider";
    public static final String CREATE_SCHEDULE = "/create_schedule";
    public static final String READ_CLIENT = "/read_client";
    public static final String READ_PROVIDER = "/read_provider";
    public static final String SET_FREE_TIME = "/set_free_time";
    public static final String DELETE_CLIENT = "/delete_client";

    private static final Map<String, String> COMMAND_MAP = new LinkedHashMap<>();

    static {
        COMMAND_MAP.put("SHOW_CURRENT_USER_INFO", SHOW_CURRENT_USER_INFO);
        COMMAND_MAP.put("ADD_CLIENT", CREATE_CLIENT);
        COMMAND_MAP.put("SET_CURRENT_USER_TIMEZONE", SET_CURRENT_USER_TIMEZONE);
        COMMAND_MAP.put("ADD_PROVIDER", CREATE_PROVIDER);
        COMMAND_MAP.put("ADD_SCHEDULE", CREATE_SCHEDULE);
        COMMAND_MAP.put("READ_CLIENT", READ_CLIENT);
        COMMAND_MAP.put("READ_PROVIDER", READ_PROVIDER);
        COMMAND_MAP.put("GET_FREE_TIME", SET_FREE_TIME);
        COMMAND_MAP.put("DELETE_CLIENT", DELETE_CLIENT);
    }

    public static Map<String, String> getCommandMap() {
        return COMMAND_MAP;
    }
}
