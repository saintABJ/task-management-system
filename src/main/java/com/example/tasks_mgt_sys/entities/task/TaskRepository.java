package com.example.tasks_mgt_sys.entities.task;

import com.example.tasks_mgt_sys.entities.org.Organization;
import com.example.tasks_mgt_sys.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getTasksByUser(User user);

    Optional<Task> getTaskById(Long id);

    Optional<Task> findTaskByIdAndUser(Long id, User user);

    List<Task> findByUserAndDueDateBeforeAndStatusNot(User user, LocalDateTime dueDate, TaskStatus status);

    /*Page<Task> findByOrganizationAndStatus(
            Organization organization,
            TaskStatus status,
            Pageable pageable
    );

    Page<Task> findByOrganization(
            Organization organization,
            Pageable pageable
    );*/

    Page<Task> findByProject_OrganizationAndDeletedAtIsNull(
            Organization organization,
            Pageable pageable
    );
}
