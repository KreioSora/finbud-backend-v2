package com.project.backend.savings.service;

import com.project.backend.common.exceptions.ValidationException;
import com.project.backend.common.models.AppResponse;
import com.project.backend.common.response.MainResponse;
import com.project.backend.savings.converter.SavingsConverter;
import com.project.backend.savings.models.Savings;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import com.project.backend.savings.models.requests.SavingsUpdateRequest;
import com.project.backend.savings.repository.SavingsRepository;
import com.project.backend.savings.validation.savings.CreateSavingsValidator;
import com.project.backend.savings.validation.savings.UpdateSavingsValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Service
@AllArgsConstructor
public class SavingsService {
    private final MainResponse mainResponse;
    private final SavingsConverter savingsConverter;
    private final SavingsRepository savingsRepository;
    private final CreateSavingsValidator createSavingsValidator;
    private final UpdateSavingsValidator updateSavingsValidator;

    public ResponseEntity<AppResponse> dashboard(Integer page, Integer pageSize, String nameQuery) {
        try {
            Page<Savings> savings = savingsRepository.findAll(PageRequest.of(page, pageSize, Sort.by("name").ascending()));
            return mainResponse.success(savings);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage());
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> savingsDetail(String code) {
        try {
            Savings savings = savingsRepository.findByCodeStartingWith(code)
                    .orElseThrow(() -> new EntityNotFoundException("Code Not Found"));
            return mainResponse.success(savings);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), code);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> addSavings(SavingsCreateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Create Savings Request");
            createSavingsValidator.validate(request, validation);
            if (validation.hasErrors()) {
                throw new ValidationException("Invalid Request", validation);
            }
            Savings savings = savingsConverter.createSavingsConverter(request);
            savings = savingsRepository.save(savings);
            return mainResponse.success(savings);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> updateSavings(Long id, SavingsUpdateRequest request) {
        try {
            Errors validation = new BeanPropertyBindingResult(request, "Update Savings Request");
            createSavingsValidator.validate(request, validation);
            if (validation.hasErrors()) {
                throw new ValidationException("Invalid Request", validation);
            }
            Savings savings = savingsRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Savings not found"));
            savings = savingsConverter.updateSavingsConverter(savings, request);
            savings = savingsRepository.save(savings);
            return mainResponse.success(savings);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), request);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }

    public ResponseEntity<AppResponse> deleteSavings(Long id) {
        try {
            Savings savings = savingsRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Savings not found"));
            savings = savingsConverter.deleteSavingsConverter(savings);
            savings = savingsRepository.save(savings);
            return mainResponse.success(savings);
        } catch (RuntimeException e) {
            return mainResponse.clientError(e.getMessage(), id);
        } catch (Exception e) {
            return mainResponse.serverError(e.getMessage());
        }
    }
}
