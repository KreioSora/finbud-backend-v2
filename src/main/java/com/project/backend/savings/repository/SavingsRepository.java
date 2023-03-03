package com.project.backend.savings.repository;

import com.project.backend.savings.models.Savings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

    Optional<Page<Savings>> findAllByCodeStartingWith(String code, Pageable pageable);

    Optional<Savings> findByCodeStartingWith(String code);

}
