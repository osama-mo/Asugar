package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.model.entity.ResetPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ResetPasswordTokenRepository
        extends JpaRepository<ResetPasswordTokenEntity, Long> {

    Optional<ResetPasswordTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ResetPasswordTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
