package com.project.backend.transactions.repository;

import com.project.backend.transactions.models.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {

    Optional<Transactions> findByUser_UsernameAndAccount_IdAndId(String username, Long accountId, String id);

    Optional<Page<Transactions>> findAllByUser_UsernameAndAccount_Id(String username, Long accountId, Pageable pageable);

}
