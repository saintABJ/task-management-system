package com.example.tasks_mgt_sys.entities.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Long> {
}
