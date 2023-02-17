package me.swerve.riseuhc.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addSecondsToJavaUtilDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static int differenceInMinutes(Date dateOne, Date dateTwo) {
        return (int) TimeUnit.MINUTES.convert(dateOne.getTime() - dateTwo.getTime(), TimeUnit.MILLISECONDS);
    }

    public static int differenceInSeconds(Date dateOne, Date dateTwo) { ;
        return (int) TimeUnit.SECONDS.convert(dateOne.getTime() - dateTwo.getTime(), TimeUnit.MILLISECONDS);
    }

    public static String millisToSeconds(long millis) {
        return new DecimalFormat("#0.0").format(millis / 1000.0F);
    }
}
