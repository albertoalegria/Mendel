package com.albertoalegria.mendel.repositories;

import com.albertoalegria.mendel.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Alberto Alegria
 */

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findBySemester(int semester);
}
