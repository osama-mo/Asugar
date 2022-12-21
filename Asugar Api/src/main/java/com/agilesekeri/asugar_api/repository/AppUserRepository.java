package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository
        extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByEmail(String email);

    @Override
    Optional<AppUserEntity> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE AppUserEntity a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUserEntity a " +
            "SET a.password = (:password) WHERE a.email = (:userName)")
    int changePassword(@Param("userName") String email, @Param("password") String newPass);
}
