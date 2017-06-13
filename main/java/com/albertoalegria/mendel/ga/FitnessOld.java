package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */

public class FitnessOld {

    /*
    *     TODO create a kind of Environment awareness to the individual.
    *     TODO check teacher check in and check out, and penalize if the selected time is so far from those except if there is no another available time
    *     TODO weight the penalizations
    * */

    private Individual individual;
    private double fitnessValue;
    private Chromosome chromosome;

    private FitnessOld(Individual individual) {
        this.individual = individual;
        this.chromosome = individual.getChromosome();
        fitnessValue = 0.0;
    }

    /*
    private void checkEnvironment() {
        ArrayList<Course> groupCourses = Environment.getFinishedCourses().stream().filter(course ->
                course.getGroup().equals(individual.getCourse().getGroup())).collect(Collectors.toCollection(ArrayList::new));

        if (groupCourses.size() > 0) {
            for (Course course : groupCourses) {

            }
        }
    }*/

    private void updateFitness() {
        int dayAcum = 0;
        for (Day day : Day.getWeek()) {
            int dayCount = getDayCount(day);
            incrementFitness(dayCount > 2);

            if (dayCount > 0) {
                dayAcum++;
            }

            if (dayCount > 1) {
                checkAdjacentTimes(day);
                checkAdjacentClassrooms(day);
            }
        }

        checkClassroomNumber();
        checkClassroomDayDistribution();

        int half = individual.getCourse().getClassHours() < 5 ? 2 : 3; //4 = 2, 5 = 3, 6 = 3
        int rest = dayAcum - half;

        incrementFitness(rest > 0, rest);

    }

    private void checkClassroomDayDistribution() {
        List<Classroom> classrooms = new ArrayList<>();
        for (Classroom classroom : individual.getChromosome().getClassrooms()) {
            if (!classrooms.contains(classroom)) {
                classrooms.add(classroom);
            }
        }

        List<Day> days = new ArrayList<>();
        for (Time time : individual.getChromosome().getTimes()) {
            if (!days.contains(time.getDayOfWeek())) {
                days.add(time.getDayOfWeek());
            }
        }

        incrementFitness(classrooms.size() != days.size(), Math.abs(classrooms.size() - days.size()));
    }

    private void checkClassroomNumber() {
        List<Classroom> classrooms = new ArrayList<>();
        for (Classroom classroom : individual.getChromosome().getClassrooms()) {
            if (!classrooms.contains(classroom)) {
                classrooms.add(classroom);
            }
        }
        incrementFitness(classrooms.size() > (individual.getCourse().getClassHours() / 2), classrooms.size());
    }

    private void checkAdjacentTimes(Day day) {
        ArrayList<Time> times = chromosome.getTimes().stream().filter(time -> time.getDayOfWeek()
                .equals(day)).collect(Collectors.toCollection(ArrayList<Time>::new));

        int prev = 0;
        int rest = 0;

        for (Time time : times) {
            rest = time.getHourDifference(prev);
            prev = time.getHour();
        }

        //System.out.println("HourDif " + rest);

        incrementFitness(rest > 1, rest);
    }

    private void checkAdjacentClassrooms(Day day) {
        ArrayList<Time> times = chromosome.getTimes().stream().filter(time -> time.getDayOfWeek()
                .equals(day)).collect(Collectors.toCollection(ArrayList<Time>::new));

        ArrayList<Classroom> classrooms = new ArrayList<>();
        for (Time time : times) {
            if (!classrooms.contains(chromosome.getClassroomForTime(time))) {
                classrooms.add(chromosome.getClassroomForTime(time));
            }
        }

        incrementFitness(classrooms.size() > 1, classrooms.size());
    }
//TODO fix fitness
    private void incrementFitness(boolean condition, int n) {
        if (condition) {
            fitnessValue += n;
        }
    }

    private void incrementFitness(boolean condition) {
        if (condition) {
            fitnessValue++;
        }
    }

    private int getDayCount(Day day) {
        return (int) individual.getChromosome().getTimes().stream().filter(time -> time.getDayOfWeek().equals(day)).count();
    }

    private Individual getIndividual() {
        return individual;
    }

    private void setIndividual(Individual individual) {
        this.individual = individual;
    }

    private double getFitnessValue() {
        return fitnessValue;
    }

    private void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    private void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public static double getFitness(Individual individual) {
        FitnessOld fitness = new FitnessOld(individual);
        fitness.updateFitness();

        return fitness.fitnessValue;
    }

    //TODO sort classrooms as well
}