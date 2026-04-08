package com.example.tasks_mgt_sys.entities.project;

import com.example.tasks_mgt_sys.entities.org.Organization;
import com.example.tasks_mgt_sys.entities.task.Task;
import com.example.tasks_mgt_sys.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project")
    private List<Task> task;
}

