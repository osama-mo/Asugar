package com.agilesekeri.asugar_api.security.password.token;

import com.agilesekeri.asugar_api.actionToken.ActionToken;
import com.agilesekeri.asugar_api.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordToken extends ActionToken {
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "reset_pass_token_id"
    )
    private AppUser appUser;

    public ResetPasswordToken(String token,
                              LocalDateTime createdAt,
                              LocalDateTime expiresAt,
                              AppUser appUser) {
        super(token, createdAt, expiresAt);
        this.appUser = appUser;
    }
}
