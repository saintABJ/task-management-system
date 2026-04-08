package com.example.tasks_mgt_sys.entities.project;

import com.example.tasks_mgt_sys.entities.org.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> getAllProjectsByOrganization(Organization organization);

    Optional<Project> getProjectById(Long id);

    Page<Project> findByOrganization(Organization organization, Pageable pageable);
}
