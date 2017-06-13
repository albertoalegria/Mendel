package com.albertoalegria.mendel.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alberto Alegria
 */

@Entity
@Table(name = "mndl_courses")
public class Course {
    @Id
    @GeneratedValue
    @Column(name = "course_id")
    private Long id;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "course_size")
    private int size;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "course_name")
    private String name;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "course_acronym")
    private String acronym;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "course_key")
    private String key;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "course_class_hours")
    private int classHours;

    @Column(name = "course_lab_hours")
    private int labHours;

    @NotNull
    @ManyToOne
    private Group group;

    @ManyToOne
    private Teacher teacher;

    @ManyToMany
    @JoinTable
    private List<Classroom> classrooms;

    @ElementCollection
    @CollectionTable(name = "times", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "course_times")
    private List<Integer> timesHours;

    @ElementCollection
    @CollectionTable(name = "classrooms", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "course_classrooms")
    private List<Long> classroomsIds;

    public Course() {

    }

    public Course(Course copy) {
        this.size = copy.getSize();
        this.name = copy.getName();
        this.acronym = copy.getAcronym();
        this.key = copy.getKey();
        this.classHours = copy.getClassHours();
        this.labHours = copy.getLabHours();
        this.group = copy.getGroup();
        this.teacher = copy.getTeacher();
        //this.classrooms = copy.getClassrooms();
    }

    public boolean needsLab() {
        return labHours > 0;
    }

    public List<String> getTimetable() {
        List<String> timetable = new ArrayList<>();
        for (int i = 0; i < timesHours.size(); i++) {
            timetable.add(findClassroom(classroomsIds.get(i)).getName() + ", " +
                    new Time(timesHours.get(i)).getDayOfWeek().getName() + " - " + new Time(timesHours.get(i)).getAmPmTimeRange());
        }
        return timetable;
    }

    public Classroom findClassroom(long id) {
        for (Classroom classroom : classrooms) {
            if (classroom.getId().equals(id)) {
                return classroom;
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getClassHours() {
        return classHours;
    }

    public void setClassHours(int classHours) {
        this.classHours = classHours;
    }

    public int getLabHours() {
        return labHours;
    }

    public void setLabHours(int labHours) {
        this.labHours = labHours;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<Integer> getTimesHours() {
        return timesHours;
    }

    public void setTimesHours(List<Integer> timesHours) {
        this.timesHours = timesHours;
    }

    public List<Long> getClassroomsIds() {
        return classroomsIds;
    }

    public void setClassroomsIds(List<Long> classroomsIds) {
        this.classroomsIds = classroomsIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Course)) {
            return false;
        }

        if (((Course) obj).getSize() != size) {
            return false;
        }

        if (!((Course) obj).getName().equals(name)) {
            return false;
        }

        if (!((Course) obj).getAcronym().equals(acronym)) {
            return false;
        }

        if (((Course) obj).getClassHours() != classHours) {
            return false;
        }

        if (((Course) obj).getLabHours() != labHours) {
            return false;
        }

        if (!((Course) obj).getGroup().equals(group)) {
            return false;
        }

        return true;
    }



    @Override
    public String toString() {
        return "Course[id=" + id + ", size=" + size + ", name=" + name + ", acronym=" + acronym + ", classHours=" +
                classHours + ", labHours=" + labHours + ", group=" + group.getName() + ", teacher=" + teacher + "]";
    }
}
