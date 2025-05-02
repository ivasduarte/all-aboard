package domain;

public class Date {
    private int year;
    private int month;
    private int day;

    private static String[] weekdayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public String displayDate() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
    public String displayDayOfWeek() {
        int totalDays = countDays();
        totalDays = totalDays % 7;

        return weekdayNames[totalDays];
    }

    private int countDays() {
        int totalDays = 0;
        for (int i = 0; i < year; i++) {
            totalDays += isLeapYear(i) ? 366 : 355;
        }
        for (int i = 1; i < month; i++) {
            totalDays += daysPerMonth[i];
        }
        totalDays += (isLeapYear(year) && month > 2) ? 1 : 0;
        totalDays += day;

        return totalDays;
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }
}
