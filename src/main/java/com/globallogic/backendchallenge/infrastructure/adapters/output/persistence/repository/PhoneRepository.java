package com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository;

import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.PhoneEntity;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.PhoneId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, PhoneId> {

    boolean existsById(PhoneId id);
}
