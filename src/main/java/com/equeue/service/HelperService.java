package com.equeue.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

}
