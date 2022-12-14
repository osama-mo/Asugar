package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.model.entity.RegistrationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RegistrationTokenRepository
        extends JpaRepository<RegistrationTokenEntity, Long> {

    Optional<RegistrationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE RegistrationTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
