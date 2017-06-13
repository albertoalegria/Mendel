package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Course;
import com.albertoalegria.mendel.models.Time;
import com.albertoalegria.mendel.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alberto Alegria
 */
public class Chromosome {

    //TODO change this for a hashmap?
    private List<Time> times;
    private List<Classroom> classrooms;

    public Chromosome() {
        times = new ArrayList<>();
        classrooms = new ArrayList<>();
    }

    public Chromosome(Chromosome chromosome) {
        times = new ArrayList<>(chromosome.getTimes());
        classrooms = new ArrayList<>(chromosome.getClassrooms());
    }

    public void add(Time time, Classroom classroom) {
        times.add(time);
        classrooms.add(classroom);
    }

    public Time getTime(int index) {
        return times.get(index);
    }

    public Classroom getClassroom(int index) {
        return classrooms.get(index);
    }

    public Time getTimeForClassroom(Classroom classroom) {
        if (classrooms.contains(classroom)) {
            return times.get(classrooms.indexOf(classroom));
        }

        return null;
    }

    public Classroom getClassroomForTime(Time time) {
        if (times.contains(time)) {
            return classrooms.get(times.indexOf(time));
        }

        /*
        * TODO create custom exceptions
        * */

        return null;
    }

    private Time getRandomTime(Course course, int classroomIndex, Time prevTime) {
        List<Time> feasibleTimes = Individual.IndividualUtils.getFeasibleTimes(course.getGroup().getTimetable().getPreferredScheduleArray(),
                course.getTeacher().getTimetable().getPreferredScheduleArray(),
                classrooms.get(classroomIndex).getTimetable().getPreferredScheduleArray());

        //No repeated times
        //feasibleTimes.removeAll(times); //Removed because sometimes crashes with the assigned teacher.
        feasibleTimes.removeAll(times); //This line fits here.

        if (feasibleTimes.size() == 0) { //This means that all the elements run out of times in common. Thus, the current time is returned. This avoids:
            return prevTime;                        //a) NPE as in the previous line [feasibleTimes.removeAll(times)]
        } else {                                    //b) Chromosomes with Overlapped times
            return feasibleTimes.get(Utils.RandUtils.getRandomNumber(0, feasibleTimes.size() - 1));
        }
    }

    private Classroom getRandomClassroom(int timeIndex, Classroom classroom) {
        List<Classroom> possibleClassrooms = new ArrayList<>();

        for (Classroom classroom1 : classrooms) {
            if (classroom1.getTimetable().getPreferredScheduleArray().contains(times.get(timeIndex)) && classroom1.getType().equals(classroom.getType())) {
                possibleClassrooms.add(classroom);
            }
        }

        Classroom c = possibleClassrooms.get(Utils.RandUtils.getRandomNumber(0, possibleClassrooms.size() - 1));

        if (!classroom.getType().equals(c.getType())) {
            return classroom;
        } else {
            return c;
        }
    }

    public void mutate(Course course, int index) {
        Classroom classroom = course.getClassrooms().get(Utils.RandUtils.getRandomNumber(0, course.getClassrooms().size() - 1));
        List<Time> feasibleTimes = Individual.IndividualUtils.getFeasibleTimes(course.getGroup().getTimetable().getPreferredScheduleArray(),
                course.getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());

        feasibleTimes.removeAll(times);

        int cont = 0;
        while (feasibleTimes.size() < 1 && cont < 100) {
            classroom = course.getClassrooms().get(Utils.RandUtils.getRandomNumber(0, course.getClassrooms().size() - 1));
            feasibleTimes = Individual.IndividualUtils.getFeasibleTimes(course.getGroup().getTimetable().getPreferredScheduleArray(),
                    course.getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());

            feasibleTimes.removeAll(times);

            cont++;
        }

        if (feasibleTimes.size() > 0) {
            classrooms.set(index, classroom);
            times.set(index, feasibleTimes.get(Utils.RandUtils.getRandomNumber(0, feasibleTimes.size() - 1)));
            //times.set(index, feasibleTimes.get(Utils.RandUtils.getRandomNumber(0, times.size() - 1))); //TODO sometimes (1 out of 30) this line causes a crash
        } //Else all stays the same
    }

    public void mutateTime(Course course, int index) {
        times.set(index, getRandomTime(course, index, times.get(index)));
    }

    public void mutateClassroom(int index) {
        classrooms.set(index, getRandomClassroom(index, classrooms.get(index)));
    }

    public void changeTime(int index, Time time) {
        times.set(index, time);
    }

    public void changeClassroom(int index, Classroom classroom) {
        classrooms.set(index, classroom);
    }

    public int getLength() {
        return times.size() * 2;
    }

    public int getTimeIndex(Time time) {
        return times.indexOf(time);
    }

    public int getClassroomIndex(Classroom classroom) {
        return classrooms.indexOf(classroom);
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Times[");

        for (Time time : times) {
            builder.append(time.getHour()).append(",");
        }

        builder.setLength(builder.length() - 1);
        builder.append("] Classrooms[");

        for (Classroom classroom : classrooms) {
            builder.append(classroom.getId()).append(",");
        }

        builder.setLength(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }
}
