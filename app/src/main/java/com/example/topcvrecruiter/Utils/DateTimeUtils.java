package com.example.topcvrecruiter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
    public static String getCurrentTime() {
        // Định dạng thời gian theo chuẩn ISO 8601 (yyyy-MM-dd'T'HH:mm:ss)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        // Lấy thời gian hiện tại
        Date now = new Date();
        // Trả về chuỗi thời gian đã format
        return sdf.format(now);
    }

    public static String formatTimeAgo(String inputTime) {
        // Kiểm tra inputTime có null không
        if (inputTime == null || inputTime.isEmpty()) {
            return "Invalid time"; // Trả về thông báo lỗi hoặc giá trị mặc định
        }

        // Điều chỉnh định dạng thời gian cho đúng với định dạng có khả năng bạn đang sử dụng
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Calendar now = Calendar.getInstance(); // Thời gian hiện tại
        Date notificationDate;

        try {
            // Chuyển chuỗi thành đối tượng Date
            notificationDate = inputFormat.parse(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date"; // Nếu không parse được chuỗi, trả về lỗi
        }

        Calendar notificationCalendar = Calendar.getInstance();
        notificationCalendar.setTime(notificationDate);

        // Tính thời gian đã trôi qua
        long timeDifferenceMillis = now.getTimeInMillis() - notificationCalendar.getTimeInMillis();

        // Trường hợp nếu trong cùng ngày, hiển thị theo giây, phút, giờ
        if (timeDifferenceMillis < TimeUnit.HOURS.toMillis(24)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);

            if (seconds < 60) {
                return seconds + " giây trước";
            } else if (minutes < 60) {
                return minutes + " phút trước";
            } else {
                return hours + " giờ trước";
            }
        }

        // Trường hợp nếu là "hôm qua"
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(notificationCalendar, yesterday)) {
            return "Hôm qua";
        }

        // Nếu thời gian nằm trong cùng tuần (không phải hôm qua)
        if (isSameWeek(notificationCalendar, now)) {
            String[] daysOfWeek = {"Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy"};
            int dayOfWeek = notificationCalendar.get(Calendar.DAY_OF_WEEK);
            return daysOfWeek[dayOfWeek - 1]; // Trả về tên thứ
        }

        // Nếu thời gian đã qua nhiều hơn 1 tuần nhưng cùng tháng, hiển thị số ngày trước
        if (now.get(Calendar.YEAR) == notificationCalendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == notificationCalendar.get(Calendar.MONTH)) {
            long days = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);
            return days + " ngày trước";
        }

        // Nếu là cùng năm nhưng khác tháng, hiển thị "dd/MM"
        if (now.get(Calendar.YEAR) == notificationCalendar.get(Calendar.YEAR)) {
            SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            return dayMonthFormat.format(notificationCalendar.getTime());
        }

        // Nếu là khác năm, hiển thị "dd/MM/yyyy"
        SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return dayMonthYearFormat.format(notificationCalendar.getTime());
    }

    // Kiểm tra xem có cùng ngày không
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // Kiểm tra xem có cùng tuần không
    private static boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }
}
