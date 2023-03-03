package com.project.backend.transactions.validation;

import com.project.backend.transactions.models.requests.TransactionCreateRequest;
import com.project.backend.transactions.models.requests.TransactionUpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TransactionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionCreateRequest.class.equals(clazz) || TransactionUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionCreateRequest request = (TransactionCreateRequest) target;
        if (request.getCurrency() == null || request.getCurrency().isEmpty()) errors.rejectValue("currency", request.getCurrency());
        if (request.getType() == null || request.getType() > 1 || request.getType() < -1) errors.rejectValue("type", request.getType().toString());
        if (request.getAmount() == null || request.getAmount() <= 0D) errors.rejectValue("amount", request.getAmount().toString());
    }
}
