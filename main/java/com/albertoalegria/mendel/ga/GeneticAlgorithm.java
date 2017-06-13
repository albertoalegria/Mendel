package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Course;
import com.albertoalegria.mendel.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author Alberto Alegria
 */
public class GeneticAlgorithm {
    private Population population;
    private int populationSize;
    private int numberOfGenerations;
    private double mutationRate;
//    private double crossoverRate;
    private double elitism;
    private Individual best;

    private Log log = LogFactory.getLog(GeneticAlgorithm.class);

    public GeneticAlgorithm(Course course, double mutationRate, double elitism ,int populationSize, int numberOfGenerations) {
        this.populationSize = populationSize;
        this.numberOfGenerations = numberOfGenerations;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
  //      this.crossoverRate = crossoverRate;

        best = new Individual();

        population = new Population(course, populationSize);
    }

    int t;

    public void run() {

        t = 0;

        while (t < numberOfGenerations) {
            log.info("Generation number " + t);
            population.updateIndividualsFitness();
            //population.export(t + "_afirst");
            selection();
            population.updateIndividualsFitness();
            //population.export(t + "_bselected");
            mutation();
            population.updateIndividualsFitness();
            //population.export(t + "_dfinal");
            //System.out.println('\n');
            t++;
        }
        best = population.getBest();

        Decrypter decrypter = new Decrypter(best);
        decrypter.getDecryptedIndividual();
    }

    //selection and crossover
    private void selection() {
        log.info("Selecting fittest individuals");

        List<Individual> newIndividuals = new ArrayList<>();
        //List<Individual> matingPool = new ArrayList<>();

        population.sort();

        int elitismOffset = (int)(populationSize * elitism);

        for (int i = 0; i < elitismOffset; i++) {
            newIndividuals.add(new Individual(population.get(i)));
        }

        //System.out.println("Fittest");
        //newIndividuals.forEach(System.out::println);

        StringBuilder builder = new StringBuilder();

        while (newIndividuals.size() < populationSize) {
            Individual p1 = getBest(5, population.getIndividuals());

            Individual p2 = getBest(5, population.getIndividuals());

            builder.append("Father1: ").append(p1.toString()).append('\n');
            builder.append("Father2: ").append(p2.toString()).append('\n');

            Individual[] descendants = crossover(p1, p2);

            descendants[0].updateFitness();
            descendants[1].updateFitness();

            builder.append("Child1: ").append(descendants[0].toString()).append(", Is valid? ").append(Individual.IndividualUtils.isValid(descendants[0])).append('\n');
            builder.append("Child1: ").append(descendants[1].toString()).append(", Is valid? ").append(Individual.IndividualUtils.isValid(descendants[1])).append('\n');

            if (descendants[0].getFitness() <= p1.getFitness()) {
                newIndividuals.add(new Individual(descendants[0]));
                builder.append("Added Child1 to new individuals\n");
            } else {
                newIndividuals.add(new Individual(p1));
                builder.append("Added Father1 to new individuals\n");
            }

            if (descendants[1].getFitness() <= p2.getFitness()) {
                newIndividuals.add(new Individual(descendants[1]));
                builder.append("Added Child2 to new individuals\n");
            } else {
                newIndividuals.add(new Individual(p2));
                builder.append("Added Father2 to new individuals\n");
            }

            //newIndividuals.add(descendants[0].getFitness() < p1.getFitness() ? descendants[0] : p1);
            //newIndividuals.add(descendants[1].getFitness() < p1.getFitness() ? descendants[1] : p2);
        }

  /*      try {
            Utils.export(builder.toString(), "crossovers/gen_" + t + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        if (newIndividuals.size() > populationSize) {
            newIndividuals = newIndividuals.subList(0, populationSize);
        }

        population.replace(newIndividuals);
    }

    private Individual getBest(int size, List<Individual> matingPool) {
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            individuals.add(getRandomIndividual(matingPool));
        }

        individuals.sort(Comparator.comparing(Individual::getFitness));

        return individuals.get(0);
    }

    private Individual getRandomIndividual(List<Individual> pool) {
        return pool.get(Utils.RandUtils.getRandomNumber(0, pool.size() - 1));
    }

    private void mutation() {
        log.info("Mutating individual");
        int elitismOffset = (int)(populationSize * elitism);
        for (int i = elitismOffset; i < populationSize; i++) {
            for (int j = 0; j < population.get(i).getCourse().getClassHours(); j ++) {
                int m = Utils.RandUtils.getRandomNumber(0, 100);
                int mr = (int)(mutationRate * 100);
                if (mr > m) {
                    population.get(i).mutate(j);
                }
            }
        }
    }

    public Individual getBest() {
        return best;
    }

    private Individual[] crossover(Individual parent1, Individual parent2) {
        //log.info("Crossing individuals");

        Chromosome offspring1 = parent1.getChromosome();
        Chromosome offspring2 = parent2.getChromosome();

        List<Classroom> classrooms1 = offspring1.getClassrooms();

        offspring1.setClassrooms(offspring2.getClassrooms());
        offspring2.setClassrooms(classrooms1);

        Individual child1 = new Individual(parent1.getCourse(), offspring1);
        Individual child2 = new Individual(parent2.getCourse(), offspring2);



        if (!Individual.IndividualUtils.isValid(child1)) {
            log.info("Repairing Offspring1 " + child1.toString());
            //log.info(child1.toString());
            //Preserve times
            child1 = Individual.IndividualUtils.repair(child1, true);
        }

        if (!Individual.IndividualUtils.isValid(child2)) {
            log.info("Repairing Offspring2 " + child2.toString());
            //log.info(child2.toString());
            //Preserve classrooms
            child2 = Individual.IndividualUtils.repair(child2, false);
        }

        if (null == child1) {
            log.warn("Cannot repair Offspring1. Parent1 will replace him in the new population");
            child1 = parent1;
        }

        if (null == child2) {
            log.warn("Cannot repair Offspring2. Parent2 will replaces him in the new population");
            child2 = parent2;
        }

        return new Individual[] {child1, child2};
    }
}
