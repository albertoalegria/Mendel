package com.albertoalegria.mendel.controllers.courses;

import com.albertoalegria.mendel.enums.Type;
import com.albertoalegria.mendel.models.Career;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Course;
import com.albertoalegria.mendel.models.Group;
import com.albertoalegria.mendel.repositories.CareerRepository;
import com.albertoalegria.mendel.repositories.ClassroomRepository;
import com.albertoalegria.mendel.repositories.CourseRepository;
import com.albertoalegria.mendel.repositories.GroupRepository;
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

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */

@Controller
@RequestMapping(value = "/courses")
public class CoursesController {
    private Log log = LogFactory.getLog(CoursesController.class);

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all courses");

        if (courseRepository.count() > 0) {
            model.addAttribute("courses", courseRepository.findAll());
            model.addAttribute("groups", groupRepository.findAll());
        }
        return "courses/index";
    }

    @GetMapping(value = "/create")
    public String createCourse(Model model) {
        log.info("Creating course");

        if (groupRepository.count() > 0) {
            model.addAttribute("groups", groupRepository.findAll());
            if (classroomRepository.count() > 0) {
                ArrayList<Classroom> allClassrooms = (ArrayList<Classroom>) classroomRepository.findAll();
                ArrayList<Classroom> normalClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
                ArrayList<Classroom> labClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.LABORATORY).collect(Collectors.toCollection(ArrayList<Classroom>::new));

                model.addAttribute("normalClassrooms", normalClassrooms);
                model.addAttribute("labClassrooms", labClassrooms);
                model.addAttribute(new Course());
                model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
                model.addAttribute("careers", careerRepository.findAll());
            }
        }

        return "courses/create";
    }

    @PostMapping(value = "/create")
    public String createCourse(@Valid @ModelAttribute Course course, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating course. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            ArrayList<Classroom> allClassrooms = (ArrayList<Classroom>) classroomRepository.findAll();
            ArrayList<Classroom> normalClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
            ArrayList<Classroom> labClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.LABORATORY).collect(Collectors.toCollection(ArrayList<Classroom>::new));

            model.addAttribute("normalClassrooms", normalClassrooms);
            model.addAttribute("labClassrooms", labClassrooms);
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("careers", careerRepository.findAll());
            model.addAttribute("groups", groupRepository.findAll());

            return "courses/create";
        }

        ArrayList<Classroom> classrooms = (ArrayList<Classroom>) course.getClassrooms();

        course.setName(WordUtils.capitalize(course.getName().toLowerCase()));
        course.setAcronym(course.getAcronym().toUpperCase());
        course.setKey(course.getKey().toUpperCase());

        log.info("Saving course with name " + course.getName());

        courseRepository.save(course);

        ArrayList<Group> otherGroups = (ArrayList<Group>) groupRepository.findBySemester(course.getGroup().getSemester());
        otherGroups.remove(course.getGroup());

        if (otherGroups.size() > 0) {
            for (Group group : otherGroups) {
                Course copy = new Course(course);
                copy.setClassrooms(classrooms);
                copy.setGroup(group);
                log.info("Saving course mirror with name " + copy.getName());
                courseRepository.save(copy);
            }
        }

        return "redirect:";
    }

    @RequestMapping(value = "/show")
    public String showCourse(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing course with id " + id);
        model.addAttribute("course", courseRepository.findOne(id));
        return "courses/show";
    }

    @GetMapping(value = "/edit")
    public String editCourse(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing course with id " + id);

        ArrayList<Classroom> allClassrooms = (ArrayList<Classroom>) classroomRepository.findAll();
        ArrayList<Classroom> normalClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
        ArrayList<Classroom> labClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.LABORATORY).collect(Collectors.toCollection(ArrayList<Classroom>::new));

        model.addAttribute("normalClassrooms", normalClassrooms);
        model.addAttribute("labClassrooms", labClassrooms);
        model.addAttribute("course", courseRepository.findOne(id));
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
        model.addAttribute("careers", careerRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());

        return "courses/edit";
    }

    @PostMapping(value = "/edit")
    public String editCourse(@Valid @ModelAttribute Course course, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating course. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            ArrayList<Classroom> allClassrooms = (ArrayList<Classroom>) classroomRepository.findAll();
            ArrayList<Classroom> normalClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.CLASSROOM).collect(Collectors.toCollection(ArrayList<Classroom>::new));
            ArrayList<Classroom> labClassrooms = allClassrooms.stream().filter(classroom -> classroom.getType() == Type.LABORATORY).collect(Collectors.toCollection(ArrayList<Classroom>::new));

            model.addAttribute("normalClassrooms", normalClassrooms);
            model.addAttribute("labClassrooms", labClassrooms);
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            model.addAttribute("careers", careerRepository.findAll());
            model.addAttribute("groups", groupRepository.findAll());
            return "courses/edit";
        }

        log.info("Saving course with name " + course.getName());

        courseRepository.save(course);

        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteCourse(@RequestParam(name = "id") long id) {
        log.info("Deleting course with id " + id);

        courseRepository.delete(id);
        return "redirect:";
    }

   @RequestMapping(value = "/groups")
    public String getGroupsByCareer(@RequestParam(name = "id_career") long idCareer, Model model) {
        log.info("Getting all groups for career with id " + idCareer);

        Career career = careerRepository.findOne(idCareer);

        model.addAttribute(new Course());
        model.addAttribute("groups", careerRepository.findOne(idCareer).getGroups());

        return "courses/create :: groups";
   }
}
