package com.albertoalegria.mendel.controllers.classrooms;

import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alberto Alegria
 */

@RestController
@RequestMapping(value = "/classrooms/rest")
public class ClassroomsRestController {
    @Autowired
    private ClassroomService classroomService;

    @GetMapping(value = "/all")
    public List<Classroom> getAll() {
        return classroomService.getAll();
    }

    @GetMapping(value = "/show/{id}")
    public Classroom getById(@PathVariable(name = "id") long id) {
        return classroomService.getById(id);
    }

    @PostMapping(value = "/delete/{id}")
    public List<Classroom> delete(@PathVariable(name = "id") long id) {
        return classroomService.delete(id);
    }
}
