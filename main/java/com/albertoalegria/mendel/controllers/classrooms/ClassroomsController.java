package com.albertoalegria.mendel.controllers.classrooms;

import com.albertoalegria.mendel.enums.Type;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Timetable;
import com.albertoalegria.mendel.repositories.BuildingRepository;
import com.albertoalegria.mendel.repositories.ClassroomRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.albertoalegria.mendel.utils.ResourceManager.MESSAGES;

/**
 * @author Alberto Alegria
 */

@Controller
@RequestMapping(value = "/classrooms")
public class ClassroomsController {
    private Log log = LogFactory.getLog(ClassroomsController.class);

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all classrooms");
        if (classroomRepository.count() > 0) {
            model.addAttribute("classrooms", classroomRepository.findAll());
        }
        return "classrooms/index";
    }

    @GetMapping(value = "/create")
    public String createClassroom(Model model) {
        log.info("Creating classroom");
        if (buildingRepository.count() > 0) {
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("buildings", buildingRepository.findAll());
            model.addAttribute("types", Type.values());
            model.addAttribute(new Classroom());
        }
        return "classrooms/create";
    }

    @PostMapping(value = "/create")
    public String createClassroom(@Valid @ModelAttribute Classroom classroom, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating classroom. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("buildings", buildingRepository.findAll());
            model.addAttribute("types", Type.values());

            return "classrooms/create";
        }

        Timetable timetable = new Timetable();
        timetable.buildTimetable();
        classroom.setTimetable(timetable);

        log.info("Saving classroom with name " + classroom.getName());

        classroomRepository.save(classroom);

        return "redirect:";
    }

    @RequestMapping(value = "/show")
    public String showClassroom(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing classroom with id " + id);

        model.addAttribute("classroom", classroomRepository.findOne(id));
        return "classrooms/show";
    }

    @GetMapping(value = "/edit")
    public String editClassroom(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing classroom with id " + id);

        model.addAttribute("classroom", classroomRepository.findOne(id));
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
        model.addAttribute("types", Type.values());

        return "classrooms/edit";
    }

    @PostMapping(value = "/edit")
    public String editClassroom(@Valid @ModelAttribute Classroom classroom, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating classroom. Model is not valid");

            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            model.addAttribute("buildings", buildingRepository.findAll());
            model.addAttribute("types", Type.values());

            return "classrooms/edit";
        }


        Timetable timetable = new Timetable();
        timetable.buildTimetable();
        classroom.setTimetable(timetable);

        log.info("Saving classroom with name " + classroom.getName());

        classroomRepository.save(classroom);

        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteClassroom(@RequestParam(name = "id") long id) {
        log.info("Deleting classroom with id " + id);

        classroomRepository.delete(classroomRepository.findOne(id));
        return "redirect:";
    }
}
