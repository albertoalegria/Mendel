package com.albertoalegria.mendel.controllers.courses;

import com.albertoalegria.mendel.models.Group;
import com.albertoalegria.mendel.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
@RestController
@RequestMapping(value = "courses/rest")
public class CoursesRestController {

    @Autowired
    private GroupRepository groupRepository;
    @RequestMapping("/groups")
    public List<Group> getGroups(@RequestParam(name = "id_career") long idCareer) {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().filter(group -> group.getCareer().getId().equals(idCareer)).collect(Collectors.toCollection(ArrayList<Group>::new));
    }
}
