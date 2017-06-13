package com.albertoalegria.mendel.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Alberto Alegria
 */

@Entity
@Table(name = "mndl_careers")
public class Career {
    @Id
    @GeneratedValue
    @Column(name = "career_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "career_name")
    private String name;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "career_acronym")
    private String acronym;

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    private List<Group> groups;

    public Career() {}

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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Career)) {
            return false;
        }

        if (!((Career) obj).getName().equals(name)) {
            return false;
        }

        if (!((Career) obj).getAcronym().equals(acronym)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Career[id=" + id + ", name=" + name + ", acronym=" + acronym +"]";
    }
}
