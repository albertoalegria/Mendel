package com.albertoalegria.mendel.models;

import com.albertoalegria.mendel.enums.Shift;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */

@Entity
@Table(name = "mndl_groups")
public class Group {
    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    @NotNull
    @Min(1)
    @Max(20)
    @Column(name = "group_semester")
    private int semester;

    @NotNull
    @Column(name = "group_shift")
    private Shift shift;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Career career;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Timetable timetable;

    public Group() {}


    public String getName() {
        return career.getAcronym() + semester + shift.getSimpleName();
    }

    public List<Course> freeCourses(Teacher teacher) {
        return courses.stream().filter(course -> course.getTeacher() == null || course.getTeacher().equals(teacher)).collect(Collectors.toList());
    }

    public List<Course> freeCourses() {
        return courses.stream().filter(course -> course.getTeacher() == null).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
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

        if (!(obj instanceof Group)) {
            return false;
        }

        if (((Group) obj).getSemester() != semester) {
            return false;
        }

        if (!((Group) obj).getShift().equals(shift)) {
            return false;
        }

        if (!((Group) obj).getCareer().equals(career)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Group[id=" + id + ", semester=" + semester + ", shift=" + shift + ", career=" + career.getName() + "]";
    }
}
