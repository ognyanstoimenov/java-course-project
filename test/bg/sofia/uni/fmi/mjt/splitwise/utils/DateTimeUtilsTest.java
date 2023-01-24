package bg.sofia.uni.fmi.mjt.splitwise.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2022, 5, 1, 12, 30);
    private static final String TEST_STRING = "2022-05-01T12:30:00";

    @Test
    void dateToString() {
        assertEquals(TEST_STRING, DateTimeUtils.dateToString(TEST_DATE), "Should return expected value");
    }

    @Test
    void stringToDate() {
        assertEquals(TEST_DATE, DateTimeUtils.stringToDate(TEST_STRING), "Should return expected value");
    }
}
