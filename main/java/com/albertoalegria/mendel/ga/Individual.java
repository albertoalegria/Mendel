package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.enums.Type;
import com.albertoalegria.mendel.exceptions.IndividualNotFeasibleException;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Course;
import com.albertoalegria.mendel.models.Time;
import com.albertoalegria.mendel.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Individual {
    //Course with which the schedule will be generated
    private Course course;

    //The chromosome holds the times and the classrooms
    private Chromosome chromosome;

    //Adaptation level of this individual
    private double fitness;

    private Log log = LogFactory.getLog(Individual.class);

    public Individual() {
        course = new Course();
        chromosome = new Chromosome();
        fitness = 0.0;
    }

    public Individual(Course course) {
        this();
        this.course = course;
        log.debug("Created individual for course " + course.getGroup() + course.getName());
        generateChromosome();
    }

    public Individual(Course course, Chromosome chromosome) {
        this.course = course;
        this.chromosome = chromosome;
        fitness = 0.0;
    }

    public Individual(Individual individual) {
        this.course = individual.getCourse();
        this.chromosome = new Chromosome(individual.getChromosome());
        this.fitness = 0.0;
    }

    private void generateChromosome() {
        log.debug("Generating random chromosome");

        initElements();
        generateTimetable();
    }

    private void initElements() {
        log.debug("Updating models timetables");

        course.getGroup().getTimetable().updateArrays();
        course.getTeacher().getTimetable().updateArrays();

        for (Classroom classroom : course.getClassrooms()) {
            classroom.getTimetable().updateArrays();
        }
    }

    private void generateTimetable() {
        log.debug("Generating possible timetable");

        if (course.needsLab()) {
            getTimetable(course.getLabHours(), Type.LABORATORY);
        }

        getTimetable(course.getClassHours() - course.getLabHours(), Type.CLASSROOM);
    }

    private void getTimetable(int hours, Type type) {
        ArrayList<Classroom> classrooms = course.getClassrooms().stream().filter(classroom -> classroom.getType().equals(type)).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < hours; i++) {
            Classroom classroom = classrooms.get(Utils.RandUtils.getRandomNumber(0, classrooms.size() - 1));
            List<Time> feasibleTimes = getFeasibleTimes(classroom);

            feasibleTimes.removeAll(chromosome.getTimes());

            int count = 0;
            while (feasibleTimes.isEmpty()) {
                if (count == 100) throw new IndividualNotFeasibleException(this, feasibleTimes); //Throws exception if there are no suitable times

                log.warn("Classroom " + classroom.getName() + " run out of times. Searching another one");
                classroom = classrooms.get(Utils.RandUtils.getRandomNumber(0, classrooms.size() - 1));
                feasibleTimes = getFeasibleTimes(classroom);
                feasibleTimes.removeAll(chromosome.getTimes());
                count++;

            }

            chromosome.add(feasibleTimes.get(Utils.RandUtils.getRandomNumber(0, feasibleTimes.size() - 1)), classroom);
        }
    }

    private List<Time> getFeasibleTimes(Classroom classroom) {
        log.debug("Getting feasible times");

        return IndividualUtils.getFeasibleTimes(course.getGroup().getTimetable().getPreferredScheduleArray(),
                course.getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());
    }

    public void mutate(int index) {
        chromosome.mutate(course, index);
    }

    public void updateFitness() {
        log.debug("Updating individual fitness");

        //fitness = FitnessOld.getFitness(this);
        fitness = Fitness.getFitness(this);
    }

    public double getInverseFitness() {
        return fitness == 0 ? 2 : 1.0 / fitness;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Individual{Course=")
                .append(course.getId())
                .append(", Teacher=")
                .append(course.getTeacher().getId())
                .append(", Chromosome=")
                .append(chromosome.toString())
                .append(", FitnessOld=")
                .append(fitness)
                .append("}")
                .toString();
    }

    public static class IndividualUtils {

        public static List<Time> getFeasibleTimes(List<Time> groupTimes, List<Time> teacherTimes, List<Time> classroomTimes) {
            List<Time> allTimes = Utils.TimesUtils.getAllTimes();
            ArrayList<Time> times = new ArrayList<>();

            for (Time time : allTimes) {
                if (groupTimes.contains(time) && teacherTimes.contains(time) && classroomTimes.contains(time)) {
                    times.add(time);
                }
            }
            return times;
        }

        public static boolean isValid(Individual individual) {
            for (Classroom classroom : individual.getChromosome().getClassrooms()) {
                List<Time> feasibleTimes = getFeasibleTimes(individual.getCourse().getGroup().getTimetable().getPreferredScheduleArray(),
                        individual.getCourse().getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());

                if (!feasibleTimes.contains(individual.getChromosome().getTimeForClassroom(classroom))) {
                    return false;
                }
            }
            return true;
        }

        public static List<Classroom> getProblematicClassrooms(Chromosome chromosome) {
            List<Classroom> classrooms = new ArrayList<>();

            for (Classroom classroom : chromosome.getClassrooms()) {
                if (!classroom.getTimetable().getPreferredScheduleArray().contains(chromosome.getTimeForClassroom(classroom))) {
                    classrooms.add(classroom);
                }
            }

            return classrooms;
        }

        public static List<Time> getProblematicTimes(Chromosome chromosome) {
            List<Time> times = new ArrayList<>();
            List<Classroom> classrooms = getProblematicClassrooms(chromosome);

            for (Classroom classroom : classrooms) {
                times.add(chromosome.getTimeForClassroom(classroom));
            }

            return times;
        }

        private static List<Classroom> getAvailableClassrooms(Time time, List<Classroom> classrooms) {
            return classrooms.stream().filter(classroom -> classroom.getTimetable().getPreferredScheduleArray().contains(time)).collect(Collectors.toList());
        }


        public static Individual repair(Individual invalid, boolean timesAreNative) {
            Chromosome chromosome = invalid.getChromosome();
            if (timesAreNative) {

                List<Time> times = getProblematicTimes(chromosome);

                for (Time time : times) {
                    List<Classroom> classrooms = getAvailableClassrooms(time, invalid.getCourse().getClassrooms());
                    if (classrooms.size() < 1) {
                        return null;
                    }
                    Classroom classroom = classrooms.get(Utils.RandUtils.getRandomNumber(0, classrooms.size() - 1));
                    chromosome.changeClassroom(chromosome.getTimeIndex(time), classroom);
                }
            } else {
                List<Classroom> classrooms = getProblematicClassrooms(chromosome);

                for (Classroom classroom : classrooms) {
                    List<Time> times = getFeasibleTimes(invalid.getCourse().getGroup().getTimetable().getPreferredScheduleArray(),
                            invalid.getCourse().getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());

                    times.removeAll(invalid.getChromosome().getTimes());

                    if (times.size() < 1) {
                        return null;
                    }
                    //TODO sometimes crashes here
                    //TODO find another way to manage this error. Maybe an exception?
                    chromosome.changeTime(chromosome.getTimeIndex(chromosome.getTimeForClassroom(classroom)), times.get(Utils.RandUtils.getRandomNumber(0, times.size() - 1)));
                }
            }

            return invalid;
        }

        public static Individual repair2(Individual individual) {
            List<Classroom> problematicClassrooms = getProblematicClassrooms(individual.getChromosome());

            for (Classroom classroom : problematicClassrooms) {
                Time time = individual.getChromosome().getTimeForClassroom(classroom);

                List<Time> times = getFeasibleTimes(individual.getCourse().getGroup().getTimetable().getPreferredScheduleArray(),
                        individual.getCourse().getTeacher().getTimetable().getPreferredScheduleArray(), classroom.getTimetable().getPreferredScheduleArray());

                for (int i = 0; i < individual.getChromosome().getClassroomIndex(classroom); i++) {
                    times.remove(individual.getChromosome().getTimes().get(i));
                }

                individual.getChromosome().changeTime(individual.getChromosome().getClassroomIndex(classroom), times.get(Utils.RandUtils.getRandomNumber(0, times.size() - 1)));
//                times.removeAll(individual.getChromosome().getTimes());
            }

            return individual;
        }
    }
}