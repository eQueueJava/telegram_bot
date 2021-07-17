package com.equeue.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static final String TIME_PATTERN = "H:mm";
    public static final String DATE_PATTERN = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN = "dd.MM.yyyy H:mm";

    private TimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalTime localTimeFromString(String stringTime) {
        return LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public static LocalDate localDateFromString(String stringDate) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static LocalDateTime localDateTimeFromString(String stringDateTime) {
        return LocalDateTime.parse(stringDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public static String stringFromLocalTime(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public static String stringFromLocalDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String stringFromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public static LocalTime localTimeFromUtcTimeForZone(LocalTime localTime, ZoneId zoneId) {
        return ZonedDateTime.of(localTime.atDate(LocalDate.EPOCH), ZoneId.of("UTC"))
                .withZoneSameInstant(zoneId).toLocalTime();
    }

    static LocalTime utcTimeFromLocalTimeAndZone(LocalTime localTime, ZoneId zoneId) {
        return ZonedDateTime.of(localTime.atDate(LocalDate.EPOCH), zoneId).
                withZoneSameInstant(ZoneOffset.UTC).toLocalTime();
    }

    public static LocalDateTime localDateTimeFromUtcDateTimeForZone(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, ZoneId.of("UTC"))
                .withZoneSameInstant(zoneId).toLocalDateTime();
    }

    public static LocalDateTime utcDateTimeFromLocalDateTimeAndZone(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, zoneId)
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    /*
        input:    16.07.2021,     17.07.2021,     18.07.2021
        output:   6(п.ятниця),    7(субота),      1(неділя)
         */
    public static int getDayFromDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        Date dateNew = formatter.parse(date);
        c.setTime(dateNew);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /*
        input:    10:30, 10:20, 10:50.
        output:   10.5,  10.3,  10.8.
         */
    public static double changeStrTimeToDouble(String time) {
        String[] split = time.split(":");
        double v = Double.parseDouble(split[1]) / 60;
        DecimalFormat f = new DecimalFormat("##.0");
        String format = "0" + f.format(v);
        format = format.replace(',', '.');
        return Integer.parseInt(split[0]) + Double.parseDouble(format);
    }

}
