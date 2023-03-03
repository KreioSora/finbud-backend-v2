package com.project.backend.savings.repository;

import com.project.backend.savings.models.SavingsAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    Optional<SavingsAccount> findByIdAndUser_Username(Long id, String username);

    Optional<Page<SavingsAccount>> findAllByUser_Username(String username, Pageable pageable);

    Optional<Page<SavingsAccount>> findAllByUser_UsernameAndSavings_Name(String username, String name, Pageable pageable);

}
