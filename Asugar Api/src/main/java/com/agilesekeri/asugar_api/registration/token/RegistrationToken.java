package com.agilesekeri.asugar_api.registration.token;

import com.agilesekeri.asugar_api.actionToken.ActionToken;
import com.agilesekeri.asugar_api.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RegistrationToken extends ActionToken {
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public RegistrationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             AppUser appUser) {
        super(token, createdAt, expiresAt);
        this.appUser = appUser;
    }
}
