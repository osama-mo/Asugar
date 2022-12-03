package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project {
    @Id
    @SequenceGenerator(
            name = "project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_sequence"
    )
    private Long id;

    private String name;

    @ManyToOne
    private AppUser admin;

    @ManyToMany
    @JoinTable(
            name = "app_user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<AppUser> members;

//    @ManyToMany
//    private Collection<Sprint> sprints;
//
//    @ManyToMany
//    private Collection<Long> Epics;

    private LocalDateTime createdAt;
    private LocalDateTime plannedTo;
    private LocalDateTime endedAt;

    public Project(String name, AppUser admin) {
        this.name = name;
        this.admin = admin;
        this.plannedTo = null;
        this.createdAt = LocalDateTime.now();
        this.endedAt = null;
        this.members = new ArrayList<>();
        this.members.add(admin);
    }
}
