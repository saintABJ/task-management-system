package com.example.tasks_mgt_sys.entities.user;

import com.example.tasks_mgt_sys.entities.org.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByOrganizationId(Long organizationId);

    boolean existsUserByEmailAndOrganizationId(String email, Long organizationId);

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmailIgnoreCase(String email);

    //boolean existByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findUserById(UUID id);

    List<User> findUserByOrganization(Organization organization);
}
