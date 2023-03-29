package com.project.backend.transactions.service;

import com.project.backend.common.models.AppResponse;
import com.project.backend.common.response.MainResponse;
import com.project.backend.common.validation.Validation;
import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.savings.repository.SavingsAccountRepository;
import com.project.backend.savings.repository.SavingsPocketsRepository;
import com.project.backend.savings.service.SavingsAccountService;
import com.project.backend.savings.service.SavingsPocketService;
import com.project.backend.transactions.converter.TransactionConverter;
import com.project.backend.transactions.models.Transactions;
import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.transactions.models.requests.TransactionUpdateRequest;
import com.project.backend.transactions.repository.TransactionsRepository;
import com.project.backend.transactions.validation.TransactionValidator;
import com.project.backend.user.UserService;
import com.project.backend.user.models.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final MainResponse mainResponse;
    private final SavingsPocketService pocketService;
    private final SavingsAccountService accountService;
    private final TransactionConverter transactionConverter;
    private final TransactionValidator transactionValidator;
    private final TransactionsRepository transactionsRepository;
    private final SavingsPocketsRepository savingsPocketsRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    public ResponseEntity<AppResponse> dashboard(Integer page, Integer pageSize, String dateQuery, Long accountId) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created_at").descending());
            User user = userService.getAuthenticatedUser();
            Page<Transactions> transactions;
            if (dateQuery == null) {
                transactions = transactionsRepository.findAllByUser_UsernameAndAccount_Id(user.getUsername(), accountId, pageable)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            } else {
                OffsetDateTime query = OffsetDateTime.parse(dateQuery, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                transactions = transactionsRepository.findAllByUser_UsernameAndAccount_IdAndCreatedAtAfter(user.getUsername(), accountId, query, pageable)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            }
            return mainResponse.success(transactions);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), accountId);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> transactionDetails(Long idAccount, String idTransact) {
        try {
            Transactions transactions = transactionsRepository.findByUser_UsernameAndAccount_IdAndId(userService.getAuthenticatedUser().getUsername(), idAccount, idTransact)
                    .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
            return mainResponse.success(transactions);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), idTransact);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> createTransaction(Long idAccount, TransactionCreateRequest request) {
        try {
            Validation.validateRequest(request, "Create Transaction Request", transactionValidator);
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = null;
            if (request.getPocketId() != null) {
                pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, request.getPocketId())
                        .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            }
            Transactions transaction = transactionConverter.createTransactionConverter(request, user, account, pocket);
            transaction = transactionsRepository.save(transaction);
            updateAccountAndPocket(account, pocket, request.getAmount() * request.getType().doubleValue());
            return mainResponse.success(transaction);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updateTransaction(Long idAccount, String idTransact, TransactionUpdateRequest request) {
        try {
            Validation.validateRequest(request, "Update Transaction Request", transactionValidator);
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = null;
            if (request.getPocketId() != null) {
                pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, request.getPocketId())
                        .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            }
            Transactions transaction = transactionsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, idTransact)
                    .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
            transaction = transactionConverter.updateTransactionConverter(request, transaction, pocket);
            Double change = (request.getAmount() * request.getType()) - (transaction.getAmount() * transaction.getType().getCode());
            updateAccountAndPocket(account, pocket, change);
            return mainResponse.success(transaction);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> deleteTransaction(Long idAccount, String idTransact) {
        try {
            User user = userService.getAuthenticatedUser();
            Transactions transaction = transactionsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, idTransact)
                    .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
            transaction = transactionConverter.deleteTransactionConverter(transaction);
            transaction = transactionsRepository.save(transaction);
            return mainResponse.success(transaction);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), idTransact);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public void updateAccountAndPocket(SavingsAccount account, SavingsPockets pocket, Double change) {
        accountService.updateAccountAmount(account, change);
        if (pocket != null) {
            pocketService.updatePocketAmount(pocket, change);
        }
    }
}
