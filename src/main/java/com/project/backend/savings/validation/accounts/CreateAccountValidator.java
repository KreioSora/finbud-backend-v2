package com.project.backend.savings.validation.accounts;

import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateAccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountCreateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountCreateRequest request = (AccountCreateRequest) target;
        // Savings validation
        if (request.getSavingsId() == null) errors.rejectValue("savings_id", request.getSavingsId().toString());
        // Currency validation
        if (request.getCurrency() == null || request.getCurrency().isEmpty()) errors.rejectValue("currency", request.getCurrency().toString());
        // Account Name validation
        if (request.getAccountName() == null || request.getAccountName().isEmpty()) errors.rejectValue("account_name", request.getAccountName());
        // Account Number validation
        if (request.getAccountNumber() == null || request.getAccountNumber().isEmpty()) errors.rejectValue("account_number", request.getAccountNumber());
        // Total Amount validation
        if (request.getTotalAmount() == null || request.getTotalAmount() < 0) errors.rejectValue("total_amount", request.getTotalAmount().toString());
    }
}
