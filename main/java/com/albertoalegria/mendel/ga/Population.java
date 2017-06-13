package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.models.Course;
import com.albertoalegria.mendel.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alberto Alegria
 */
public class Population {
    private int size;

    private Course course;

    private List<Individual> individuals;

    public Population(Course course, int size) {
        this.course = course;
        this.size = size;
        individuals = new ArrayList<>();
        generatePopulation();
    }

    private void generatePopulation() {
        for (int i = 0; i < size; i++) {
            Individual individual = new Individual(course);
            individuals.add(individual);
        }
    }

    public void updateIndividualsFitness() {
        for (Individual individual : individuals) {
            individual.updateFitness();
        }
    }


    public Individual getBest() {
        /*Individual best = individuals.get(0);

        for (Individual individual : individuals) {
            if (individual.getFitness() < best.getFitness()) {
                best = individual;
            }
        }*/

        individuals.sort(Comparator.comparing(Individual::getFitness));

        return individuals.get(0);
    }

    public void printPopulation() {
        individuals.forEach(System.out::println);
    }

    public void sort() {
        individuals.sort(Comparator.comparing(Individual::getFitness));
    }

    public Individual get(int index) {
        return individuals.get(index);
    }

    public void replace(List<Individual> newIndividuals) {
        individuals.clear();
        individuals = newIndividuals;
    }

    public void export(String gen) {
        Individual best = getBest();

        StringBuilder builder = new StringBuilder();

        for (Individual individual : individuals) {
            builder.append(individual.toString()).append("\n");
        }

//        builder.append("Best individual: ").append(best.toString()).append("\n");
        builder.append("Population fitness: ").append(getAverageFitness());

        try {
            Utils.export(builder.toString(), "population/generation" + gen + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return individuals.size();
    }

    public double getAverageFitness() {
        double avg = 0.0;
        for (Individual individual : individuals) {
            avg += individual.getFitness();
        }

        return avg / individuals.size();
    }


    public void truncate(int newSize) {
        individuals.subList(0, newSize);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }
}
