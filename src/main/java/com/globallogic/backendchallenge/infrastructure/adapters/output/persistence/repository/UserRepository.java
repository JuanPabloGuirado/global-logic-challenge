package com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository;

import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserUuid(String uuid);

    boolean existsByNameAndEmail(String name, String email);
}
