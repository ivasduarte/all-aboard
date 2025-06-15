package domain;

/**
 * Represents a date with year, month, and day components.
 * This class provides functionality for date manipulation, validation,
 * and conversion between different date formats.
 */
public class Date {
    /**
     * The year of the date.
     */
    private int year;

    /**
     * The month of the date.
     */
    private int month;

    /**
     * The day of the date.
     */
    private int day;

    /**
     * The names of the days of the week.
     */
    private static String[] weekdayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    /**
     * The number of days each month of the year as.
     */
    private static int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * The name of the months of the year.
     */
    private static String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    /**
     * Constructs an instance of Date, receiving the year, the month and the day.
     *
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Returns the year of the date.
     *
     * @return year of the date
     */
    public int getYear() {
        return year;
    }

    /**
     * returns the month of the date.
     *
     * @return month of the date
     */
    public int getMonth() {
        return month;
    }

    /**
     * returns the day of the date.
     *
     * @return day of the date
     */
    public int getDay() {
        return day;
    }

    /**
     * Modifies the year of the date.
     *
     * @param year the new year of the date
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Modifies the month of the date.
     *
     * @param month the new month of the date
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Modifies the day of the date.
     *
     * @param day the new day of the date
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * This method returns the date on the following format: %02d/%02d/%04d.
     *
     * @return date characteristics
     */
    public String displayDate() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    /**
     * This method returns the day of the week of the date.
     *
     * @return day of the week
     */
    public String displayDayOfWeek() {
        int totalDays = countDays();
        totalDays = totalDays % 7;

        return weekdayNames[totalDays];
    }

    /**
     * This method returns the number of days from the day 1/1/1 until the current date.
     *
     * @return number of days
     */
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

    /**
     * This method verifies if a specific year is a leap year.
     *
     * @param year the year to verify
     * @return True if the year is a leap year
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }
}
