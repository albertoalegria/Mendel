package com.albertoalegria.mendel.models;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.enums.Shift;
import com.albertoalegria.mendel.exceptions.HourOutOfBoundsException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.albertoalegria.mendel.utils.Global.*;

/**
 * @author Alberto Alegria
 */
public class Time {
    private int id;
    private int hour;
    private int hourOfDay;
    private int fancyHourOfDay;
    private Day dayOfWeek;

    public Time(int hour) {
        if (hour <= 0 || hour > TOTAL_HOURS) {
            throw new HourOutOfBoundsException(hour, TOTAL_HOURS);
        }
        this.hour = hour;
        id = hour - 1;
        hourOfDay = hour % HOURS_PER_DAY;

        if (hourOfDay == 0) {
            hourOfDay = HOURS_PER_DAY;
        }

        fancyHourOfDay = (FIRST_HOUR - 1) + hourOfDay;

        dayOfWeek = Day.getDay(((hour - 1) / 14) + 1);
    }

    public String getFormattedTime() {
        return dayOfWeek.toString() + " " + fancyHourOfDay + ":00 - " + (fancyHourOfDay + 1) + ":00";
    }

    public String get24hTimeRange() {
        return get24hTime(fancyHourOfDay) + "-" + get24hTime(fancyHourOfDay + 1);
    }

    public String getAmPmTimeRange() {
        return getAmPmTime(fancyHourOfDay) + "-" + getAmPmTime(fancyHourOfDay + 1);
    }

    public String get24hTime() {
        return get24hTime(fancyHourOfDay);
    }

    public String getAmPmTime() {
        return getAmPmTime(fancyHourOfDay);
    }

    private Date getCalendarHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);

        return calendar.getTime();
    }

    public String get24hTime(int hour) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        return dateFormat.format(getCalendarHour(hour));
    }

    public String getAmPmTime(int hour) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        return dateFormat.format(getCalendarHour(hour));
    }

    public int getFormattedHour(int aHour) {
        return aHour == 12 ? 12 : hourOfDay % 12;
    }

    public String getMeridian(int aHour) {
        return aHour < 12 ? "AM" : "PM";
    }

    public int getHourDifference(Time time) {
        return Math.abs(this.hour - time.getHour());
    }

    public int getHourDifference(int hour) {
        return Math.abs(this.hour - hour);
    }

    public boolean isInSameDay(Time time) {
        return this.dayOfWeek.equals(time.dayOfWeek);
    }

    public Shift getShift() {
        return hourOfDay <= SHIFT_HOUR ? Shift.MORNING : Shift.AFTERNOON;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getFancyHourOfDay() {
        return fancyHourOfDay;
    }

    public Day getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof Time)) {
            return false;
        }

        if (((Time) object).id != this.id) {
            return false;
        }

        if (((Time) object).hour != this.hour) {
            return false;
        }

        if (((Time) object).hourOfDay != this.hourOfDay) {
            return false;
        }

        if (!((Time) object).dayOfWeek.equals(this.dayOfWeek)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Time{" +
                "id=" + id +
                ", hour=" + hour +
                ", hourOfDay=" + hourOfDay +
                ", fancyHourOfDay=" + fancyHourOfDay +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
