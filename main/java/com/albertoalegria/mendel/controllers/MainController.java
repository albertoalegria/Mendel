package com.albertoalegria.mendel.controllers;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.ga.*;
import com.albertoalegria.mendel.models.*;
import com.albertoalegria.mendel.repositories.ClassroomRepository;
import com.albertoalegria.mendel.repositories.CourseRepository;
import com.albertoalegria.mendel.repositories.GroupRepository;
import com.albertoalegria.mendel.repositories.TeacherRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */

@Controller
@RequestMapping(value = {"", "/"})
public class MainController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Log log = LogFactory.getLog(MainController.class);

    Individual current;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        boolean ok = courseRepository.count() > 0 && teacherRepository.count() > 0 && classroomRepository.count() > 0;

        model.addAttribute("ok", ok);
        model.addAttribute("groups", groupRepository.findAll());

        return "index";
    }


    @RequestMapping(value = "generate")
    public String generate(@RequestParam("id") long id) {
        Course course = courseRepository.findOne(id);

        //                                                                                       10%
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(course, 0.04, 0.1, 100, 200);
        geneticAlgorithm.run();

        current = geneticAlgorithm.getBest();

        //attr.addFlashAttribute("id", id);

        return "redirect:/show?id=" + id;
        //return "redirect:/show";
    }

    @RequestMapping(value = "show")
    public String show(@RequestParam("id") long id, Model model, HttpServletRequest request) {
        Course course = courseRepository.findOne(id);


        model.addAttribute("title", course.getName() + " Timetable");
        model.addAttribute("days", Day.getWeek());
        model.addAttribute("id", id);

        if (current != null) {
            Decrypter decrypter = new Decrypter(current);
            model.addAttribute("isPreview", true);
            model.addAttribute("fitness", current.getFitness());
            model.addAttribute("timetable", decrypter.getDecryptedIndividual());
        } else {
            model.addAttribute("isPreview", false);
            model.addAttribute("timetable", course.getTimetable());
        }

        return "show";
    }

    @RequestMapping(value = "save")
    public String save(@RequestParam("id") long id) {
        Course course = courseRepository.findOne(id);

        Group group = groupRepository.findOne(course.getGroup().getId());
        Teacher teacher = teacherRepository.findOne(course.getTeacher().getId());

        group.getTimetable().updateArrays();
        teacher.getTimetable().updateArrays();

        Chromosome chromosome = current.getChromosome();

        List<Time> times = chromosome.getTimes();

        HashMap<Time, Classroom> keys = new HashMap<>();

        for (Time time : times) {
            keys.put(time, chromosome.getClassroomForTime(time));
        }

        times.sort(Comparator.comparing(Time::getHour));

        for (Time time : times) {
            chromosome.changeClassroom(chromosome.getTimeIndex(time), keys.get(time));
        }

        course.setTimesHours(times.stream().map(Time::getHour).collect(Collectors.toList()));
        course.setClassroomsIds(chromosome.getClassrooms().stream().map(Classroom::getId).collect(Collectors.toList()));

        for (Time time : chromosome.getTimes()) {
            Classroom classroom = classroomRepository.findOne(chromosome.getClassroomForTime(time).getId());
            classroom.getTimetable().updateArrays();

            group.getTimetable().addHour(time);
            teacher.getTimetable().addHour(time);
            classroom.getTimetable().addHour(time);

            classroomRepository.save(classroom);
        }

        courseRepository.save(course);
        groupRepository.save(group);
        teacherRepository.save(teacher);

        current = null;
        return "redirect:";
    }

    @RequestMapping("resetall")
    public String resetAll() {

        for (Group group : groupRepository.findAll()) {
            Timetable timetable = new Timetable();
            timetable.buildTimetable(group.getShift());
            timetable.updateStrings();
            group.setTimetable(timetable);

            groupRepository.save(group);
        }

        for (Teacher teacher : teacherRepository.findAll()) {
            Timetable timetable = new Timetable();
            timetable.buildTimetable(teacher.getCheckIn(), teacher.getCheckOut());
            timetable.updateStrings();
            teacher.setTimetable(timetable);

            teacherRepository.save(teacher);
        }

        for (Classroom classroom : classroomRepository.findAll()) {
            Timetable timetable = new Timetable();
            timetable.buildTimetable();
            timetable.updateStrings();
            classroom.setTimetable(timetable);

            classroomRepository.save(classroom);
        }

        for (Course course : courseRepository.findAll()) {
            course.getTimesHours().clear();
            course.getClassroomsIds().clear();
            courseRepository.save(course);
        }

        return "redirect:";
    }

}
