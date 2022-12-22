package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "project")
public class ProjectEntity {
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
    @JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "fk_admin_id"))
    private AppUserEntity admin;

    @ManyToOne
    @JoinColumn(name = "product_owner_id", foreignKey = @ForeignKey(name = "fk_product_owner_id"))
    private AppUserEntity productOwner;

    @ManyToMany
    @JoinTable(
            name = "app_user_project",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_id")),
            inverseJoinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_member_id")))
    private Set<AppUserEntity> members;

    @OneToMany(mappedBy = "project")
    private Set<SprintEntity> sprints;

    @OneToMany(mappedBy = "project")
    private Set<EpicEntity> Epics;

    @OneToMany(mappedBy = "project")
    private Set<AbstractIssue> issues;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "planned_to",
            nullable = true
    )
    private LocalDateTime plannedTo;

    @Column(
            name = "ended_at",
            nullable = true
    )
    private LocalDateTime endedAt;

    public ProjectEntity(String name, AppUserEntity admin) {
        this.name = name;
        this.admin = admin;
        this.plannedTo = null;
        this.createdAt = LocalDateTime.now();
        this.endedAt = null;
        this.members = new HashSet<>();
        this.members.add(admin);
    }

    public boolean addMember(AppUserEntity user) {
        return members.add(user);
    }

    public boolean removeMember(AppUserEntity user) {
        boolean result = members.remove(user);
        user.getProjects().remove(this);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity project = (ProjectEntity) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, admin);
    }
}
