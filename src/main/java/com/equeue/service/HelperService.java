package com.equeue.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelperService {

    public static Timestamp timeOf(String stringTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return new Timestamp(dateFormat.parse(stringTime).getTime());
    }

    public static Timestamp dateOf(String stringDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return new Timestamp(dateFormat.parse(stringDate).getTime());
    }

    public static String dateOf(Timestamp timestamp) {
        return dateOf(timestamp.getTime());
    }

    public static String timeOf(Timestamp timestamp) {
        return timeOf(timestamp.getTime());
    }

    public static String timeOf(Long l) {
        return new SimpleDateFormat("HH:mm").format(l);
    }

    public static String dateOf(Long l) {
        return new SimpleDateFormat("dd.MM.yyyy").format(l);
    }

    public static Timestamp stringToTimestamp(String value) {
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(value));
        return Timestamp.valueOf(localDateTime);
    }

    /*
    input:    16.07.2021,     17.07.2021,     18.07.2021
    output:   6(п.ятниця),    7(субота),      1(неділя)
     */
    public static int getDeyFromDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
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

    /*
    input:    [9.0, 09:0, 9:00]
    output:   [09:00, 09:00, 09:00]
     */
    public static List<String> formList(List<String> list) {
        List<String> arrayList = new ArrayList<>();
        String str = "";
        for (String s : list) {
            s = s.replace('.', ':');
            String[] split = s.split(":");
            if (split[0].length() == 1) {
                str = "0" + split[0];
            } else {
                str = split[0];
            }
            str += ":";
            if (split[1].length() == 1) {
                str += split[1] + "0";
            } else {
                str += split[1];
            }
            arrayList.add(str);
            str = "";
        }
        return arrayList;
    }
}
