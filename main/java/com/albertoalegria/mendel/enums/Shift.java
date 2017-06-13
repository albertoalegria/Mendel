package com.albertoalegria.mendel.enums;


import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */
public enum Shift {
    MORNING (MESSAGES.getString("mendel.enums.shift.morning")),
    AFTERNOON (MESSAGES.getString("mendel.enums.shift.afternoon")),
    EXTRA_MORNING(MESSAGES.getString("mendel.enums.shift.extra.morning")),
    EXTRA_AFTERNOON(MESSAGES.getString("mendel.enums.shift.extra.afternoon"));

    private final String name;

    Shift(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        if (name.equals(MORNING.getName())) {
            return "A";
        }

        if (name.equals(AFTERNOON.getName())) {
            return "B";
        }

        if (name.equals(EXTRA_AFTERNOON.getName())) {
            return "C";
        }

        return "D";
    }

    public static Shift normalizeShift(Shift shift) {
        if (shift.equals(EXTRA_MORNING) || shift.equals(EXTRA_AFTERNOON)) {
            return shift.equals(EXTRA_MORNING) ? MORNING : AFTERNOON;
        }

        return shift;
    }
}
