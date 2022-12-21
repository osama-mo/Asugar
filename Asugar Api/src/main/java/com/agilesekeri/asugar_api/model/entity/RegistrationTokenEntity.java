package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.ActionToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RegistrationTokenEntity extends ActionToken {
    @ManyToOne
    @JoinColumn(
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id")
    )
    private AppUserEntity appUser;

    public RegistrationTokenEntity(String token,
                                   LocalDateTime createdAt,
                                   LocalDateTime expiresAt,
                                   AppUserEntity appUser) {
        super(token, createdAt, expiresAt);
        this.appUser = appUser;
    }
}
