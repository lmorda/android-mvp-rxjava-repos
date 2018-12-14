package com.example.lmorda.rxrepos.util;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateTimeHelper {

    private static long getSecondsSince(String timeStamp1, String timeStamp2) {
        return Instant.parse(timeStamp1).getEpochSecond() - Instant.parse(timeStamp2).getEpochSecond();
    }

    private static long getMinutesSince(String timeStamp1, String timeStamp2) {
        long seconds = Instant.parse(timeStamp1).getEpochSecond()- Instant.parse(timeStamp2).getEpochSecond();
        return seconds/60;
    }

    private static boolean isSameDay(String timeStamp1, String timeStamp2) {
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.parse(timeStamp1), ZoneId.systemDefault());
        OffsetDateTime dateTime2 = OffsetDateTime.ofInstant(Instant.parse(timeStamp2), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMddYYYY");
        return dateTime1.format(dtf).equals(dateTime2.format(dtf));
    }

    private static long getDaysSince(String timeStamp1, String timeStamp2) {
        long seconds = Instant.parse(timeStamp1).getEpochSecond() - Instant.parse(timeStamp2).getEpochSecond();
        return seconds/(60*60*24);
    }

    private static boolean isSameYear(String timeStamp1, String timeStamp2) {
        OffsetDateTime dateTime1 = OffsetDateTime.ofInstant(Instant.parse(timeStamp1), ZoneId.systemDefault());
        OffsetDateTime dateTime2 = OffsetDateTime.ofInstant(Instant.parse(timeStamp2), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY");
        return dateTime1.format(dtf).equals(dateTime2.format(dtf));
    }

    public static String getFormattedTimeStamp(String timestamp1, String timestamp2) {
        if (getSecondsSince(timestamp1, timestamp2) < 60) {
            return "Now";
        }
        else if (getMinutesSince(timestamp1, timestamp2) < 60) {
            return getMinutesSince(timestamp1, timestamp2) + " mins ago";
        }
        else if (isSameDay(timestamp1, timestamp2)) {
            OffsetDateTime dateTime = OffsetDateTime.ofInstant(Instant.parse(timestamp2), ZoneId.systemDefault());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
            return dateTime.format(dtf);
        }
        else if (getDaysSince(timestamp1, timestamp2) < 7) {
            OffsetDateTime dateTime = OffsetDateTime.ofInstant(Instant.parse(timestamp2), ZoneId.systemDefault());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
            return dateTime.format(dtf);
        }
        else if (isSameYear(timestamp1, timestamp2)) {
            OffsetDateTime dateTime = OffsetDateTime.ofInstant(Instant.parse(timestamp2), ZoneId.systemDefault());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
            return dateTime.format(dtf).replace("0", "");
        }
        else {
            OffsetDateTime dateTime = OffsetDateTime.ofInstant(Instant.parse(timestamp2), ZoneId.systemDefault());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM YYYY");
            return dateTime.format(dtf);
        }
    }

}
