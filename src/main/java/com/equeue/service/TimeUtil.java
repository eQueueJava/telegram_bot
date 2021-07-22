package com.equeue.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
