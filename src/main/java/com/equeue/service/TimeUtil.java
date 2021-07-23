package com.equeue.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;

public class TimeUtil {

    public static final String TIME_PATTERN_PARSE = "H:mm";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_PATTERN_PARSE = "d.M.yyyy";
    public static final String DATE_PATTERN = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN_PARSE = "d.M.yyyy H:mm";
    public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    public static final DateTimeFormatter DATE_FORMATTER_PARSE = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NORMAL)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 4, SignStyle.EXCEEDS_PAD)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);

    public static final DateTimeFormatter DATE_TIME_FORMATTER_PARSE = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NORMAL)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 4, SignStyle.EXCEEDS_PAD)
            .appendLiteral(' ')
            .appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NORMAL)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);

    private TimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalTime getTimeFromString(String stringTime) {
        return LocalTime.parse(stringTime,
                DateTimeFormatter.ofPattern(TIME_PATTERN_PARSE));
    }

    public static LocalDate getDateFromString(String stringDate) {
        return LocalDate.parse(stringDate, DATE_FORMATTER_PARSE);
    }

    public static LocalDateTime getDateTimeFromString(String stringDateTime) {
        return LocalDateTime.parse(stringDateTime, DATE_TIME_FORMATTER_PARSE);
    }

    public static String getStringFromTime(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public static String getStringFromDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String getStringFromDateTime(LocalDateTime localDateTime) {
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

}
