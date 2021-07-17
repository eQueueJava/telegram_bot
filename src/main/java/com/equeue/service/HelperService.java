package com.equeue.service;

import java.util.ArrayList;
import java.util.List;

public class HelperService {

    private HelperService() {
        throw new IllegalStateException("Utility class");
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
