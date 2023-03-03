package com.project.backend.savings.service;

import com.project.backend.common.exceptions.ValidationException;
import com.project.backend.common.models.AppResponse;
import com.project.backend.common.response.MainResponse;
import com.project.backend.savings.converter.SavingsAccountConverter;
import com.project.backend.savings.models.SavingsAccount;
import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.AccountUpdateRequest;
import com.project.backend.savings.repository.SavingsAccountRepository;
import com.project.backend.savings.validation.accounts.CreateAccountValidator;
import com.project.backend.savings.validation.accounts.UpdateAccountValidator;
import com.project.backend.user.UserService;
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
public class SavingsAccountService {
    private final UserService userService;
    private final MainResponse mainResponse;
    private final CreateAccountValidator createAccountValidator;
    private final UpdateAccountValidator updateAccountValidator;
    private final SavingsAccountConverter savingsAccountConverter;
    private final SavingsAccountRepository savingsAccountRepository;

    public ResponseEntity<AppResponse> dashboard(Integer page, Integer pageSize, String nameQuery) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("savings_name").ascending());
            Page<SavingsAccount> account = savingsAccountRepository.findAllByUser_UsernameAndSavings_Name(userService.getAuthenticatedUser().getUsername(), nameQuery, pageable)
                    .orElseThrow(() -> new EntityNotFoundException("No account found"));
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage());
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> accountDetail(Long id) {
        try {
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(id, userService.getAuthenticatedUser().getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account Not Found"));
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), id);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> createAccount(AccountCreateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Create Account Request");
            createAccountValidator.validate(request, validation);
            if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
            SavingsAccount account = savingsAccountConverter.createAccountConverter(request, userService.getAuthenticatedUser());
            account = savingsAccountRepository.save(account);
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updateAccount(Long id, AccountUpdateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Update Account Request");
            updateAccountValidator.validate(request, validation);
            if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(id, userService.getAuthenticatedUser().getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            account = savingsAccountConverter.updateAccountConverter(account, request);
            account = savingsAccountRepository.save(account);
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updateAccountAmount(Long id, Double change) {
        try {
            if (change == 0D){
                throw new ValidationException("Invalid change amount", new Object[]{change});
            }
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(id, userService.getAuthenticatedUser().getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            account = updateAccountAmount(account, change);
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), change);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> deleteAccount(Long id) {
        try {
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(id, userService.getAuthenticatedUser().getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            account = savingsAccountConverter.deleteAccountConverter(account);
            account = savingsAccountRepository.save(account);
            return mainResponse.success(account);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), id);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public SavingsAccount updateAccountAmount(SavingsAccount account, Double change){
        account = savingsAccountConverter.updateAmountConverter(account, change);
        account = savingsAccountRepository.save(account);
        return account;
    }
}
