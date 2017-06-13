package com.albertoalegria.mendel.models;

import com.albertoalegria.mendel.enums.Shift;
import com.albertoalegria.mendel.utils.Utils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */

@Entity
@Table(name = "mndl_timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "timetable_id")
    private Long id;

    @Column(name = "timetable_schedule")
    private String schedule;

    @Column(name = "timetable")
    private String preferredSchedule;

    @Transient
    private List<Time> scheduleArray;

    @Transient
    private List<Time> preferredScheduleArray;

    public Timetable() {
        scheduleArray = new ArrayList<>();
        schedule = "";
    }

    //Classroom
    public void buildTimetable() {
        preferredScheduleArray = Utils.TimesUtils.getAllTimes();
        preferredSchedule = Utils.ArrayUtils.timesToString(preferredScheduleArray);

    }

    //Teacher
    public void buildTimetable(int start, int end) {
        preferredScheduleArray = Utils.TimesUtils.getTimesArray(start, end);
        preferredSchedule = Utils.ArrayUtils.timesToString(preferredScheduleArray);
    }

    //Groups
    public void buildTimetable(Shift shift) {
        preferredScheduleArray = Utils.TimesUtils.getAllTimes();
        preferredScheduleArray = preferredScheduleArray.stream().filter(time -> time.getShift().equals(shift))
                .collect(Collectors.toCollection(ArrayList<Time>::new));

        preferredSchedule = Utils.ArrayUtils.timesToString(preferredScheduleArray);
    }

    public void updateStrings() {
        schedule = Utils.ArrayUtils.timesToString(scheduleArray);
        preferredSchedule = Utils.ArrayUtils.timesToString(preferredScheduleArray);
    }

    public void updateArrays() {
        scheduleArray = Utils.ArrayUtils.stringToTimes(schedule);
        preferredScheduleArray = Utils.ArrayUtils.stringToTimes(preferredSchedule);
    }

    public void reset() {
        preferredScheduleArray.addAll(scheduleArray);
        updateStrings();
    }

    public boolean isHourAvailable(Time time) {
        return preferredScheduleArray.contains(time);
    }

    public void addHour(Time time) {
        scheduleArray.add(time);
        preferredScheduleArray.remove(time);

        updateStrings();
    }

    public boolean isEmpty() {
        return schedule.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPreferredSchedule() {
        return preferredSchedule;
    }

    public void setPreferredSchedule(String preferredSchedule) {
        this.preferredSchedule = preferredSchedule;
    }

    public List<Time> getScheduleArray() {
        return scheduleArray;
    }

    public void setScheduleArray(List<Time> scheduleArray) {
        this.scheduleArray = scheduleArray;
    }

    public List<Time> getPreferredScheduleArray() {
        return preferredScheduleArray;
    }

    public void setPreferredScheduleArray(List<Time> preferredScheduleArray) {
        this.preferredScheduleArray = preferredScheduleArray;
    }
}