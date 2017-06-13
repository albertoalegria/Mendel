package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Time;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Fitness {
    /*
    * TODO Check day distribution. Optimum days distribution = DD <= CH/2. Where CH = Class Hours and DD = Days Distribution
    * TODO Check classroom distribution. Optimum classroom number = 1 > CD < (CH/2). Where CD = Classroom Distribution
    * TODO Check DD-CD relation. Optimum DD-CD relation = ∑DD != ∑CD.
    * TODO Use penalization weight PW. 0>PW<1;
    *
    * 1.- Times Per Day DONE
    *       -Check chromosome Times per Day Count. If TpDC > 2, penalize.
    *
    * 2.- Adjacent Times DONE
    *       -Check if the Times of the same day are adjacent.
    *
    * 3.- Adjacent Classrooms DONE
    *       -Check if each time assigned in the same has adjacent classrooms.
    *
    * 4.- Days Distribution DONE
    *       -Check how many days are assigned. A class with 4 hours must've assigned 4 times between 2 days. If DC > CH/2, penalize.
    *
    * 5.- Hour Optimization
    *       -Check if the generated Time is between the first Teacher available times'.
    *
    * Weights
    * 1.- 2.0
    * 2.- 1.8
    * 3.- 1.6
    * 4.- 1.4
    * 5.- 1.2
    * */

    private static final double WEIGHT_SC_O  = 0.0D;
    private static final double WEIGHT_SC_1  = 0.1D;
    private static final double WEIGHT_SC_2  = 0.2D;
    private static final double WEIGHT_SC_3  = 0.3D;
    private static final double WEIGHT_SC_4  = 0.4D;
    private static final double WEIGHT_SC_5  = 0.5D;
    private static final double WEIGHT_SC_6  = 0.6D;
    private static final double WEIGHT_SC_7  = 0.7D;
    private static final double WEIGHT_SC_8  = 0.8D;
    private static final double WEIGHT_SC_9  = 0.9D;
    private static final double WEIGHT_SC_10 = 1.0D;
    private static final double WEIGHT_SC_11 = 1.1D;
    private static final double WEIGHT_SC_12 = 1.2D;
    private static final double WEIGHT_SC_13 = 1.3D;
    private static final double WEIGHT_SC_14 = 1.4D;
    private static final double WEIGHT_SC_15 = 1.5D;
    private static final double WEIGHT_SC_16 = 1.6D;
    private static final double WEIGHT_SC_17 = 1.7D;
    private static final double WEIGHT_SC_18 = 1.8D;
    private static final double WEIGHT_SC_19 = 1.9D;
    private static final double WEIGHT_SC_20 = 2.0D;


    private Individual individual;
    private Chromosome chromosome;
    private double fitness;
    private Log log = LogFactory.getLog(Fitness.class);

    private Fitness(Individual individual) {
        this.individual = individual;
        this.chromosome = individual.getChromosome();
        this.fitness = 0.0D;
    }

    private void updateFitness() {
        int acum = 0;
        for (Day day : Day.getWeek()) {


            if (getTimesPerDay(day) > 0) {
                acum++;
            }

            if (getTimesPerDay(day) > 1) {
                timesPerDay(day);
                adjacentTimes(day);
                adjacentClassrooms(day);
            }

            hoursOptimization(day);
        }
        int half = (int)Math.round((double)individual.getCourse().getClassHours()/2.0);
        int rest = acum - half;

        incrementFitness(rest > 0, rest, WEIGHT_SC_14);
    }

    private void timesPerDay(Day day) {
        int timesPerDay = getTimesPerDay(day);

        incrementFitness(timesPerDay > 2, (double) timesPerDay, WEIGHT_SC_20);
    }

    private void adjacentTimes(Day day) {
        List<Time> times = getTimesForDay(day);

        int counter = 0;

        Time prev = times.get(0);

        for (Time time : times) {
            counter += time.getHourDifference(prev);
            prev = time;
        }

        incrementFitness(counter > 1, counter, WEIGHT_SC_18);
    }

    private void adjacentClassrooms(Day day) {
        List<Time> times = getTimesForDay(day);
        List<Classroom> classrooms = new ArrayList<>();

        for (Time time : times) {
            if (!classrooms.contains(chromosome.getClassroomForTime(time))) {
                classrooms.add(chromosome.getClassroomForTime(time));
            }
        }

        incrementFitness(classrooms.size() > 1, classrooms.size(), WEIGHT_SC_16);
    }

    private void hoursOptimization(Day day) {
        List<Time> times = chromosome.getTimes().stream().filter(time -> time.getDayOfWeek().equals(day)).collect(Collectors.toList());
        times.sort(Comparator.comparing(Time::getHourOfDay));

        for (Time time : times) {
            List<Time> feasibleTimes = Individual.IndividualUtils.getFeasibleTimes(individual.getCourse().getGroup().getTimetable().getPreferredScheduleArray(),
                    individual.getCourse().getTeacher().getTimetable().getPreferredScheduleArray(), individual.getChromosome().getClassroomForTime(time).getTimetable().getPreferredScheduleArray());
            feasibleTimes = feasibleTimes.stream().filter(t -> t.getDayOfWeek().equals(day)).collect(Collectors.toList());
            feasibleTimes.sort(Comparator.comparing(Time::getHourOfDay));

            //TODO check this

            //log.info(Utils.ArrayUtils.timesToString(times) + ", " + Utils.ArrayUtils.timesToString(feasibleTimes));

            incrementFitness(times.indexOf(time) != feasibleTimes.indexOf(time), (double) Math.abs(times.indexOf(time) - feasibleTimes.indexOf(time)), WEIGHT_SC_4);
        }
    }

    private List<Time> getTimesForDay(Day day) {
        return chromosome.getTimes().stream().filter(time -> time.getDayOfWeek().equals(day)).collect(Collectors.toList());
    }

    private int getTimesPerDay(Day day) {
        return getTimesForDay(day).size();
    }

    private void incrementFitness(boolean condition, double number, double weight) {
        if (condition) {
            fitness += (number * weight);
        }
    }

    private double getFitness() {
        return fitness;
    }

    public static double getFitness(Individual individual) {
        Fitness fitness = new Fitness(individual);
        fitness.updateFitness();
        return fitness.getFitness();
    }
}