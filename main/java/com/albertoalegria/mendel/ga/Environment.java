package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.models.Course;

import java.util.ArrayList;

/**
 * @author Alberto Alegria
 */
public class Environment {
    private static ArrayList<Course> finishedCourses = new ArrayList<>();

    public static void add(Course course) {
        finishedCourses.add(course);
    }

    public static ArrayList<Course> getFinishedCourses() {
        return finishedCourses;
    }

    public static int size() {
        return finishedCourses.size();
    }
}
