package com.albertoalegria.mendel.controllers.groups;

import com.albertoalegria.mendel.enums.Shift;
import com.albertoalegria.mendel.models.Group;
import com.albertoalegria.mendel.models.Timetable;
import com.albertoalegria.mendel.repositories.CareerRepository;
import com.albertoalegria.mendel.repositories.GroupRepository;
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
@RequestMapping(value = "/groups")
public class GroupsController {
    private Log log = LogFactory.getLog(GroupsController.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CareerRepository careerRepository;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all groups");
        if (groupRepository.count() > 0) {
            model.addAttribute("groups", groupRepository.findAll());
        }
        return "groups/index";
    }

    @GetMapping(value = "/create")
    public String createGroup(Model model) {
        log.info("Creating group");

        if (careerRepository.count() > 0) {
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("careers", careerRepository.findAll());
            model.addAttribute("shifts", Shift.values());
            model.addAttribute(new Group());
        }
        return "groups/create";
    }

    @PostMapping(value = "/create")
    public String createGroup(@Valid @ModelAttribute Group group, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating group. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            model.addAttribute("careers", careerRepository.findAll());
            model.addAttribute("shifts", Shift.values());

            return "groups/create";
        }

        Timetable timetable = new Timetable();
        timetable.buildTimetable(Shift.normalizeShift(group.getShift()));
        group.setTimetable(timetable);

        log.info("Saving group with name " + group.getName());

        groupRepository.save(group);

        return "redirect:";
    }

    @RequestMapping(value = "/show")
    public String showGroup(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing group with id " + id);

        model.addAttribute("group", groupRepository.findOne(id));

        return "groups/show";
    }

    @GetMapping(value = "/edit")
    public String editGroup(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing group with id " + id);

        model.addAttribute("group", groupRepository.findOne(id));
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
        model.addAttribute("careers", careerRepository.findAll());
        model.addAttribute("shifts", Shift.values());

        return "groups/edit";
    }

    @PostMapping(value = "/edit")
    public String editGroup(@Valid @ModelAttribute Group group, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.info("Error creating group. Model is not valid");

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            model.addAttribute("careers", careerRepository.findAll());
            model.addAttribute("shifts", Shift.values());

            return "groups/edit";
        }

        Timetable timetable = new Timetable();
        timetable.buildTimetable(group.getShift());
        group.setTimetable(timetable);

        log.info("Saving group with name " + group.getName());

        groupRepository.save(group);

        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteGroup(@RequestParam(name = "id") long id) {
        log.info("Deleting group with id " + id);

        groupRepository.delete(id);
        return "redirect:";
    }
}
