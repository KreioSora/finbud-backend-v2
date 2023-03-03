package com.project.backend.savings.repository;

import com.project.backend.savings.models.SavingsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsTypeRepository extends JpaRepository<SavingsType, Long> {

    Optional<SavingsType> findByName(String name);

}
