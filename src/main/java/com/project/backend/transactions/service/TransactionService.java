package com.project.backend.transactions.service;

import com.project.backend.common.exceptions.ValidationException;
import com.project.backend.common.models.AppResponse;
import com.project.backend.common.response.MainResponse;
import com.project.backend.savings.converter.SavingsPocketConverter;
import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.SavingsPockets;
import com.project.backend.savings.models.requests.PocketCreateRequest;
import com.project.backend.savings.models.requests.PocketUpdateRequest;
import com.project.backend.savings.repository.SavingsAccountRepository;
import com.project.backend.savings.repository.SavingsPocketsRepository;
import com.project.backend.savings.service.SavingsAccountService;
import com.project.backend.savings.service.SavingsPocketService;
import com.project.backend.savings.validation.pockets.CreatePocketValidator;
import com.project.backend.savings.validation.pockets.UpdatePocketValidator;
import com.project.backend.transactions.converter.TransactionConverter;
import com.project.backend.transactions.models.Transactions;
import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.transactions.repository.TransactionsRepository;
import com.project.backend.transactions.validation.CreateTransactionValidator;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final UserService userService;
    private final SavingsPocketService pocketService;
    private final SavingsAccountService accountService;
    private final MainResponse mainResponse;
    private final TransactionConverter transactionConverter;
    private final TransactionsRepository transactionsRepository;
    private final SavingsPocketsRepository savingsPocketsRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final CreateTransactionValidator createTransactionValidator;

    public ResponseEntity<AppResponse> dashboard(Integer page, Integer pageSize, String query, Long accountId) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created_at").descending());
            Page<Transactions> transactions = transactionsRepository.findAllByUser_UsernameAndAccount_Id(userService.getAuthenticatedUser().getUsername(), accountId, pageable)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
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
            Errors validation = new BeanPropertyBindingResult(request, "Create Transaction Request");
            createTransactionValidator.validate(request, validation);
            if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = null;
            if (request.getPocketId() != null ) {
                pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), request.getAccountId(), request.getPocketId())
                        .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            }
            Transactions transaction = transactionConverter.createTransactionConverter(request, user, account, pocket);
            transaction = transactionsRepository.save(transaction);
            updateAccountAndPocket(account, pocket, request.getAmount() * request.getType().doubleValue());
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updatePocket(Long idAccount, Long idPocket, PocketUpdateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Update Pocket Request");
            createPocketValidator.validate(request, validation);
            if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, idPocket)
                    .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            pocket = savingsPocketConverter.updatePocketConverter(pocket, request);
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updatePocketAmount(Long idAccount, Long idPocket, Double change) {
        try {
            if (change == 0D) {
                throw new ValidationException("Invalid change amount", new Object[]{change});
            }
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(user.getUsername(), idAccount, idPocket)
                    .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            pocket = savingsPocketConverter.updateAmountConverter(pocket, change);
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), change);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> deletePocket(Long idAccount, Long idPocket) {
        try {
            SavingsPockets pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(userService.getAuthenticatedUser().getUsername(), idAccount, idPocket)
                    .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            pocket = savingsPocketsRepository.save(pocket);
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), idPocket);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public void updateAccountAndPocket(SavingsAccount account, SavingsPockets pocket, Double change) {
        accountService.updateAccountAmount(account, change);
        if (pocket != null) {
            pocketService.updatePocketAmount(account, pocket, change);
        }
    }
}
