package com.albertoalegria.mendel.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Alberto Alegria
 */
@Entity
@Table(name = "mndl_teachers")
public class Teacher {
    @Id
    @GeneratedValue
    @Column(name = "teacher_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "teacher_first_name")
    private String firstName = "";

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "teacher_last_name")
    private String lastName;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "teacher_hours")
    private int hours;

    @NotNull
    @Column(name = "teacher_check_in")
    private int checkIn;

    @NotNull
    @Column(name = "teacher_check_out")
    private int checkOut;

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Timetable timetable;

    public Teacher() {}

    public String getFormattedCheckIn() {
        return new Time(checkIn).getAmPmTime();
    }

    public String getFormattedCheckOut() {
        return new Time(checkOut).getAmPmTime();
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public boolean isNull() {
        return id == null;
    }

    public int getTotalHours() {
        return checkOut - checkIn;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(int checkIn) {
        this.checkIn = checkIn;
    }

    public int getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(int checkOut) {
        this.checkOut = checkOut;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Teacher)) {
            return false;
        }

        if (!((Teacher) obj).getFirstName().equals(firstName)) {
            return false;
        }

        if (!((Teacher) obj).getLastName().equals(lastName)) {
            return false;
        }

        if (((Teacher) obj).getHours() != hours) {
            return false;
        }

        if (((Teacher) obj).getCheckIn() != checkIn) {
            return false;
        }

        if (((Teacher) obj).getCheckOut() != checkOut) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Teacher[id=" + id + ", name=" + getName() + ", hours=" + hours + ", checkIn=" + checkIn
                + ", checkOut=" + checkOut + "]";
    }
}
