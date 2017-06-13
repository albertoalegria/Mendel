package com.albertoalegria.mendel.controllers.careers;

import com.albertoalegria.mendel.models.Career;
import com.albertoalegria.mendel.repositories.CareerRepository;
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
@RequestMapping(value = "/careers")
public class CareersController {
    private Log log = LogFactory.getLog(CareersController.class);

    @Autowired
    private CareerRepository careerRepository;

    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        log.info("Showing all careers");

        if (careerRepository.count() > 0) {
            model.addAttribute("careers", careerRepository.findAll());
        }

        return "careers/index";
    }

    @GetMapping(value = "/create")
    public String createCareer(Model model) {
        log.info("Creating career");

        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
        model.addAttribute(new Career());
        return "careers/create";
    }

    @PostMapping(value = "/create")
    public String createClassroom(@Valid @ModelAttribute Career career, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating career. Model is not valid");

            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.create"));
            return "careers/create";
        }

        log.info("Saving career with name " + career.getName());
        careerRepository.save(career);

        return "redirect:";
    }

    @RequestMapping(value = "/show")
    public String showCareer(@RequestParam(name = "id") long id, Model model) {
        log.info("Showing career with id " + id);

        model.addAttribute("career", careerRepository.findOne(id));
        return "careers/show";
    }

    @GetMapping(value = "/edit")
    public String editCareer(@RequestParam(name = "id") long id, Model model) {
        log.info("Editing career with id " + id);

        model.addAttribute("career", careerRepository.findOne(id));
        model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));

        return "careers/edit";
    }

    @PostMapping(value = "/edit")
    public String editCareer(@Valid @ModelAttribute Career career, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Error creating career. Model is not valid");
            for (ObjectError error : errors.getAllErrors()) {
                log.error(error.getDefaultMessage());
            }

            model.addAttribute("buttonText", MESSAGES.getString("mendel.general.save"));
            return "careers/edit";
        }

        log.info("Saving career with name " + career.getName());

        careerRepository.save(career);
        return "redirect:";
    }

    @RequestMapping(value = "/delete")
    public String deleteCareer(@RequestParam(name = "id") long id) {
        log.info("Deleting career with id " + id);
        careerRepository.delete(id);
        return "redirect:";
    }
}
