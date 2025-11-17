package com.management.task.repository;

import com.management.task.model.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Integer> {
    List<ProjectTask> findByProjectId(Integer projectId);
}
