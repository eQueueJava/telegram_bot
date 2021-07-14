package com.equeue.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperService {

    public static Timestamp timeOf(String stringTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date parsedDate = dateFormat.parse(stringTime);
        return new Timestamp(parsedDate.getTime());
    }

    public static String dateOf(Long l) {
        return new SimpleDateFormat("dd.MM.yyyy").format(l);
    }

}
