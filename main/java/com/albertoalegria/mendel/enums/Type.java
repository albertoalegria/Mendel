package com.albertoalegria.mendel.enums;

import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */
public enum Type {

    CLASSROOM (MESSAGES.getString("mendel.enums.type.classroom")),
    LABORATORY (MESSAGES.getString("mendel.enums.type.laboratory"));

    private final String name;
    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
