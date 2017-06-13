package com.albertoalegria.mendel.exceptions;

import com.albertoalegria.mendel.ga.Individual;
import com.albertoalegria.mendel.models.Time;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Alberto Alegria
 */
public class IndividualNotFeasibleException extends RuntimeException {
    private Log log = LogFactory.getLog(IndividualNotFeasibleException.class);

    public IndividualNotFeasibleException() {
        super("Cannot find suitable solution.");
        log.fatal("Cannot find suitable solution.");
    }

    public IndividualNotFeasibleException(Individual individual, List<Time> times) {
        super("Cannot find suitable solution.");
        log.fatal(getMessage(individual, times));
    }

    private String getMessage(Individual individual, List<Time> times) {
        StringBuilder builder = new StringBuilder();
        builder.append("Cannot find suitable solution for ")
                .append(individual.toString()).append(". ")
                .append('\n');

        if (times.size() > 1) {
            builder.append("Neither group, teacher or available classrooms contains these free times: ");

            for (Time time : times) {
                builder.append(time.getAmPmTimeRange()).append('\n');
            }
        } else {
            builder.append("Neither group, teacher or available classrooms contains free times in common.");
        }

        builder.append(" Genetic algorithm can't continue.\n");
        return builder.toString();
    }
}
