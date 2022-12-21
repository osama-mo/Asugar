package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.ActionToken;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordTokenEntity extends ActionToken {
    @ManyToOne
    @JoinColumn(
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id")
    )
    private AppUserEntity appUser;

    public ResetPasswordTokenEntity(String token,
                                    LocalDateTime createdAt,
                                    LocalDateTime expiresAt,
                                    AppUserEntity appUser) {
        super(token, createdAt, expiresAt);
        this.appUser = appUser;
    }
}
