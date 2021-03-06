package com.albertoalegria.mendel.repositories;

import com.albertoalegria.mendel.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alberto Alegria
 */
@Repository
@Transactional
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
