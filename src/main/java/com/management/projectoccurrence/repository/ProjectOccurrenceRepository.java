package com.management.projectoccurrence.repository;

import com.management.projectoccurrence.model.ProjectOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectOccurrenceRepository extends JpaRepository<ProjectOccurrence, Integer> {

    List<ProjectOccurrence> findByProjectId(Integer projectId);
}
