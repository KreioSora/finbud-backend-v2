package com.project.backend.savings.repository;

import com.project.backend.savings.models.SavingsPockets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsPocketsRepository extends JpaRepository<SavingsPockets, Long> {

    Optional<SavingsPockets> findByUser_UsernameAndAccount_IdAndId(String username, Long accountId, Long id);

    Optional<Page<SavingsPockets>> findAllByUser_UsernameAndAccount_Id(String username, Long accountId, Pageable pageable);

}
