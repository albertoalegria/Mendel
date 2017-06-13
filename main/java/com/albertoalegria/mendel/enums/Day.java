package com.albertoalegria.mendel.enums;

import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */
public enum Day {
    MONDAY (MESSAGES.getString("mendel.enums.day.monday")),
    TUESDAY (MESSAGES.getString("mendel.enums.day.tuesday")),
    WEDNESDAY (MESSAGES.getString("mendel.enums.day.wednesday")),
    THURSDAY (MESSAGES.getString("mendel.enums.day.thursday")),
    FRIDAY (MESSAGES.getString("mendel.enums.day.friday")),
    SATURDAY (MESSAGES.getString("mendel.enums.day.saturday")),
    SUNDAY (MESSAGES.getString("mendel.enums.day.sunday"));

    private final String name;
    Day(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Day[] getWeek() {
        return new Day[] {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY};
    }

    public static Day getDay(int id) {
        switch (id) {
            case 1:
                return MONDAY;
            case 2:
                return TUESDAY;
            case 3:
                return WEDNESDAY;
            case 4:
                return THURSDAY;
            case 5:
                return FRIDAY;
            case 6:
                return SATURDAY;
        }
        return SUNDAY;
    }
}
