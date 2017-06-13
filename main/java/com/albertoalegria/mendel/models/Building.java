package com.albertoalegria.mendel.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Alberto Alegria
 */

@Entity
@Table(name = "mndl_buildings")
public class Building {
    @Id
    @GeneratedValue
    @Column(name = "building_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100, message = "{mendel.messages.error.building.name}")
    @Column(name = "building_name")
    private String name;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Classroom> classrooms;


    public Building() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof  Building)) {
            return false;
        }

        if (!((Building) obj).getName().equals(name)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Building[id=" + id + ", name=" + name + "]";
    }
}
