package com.example.tasks_mgt_sys.entities.user;

import com.example.tasks_mgt_sys.entities.org.Organization;
import com.example.tasks_mgt_sys.entities.project.Project;
import com.example.tasks_mgt_sys.entities.role.Role;
import com.example.tasks_mgt_sys.entities.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,  unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean isVerified;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Organization organization;

    @ManyToMany
    //use fetch on field or EntityGraph on repo methods but use EntityGraph instead because it's good practice compared to fetch which loads even when not needed.
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;
}
