package com.albertoalegria.mendel.controllers.teachers;

import com.albertoalegria.mendel.models.*;
import com.albertoalegria.mendel.repositories.ClassroomRepository;
import com.albertoalegria.mendel.repositories.CourseRepository;
import com.albertoalegria.mendel.repositories.GroupRepository;
import com.albertoalegria.mendel.repositories.TeacherRepository;
import com.albertoalegria.mendel.utils.Utils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */
@Controller
@RequestMapping(value = "/teachers")
public class TeachersController {
    Log log = LogFactory.getLog(TeachersController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    //
    @Autowired
    private ClassroomRepository classroomRepository;
/*
    @Autowired
    private Timetable timetable;*/

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all teachers");

        if (teacherRepository.count() > 0) {
            model.addAttribute("teachers", teacherRepository.findAll());
        }

        return "teachers/index";
    }

    @GetMapping(value = "/create")
    public String createTeacher(Model model) {
        log.info("Creating teacher");

        if (courseRepository.count() > 0) {
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("editing", false);
            model.addAttribute(new Teacher());
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("checkInHours", Utils.TimesUtils.getTimesRange(1, 13));
            model.addAttribute("checkOutHours", Utils.TimesUtils.getTimesRange(2, 14));
        }
        return "teachers/create";
    }

    @PostMapping(value = "/create")
    public String createTeacher(@Valid @ModelAttribute Teacher teacher, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating teacher. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("editing", false);
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("checkInHours", Utils.TimesUtils.getTimesRange(1, 13));
            model.addAttribute("checkOutHours", Utils.TimesUtils.getTimesRange(2, 14));

            return "teachers/create";
        }

        teacher.setFirstName(WordUtils.capitalize(teacher.getFirstName().toLowerCase()));
        teacher.setLastName(WordUtils.capitalize(teacher.getLastName().toLowerCase()));

        for (Course course : teacher.getCourses()) {
            log.info("Setting teacher " + teacher.getFirstName() + " to course " + course.getName());

            course.setTeacher(teacher);
        }

        Timetable timetable = new Timetable();
        timetable.buildTimetable(teacher.getCheckIn(), teacher.getCheckOut());
        teacher.setTimetable(timetable);

        log.info("Saving teacher with name " + teacher.getName());

        teacherRepository.save(teacher);
        return "redirect:";
    }

    @RequestMapping(value = "/show")
    public String showTeacher(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing teacher with id " + id);

        model.addAttribute("teacher", teacherRepository.findOne(id));
        return "teachers/show";
    }

    private List<Course> teacherCourses;

    @GetMapping(value = "/edit")
    public String editTeacher(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing teacher with id " + id);

        /*
        * TODO preserve old times after editing
        * */

        teacherCourses = teacherRepository.findOne(id).getCourses();

        model.addAttribute("editing", true);
        model.addAttribute("teacher", teacherRepository.findOne(id));
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
        model.addAttribute("checkInHours", Utils.TimesUtils.getTimesRange(1, 13));
        model.addAttribute("checkOutHours", Utils.TimesUtils.getTimesRange(2, 14));

        return "teachers/edit";
    }

    @PostMapping(value = "/edit")
    public String editTeacher(@Valid @ModelAttribute Teacher teacher, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating teacher. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            /*
            * TODO Try saving the courses in the DB as well
            * */

            model.addAttribute("editing", true);
            model.addAttribute("groups", groupRepository.findAll());
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            model.addAttribute("checkInHours", Utils.TimesUtils.getTimesRange(1, 13));
            model.addAttribute("checkOutHours", Utils.TimesUtils.getTimesRange(2, 14));
            
            return "teachers/edit";
        }

        for (Course course : teacherCourses) {
            course.setTeacher(null);
            courseRepository.save(course);
        }

        for (Course course : teacher.getCourses()) {
            course.setTeacher(teacher);
        }

        teacher.setFirstName(WordUtils.capitalize(teacher.getFirstName().toLowerCase()));
        teacher.setLastName(WordUtils.capitalize(teacher.getLastName().toLowerCase()));

        Timetable timetable = new Timetable();
        timetable.buildTimetable(teacher.getCheckIn(), teacher.getCheckOut());
        teacher.setTimetable(timetable);

        teacherRepository.save(teacher);

        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteTeacher(@RequestParam(name = "id") long id) {

        log.info("Deleting teacher with id " + id);

        for (Course course : teacherRepository.findOne(id).getCourses()) {
            course.setTeacher(null);
        }

        teacherRepository.delete(id);
        return "redirect:";
    }

    /*@Autowired
    Timetable timetable;

    @RequestMapping(value = "/tt")
    public String outside(@RequestParam(name = "id")long id) {
        Timetable timetable = new Timetable();
        Teacher teacher = teacherRepository.findOne(id);
        timetable.setId(teacher.getId());
        timetable.setTt("meh");
        teacher.setTimetableTable(timetable);
        teacherRepository.save(teacher);
        return "redirect:";
    }*/
}