package ru.practicum.ewm.util;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_TIME_FORMAT);

    public static String getLocalDateTimeAsString(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static String encodeDateTimeToString(LocalDateTime localDateTime) {
        String formattedDateTime = localDateTime.format(DATE_TIME_FORMATTER);
        return URLEncoder.encode(formattedDateTime, StandardCharsets.UTF_8);
    }

    public static LocalDateTime decodeStringToDateTime(String encodedDateTime) {
        String decodedDateTime = URLEncoder.encode(encodedDateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDateTime, DATE_TIME_FORMATTER);
    }
}
