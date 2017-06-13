package com.albertoalegria.mendel.services;

import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.repositories.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alberto Alegria
 */

@Service("classroomService")
public class ClassroomService implements ModelService<Classroom> {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public List<Classroom> getAll() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getById(long id) {
        return classroomRepository.findOne(id);
    }

    @Override
    public List<Classroom> create(Classroom classroom) {
        classroomRepository.save(classroom);
        return getAll();
    }

    @Override
    public List<Classroom> delete(long id) {
        classroomRepository.delete(id);
        return getAll();
    }
}
