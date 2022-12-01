package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;

import javax.persistence.*;

@Entity
public class ProjectMember {
    @EmbeddedId
    private ProjectMemberKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Project project;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private AppUser user;

    private ProjectMemberRole role;
}
