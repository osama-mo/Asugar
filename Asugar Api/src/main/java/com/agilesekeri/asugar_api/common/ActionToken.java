package com.agilesekeri.asugar_api.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public abstract class ActionToken {
    @Id
    @SequenceGenerator(
            name = "action_token_sequence",
            sequenceName = "action_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "action_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    public ActionToken(String token,
                       LocalDateTime createdAt,
                       LocalDateTime expiresAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
