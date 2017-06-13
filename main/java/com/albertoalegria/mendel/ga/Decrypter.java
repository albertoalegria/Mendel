package com.albertoalegria.mendel.ga;

import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Time;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alberto Alegria
 */
public class Decrypter {
    private final Log log = LogFactory.getLog(Decrypter.class);

    private Individual individual;
    private List<Classroom> classrooms;

    public Decrypter(Individual individual) {
        this.individual = individual;
    }

    public List<String> getDecryptedIndividual() {
        log.info("Decrypting " + individual.toString());

        Chromosome chromosome = individual.getChromosome();
        List<Time> times = chromosome.getTimes();
        classrooms = chromosome.getClassrooms();

        HashMap<Time, Classroom> keys = new HashMap<>();

        for (Time time : times) {
            keys.put(time, chromosome.getClassroomForTime(time));
        }

        times.sort(Comparator.comparing(Time::getHour));

        for (Time time : times) {
            chromosome.changeClassroom(chromosome.getTimeIndex(time), keys.get(time));
        }

        List<String> timetable = new ArrayList<>();

        for (Time time : times) {
            //System.out.println(chromosome.getClassroomForTime(time).getName() + ", " + time.getDayOfWeek() + "-" + time.getAmPmTimeRange() );
            log.info(chromosome.getClassroomForTime(time).getName() + ", " + time.getDayOfWeek() + " - " + time.getAmPmTimeRange());
            timetable.add(chromosome.getClassroomForTime(time).getName() + ", " + time.getDayOfWeek().getName() + " - " + time.getAmPmTimeRange());
        }
        return timetable;
    }


}
