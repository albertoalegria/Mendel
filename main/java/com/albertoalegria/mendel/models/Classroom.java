package com.albertoalegria.mendel.models;

import com.albertoalegria.mendel.enums.Type;

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
@Table(name = "mndl_classrooms")
public class Classroom {

    @Id
    @GeneratedValue
    @Column(name = "classroom_id")
    private Long id;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "classroom_size")
    private int size;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "classroom_identifier")
    private String identifier;

    @NotNull
    @Column(name = "classroom_type")
    private Type type;

    @NotNull
    @ManyToOne
    private Building building;

    @ManyToMany(mappedBy = "classrooms")
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Timetable timetable;

    public Classroom() {}

    public String getName() {
        return building.getName() + "-" + identifier;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
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

        if (!(obj instanceof Classroom)) {
            return false;
        }

        if (((Classroom) obj).getSize() != size) {
            return false;
        }

        if (!((Classroom) obj).getIdentifier().equals(identifier)) {
            return false;
        }

        if (!((Classroom) obj).getType().equals(type)) {
            return false;
        }

        if (!((Classroom) obj).getBuilding().equals(building)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Classroom[id= " + id + ", size=" + size + ", identifier=" + identifier + ", type=" + type + ", building="
                + building.getName() + "]";
    }
}
