package com.project.backend.savings.service;

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
import com.project.backend.savings.validation.pockets.CreatePocketValidator;
import com.project.backend.savings.validation.pockets.UpdatePocketValidator;
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
public class SavingsPocketService {

    private final UserService userService;
    private final MainResponse mainResponse;
    private final CreatePocketValidator createPocketValidator;
    private final UpdatePocketValidator updatePocketValidator;
    private final SavingsPocketConverter savingsPocketConverter;
    private final SavingsPocketsRepository savingsPocketsRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    public ResponseEntity<AppResponse> accountPocket(Integer page, Integer pageSize, String query, Long id) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created_at").descending());
            Page<SavingsPockets> pocket = savingsPocketsRepository.findAllByUser_UsernameAndAccount_Id(userService.getAuthenticatedUser().getUsername(), id, pageable)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), id);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> pocket(Long idAccount, Long idPocket) {
        try {
            SavingsPockets pocket = savingsPocketsRepository.findByUser_UsernameAndAccount_IdAndId(userService.getAuthenticatedUser().getUsername(), idAccount, idPocket)
                    .orElseThrow(() -> new EntityNotFoundException("Pocket not found"));
            return mainResponse.success(pocket);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), idPocket);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> addPocket(Long idAccount, PocketCreateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Create Pocket Request");
            createPocketValidator.validate(request, validation);
            if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
            User user = userService.getAuthenticatedUser();
            SavingsAccount account = savingsAccountRepository.findByIdAndUser_Username(idAccount, user.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            SavingsPockets pocket = savingsPocketConverter.createPocketConverter(request, user, account);
            pocket = savingsPocketsRepository.save(pocket);
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
            updatePocketValidator.validate(request, validation);
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
            return updatePocketAmount(account, pocket, change);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), change);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updatePocketAmount(SavingsAccount account, SavingsPockets pocket, Double change) {
        pocket = savingsPocketConverter.updateAmountConverter(pocket, change);
        savingsPocketsRepository.save(pocket);
        return mainResponse.success(pocket);
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
}
