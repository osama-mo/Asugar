package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.project.epic.Epic;
import com.agilesekeri.asugar_api.project.sprint.Sprint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToOne
    private AppUser productOwner;

    @ManyToMany
    @JoinTable(
            name = "app_user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> members;

    @OneToMany
    private Set<Sprint> sprints;

    @OneToMany
    private Set<Epic> Epics;

    private LocalDateTime createdAt;
    private LocalDateTime plannedTo;
    private LocalDateTime endedAt;

    public Project(String name, AppUser admin) {
        this.name = name;
        this.admin = admin;
        this.plannedTo = null;
        this.createdAt = LocalDateTime.now();
        this.endedAt = null;
        this.members = new HashSet<>();
        this.members.add(admin);
    }

    public boolean addMember(AppUser user) {
        return members.add(user);
    }

    public boolean removeMember(AppUser user) {
        boolean result = members.remove(user);
        user.getProjects().remove(this);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id) && name.equals(project.name) && admin.equals(project.admin) && createdAt.equals(project.createdAt) && Objects.equals(plannedTo, project.plannedTo) && Objects.equals(endedAt, project.endedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, admin, createdAt, plannedTo, endedAt);
    }
}
