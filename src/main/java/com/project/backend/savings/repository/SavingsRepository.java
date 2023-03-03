package com.project.backend.savings.repository;

import com.project.backend.savings.models.Savings;
import com.project.backend.savings.models.SavingsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

    Optional<Savings> findAllByType(SavingsType type);

    Optional<Savings> findByCodeStartingWith(String code);

}
