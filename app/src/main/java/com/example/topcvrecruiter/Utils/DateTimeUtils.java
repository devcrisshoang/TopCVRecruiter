package com.example.topcvrecruiter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        return sdf.format(now);
    }

    public static String formatTimeAgo(String inputTime) {
        if (inputTime == null || inputTime.isEmpty()) {
            return "Invalid time";
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Calendar now = Calendar.getInstance();
        Date notificationDate;

        try {

            notificationDate = inputFormat.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }

        Calendar notificationCalendar = Calendar.getInstance();
        assert notificationDate != null;
        notificationCalendar.setTime(notificationDate);

        long timeDifferenceMillis = now.getTimeInMillis() - notificationCalendar.getTimeInMillis();

        if (timeDifferenceMillis < TimeUnit.HOURS.toMillis(24)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);

            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else {
                return hours + " hour ago";
            }
        }

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(notificationCalendar, yesterday)) {
            return "Yesterday";
        }

        if (isSameWeek(notificationCalendar, now)) {
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            int dayOfWeek = notificationCalendar.get(Calendar.DAY_OF_WEEK);
            return daysOfWeek[dayOfWeek - 1]; // Trả về tên thứ
        }

        if (now.get(Calendar.YEAR) == notificationCalendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == notificationCalendar.get(Calendar.MONTH)) {
            long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);
            return days + " the day before";
        }

        if (now.get(Calendar.YEAR) == notificationCalendar.get(Calendar.YEAR)) {
            SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            return dayMonthFormat.format(notificationCalendar.getTime());
        }

        SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return dayMonthYearFormat.format(notificationCalendar.getTime());
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }
}
