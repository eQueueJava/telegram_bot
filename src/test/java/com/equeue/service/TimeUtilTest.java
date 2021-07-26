package com.equeue.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeUtilTest {

    @ParameterizedTest(name = "{0} -> {1}:{2}")
    @CsvSource({
            "0:00, 0, 0",
            "0:59, 0, 59",
            "00:59, 0, 59",
            "9:30, 9, 30",
            "09:30, 9, 30",
            "23:59, 23, 59"
    })
    void getTimeFromString_whenTimeStringIsOk_isOk(String stringTime, int hour, int minute) {
        assertThat(LocalTime.of(hour, minute), equalTo(TimeUtil.getTimeFromString(stringTime)));
    }

    @ParameterizedTest(name = "\"{0}\" throws Exception")
    @ValueSource(strings = {"", " ", "-5:00", "5:0", "05:0", "55:00", "5:65", "05:00a", "a05:00"})
    void getTimeFromString_whenTimeStringIsWrong_throwException(String stringTime) {
        assertThrows(DateTimeParseException.class, () -> TimeUtil.getTimeFromString(stringTime));
    }

    @ParameterizedTest(name = "{0} -> {1}.{2}.{3}")
    @CsvSource({
            "20.9.2020, 2020, 9, 20",
            "20.09.2020, 2020, 9, 20",
            "20.09.2001, 2001, 9, 20",
            "29.2.2020, 2020, 2, 29",
            "1.1.1970, 1970, 1, 1",
            "01.01.1970, 1970, 1, 1",
            "31.12.1969, 1969, 12, 31",
            "1.1.0001, 1, 1, 1"
    })
    void getDateFromString_whenDateStringIsOk_isOk(String stringDate, int year, int month, int dayOfMonth) {
        assertThat(LocalDate.of(year, month, dayOfMonth), equalTo(TimeUtil.getDateFromString(stringDate)));
    }

    @ParameterizedTest(name = "\"{0}\" throws Exception")
    @ValueSource(strings = {"29.2.2021", "31.4.2020", "", " ", "-5.1.2000", "1.1.2000y", "2001.1.1", "1.2001", "0.1.2001", "01.01.01", "32.04.2021"})
    void getDateFromString_whenDateStringIsWrong_throwException(String stringDate) {
        assertThrows(DateTimeParseException.class, () -> TimeUtil.getDateFromString(stringDate));
    }

    @ParameterizedTest(name = "{0} -> {1}.{2}.{3} {1}:{2}")
    @CsvSource({
            "20.9.2020 0:00, 2020, 9, 20, 0, 0",
            "20.09.2020 0:59, 2020, 9, 20, 0, 59",
            "20.09.2001 15:33, 2001, 9, 20, 15, 33",
            "29.2.2020 00:59, 2020, 2, 29, 0, 59",
            "1.1.1970 9:30, 1970, 1, 1, 9, 30",
            "01.01.1970 09:30, 1970, 1, 1, 9, 30",
            "31.12.1969 23:59, 1969, 12, 31, 23, 59"
    })
    void getDateTimeFromString_whenDateTimeStringIsOk_isOk(
            String stringDateTime, int year, int month, int dayOfMonth, int hour, int minute) {
        assertThat(LocalDateTime.of(year, month, dayOfMonth, hour, minute), equalTo(TimeUtil.getDateTimeFromString(stringDateTime)));
    }

    @ParameterizedTest(name = "\"{0}\" throws Exception")
    @ValueSource(strings = {"29.2.2021 12:12", "31.4.2020 12:12", "", " ", "1.1.2001", "01:01", "1.1.2001 50:00", "32.04.2021 13:00"})
    void getDateTimeFromString_whenDateTimeStringIsWrong_throwException(String stringDateTime) {
        assertThrows(DateTimeParseException.class, () -> TimeUtil.getDateTimeFromString(stringDateTime));
    }

    @ParameterizedTest(name = "{0}:{1} -> {2}")
    @CsvSource({
            "0, 0, 00:00",
            "0, 59, 00:59",
            "0, 59, 00:59",
            "9, 30, 09:30",
            "9, 30, 09:30",
            "23, 59, 23:59"
    })
    void getStringFromTime_whenTimeStringIsOk_isOk(int hour, int minute, String stringTime) {
        assertThat(stringTime, equalTo(TimeUtil.getStringFromTime(LocalTime.of(hour, minute))));
    }

    @ParameterizedTest(name = "{0}.{1}.{2} -> {3}")
    @CsvSource({
            "2020, 9, 20, 20.09.2020",
            "2001, 9, 20, 20.09.2001",
            "2020, 2, 29, 29.02.2020",
            "1970, 1, 1, 01.01.1970",
            "1970, 1, 1, 01.01.1970",
            "1969, 12, 31, 31.12.1969",
            "1, 1, 1, 01.01.0001"
    })
    void getStringFromDate_whenDateStringIsOk_isOk(int year, int month, int dayOfMonth, String stringDate) {
        assertThat(stringDate, equalTo(TimeUtil.getStringFromDate(LocalDate.of(year, month, dayOfMonth))));
    }

    @ParameterizedTest(name = "{0}.{1}.{2} {3}:{4} -> {5}")
    @CsvSource({
            "2020, 9, 20, 0, 0, 20.09.2020 00:00",
            "2020, 9, 20, 0, 59, 20.09.2020 00:59",
            "2001, 9, 20, 15, 33, 20.09.2001 15:33",
            "2020, 2, 29, 0, 59, 29.02.2020 00:59",
            "1970, 1, 1, 9, 30, 01.01.1970 09:30",
            "1970, 1, 1, 9, 30, 01.01.1970 09:30",
            "1969, 12, 31, 23, 59, 31.12.1969 23:59"
    })
    void getStringFromDateTime_whenDateTimeStringIsOk_isOk(
            int year, int month, int dayOfMonth, int hour, int minute, String stringDateTime) {
        assertThat(stringDateTime, equalTo(TimeUtil.getStringFromDateTime(LocalDateTime.of(year, month, dayOfMonth, hour, minute))));
    }

}
