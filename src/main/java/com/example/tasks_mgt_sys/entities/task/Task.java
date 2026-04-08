package com.example.tasks_mgt_sys.entities.task;

import com.example.tasks_mgt_sys.entities.project.Project;
import com.example.tasks_mgt_sys.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Project project;

    private LocalDateTime dueDate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
