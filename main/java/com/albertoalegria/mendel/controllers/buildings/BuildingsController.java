package com.albertoalegria.mendel.controllers.buildings;

import com.albertoalegria.mendel.models.Building;
import com.albertoalegria.mendel.repositories.BuildingRepository;
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
@RequestMapping(value = "/buildings")
public class BuildingsController {
    private Log log = LogFactory.getLog(BuildingsController.class);

    @Autowired
    private BuildingRepository buildingRepository;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all buildings");

        if (buildingRepository.count() > 0) {
            model.addAttribute("buildings", buildingRepository.findAll());
        }
        return "buildings/index";
    }

    @GetMapping(value = "/create")
    public String createBuilding(Model model) {
        log.info("Creating building");

        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
        model.addAttribute(new Building());

        return "buildings/create";
    }

    @PostMapping(value = "/create")
    public String createBuilding(@Valid @ModelAttribute Building building, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating building. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            return "buildings/create";
        }

        log.info("Saving building with name " + building.getName());

        buildingRepository.save(building);

        return "redirect:";
    }

    @RequestMapping(value = "show")
    public String showBuilding(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing building with id " + id);

        model.addAttribute("building", buildingRepository.findOne(id));
        return "buildings/show";
    }

    @GetMapping(value = "/edit")
    public String editBuilding(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing building with id " + id);

        model.addAttribute("building", buildingRepository.findOne(id));
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));

        return "buildings/edit";
    }

    @PostMapping(value = "/edit")
    public String editBuilding(@Valid @ModelAttribute Building building, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error editing building. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }
            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            return "buildings/edit";
        }

        log.info("Saving building with name " + building.getName());

        buildingRepository.save(building);
        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteBuilding(@RequestParam(name = "id") long id) {
        log.info("Deleting building with id " + id);

        buildingRepository.delete(id);
        return "redirect:";
    }
}
