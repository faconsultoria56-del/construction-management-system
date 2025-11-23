package com.management.resource.repository;

import com.management.resource.model.ProjectResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectResourceRepository extends JpaRepository<ProjectResource, Integer> {
    List<ProjectResource> findByProjectId(Integer projectId);
}
