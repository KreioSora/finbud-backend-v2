package com.project.backend.savings.validation.accounts;

import com.project.backend.savings.models.requests.AccountCreateRequest;
import com.project.backend.savings.models.requests.AccountUpdateRequest;
import com.project.backend.savings.models.requests.SavingsCreateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateAccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountUpdateRequest request = (AccountUpdateRequest) target;
        // Currency validation
        if (request.getCurrency().isEmpty()) errors.rejectValue("currency", request.getCurrency().toString());
        // Account Name validation
        if (request.getAccountName() == null || request.getAccountName().isEmpty()) errors.rejectValue("account_name", request.getAccountName());
        // Account Number validation
        if (request.getAccountNumber().isEmpty()) errors.rejectValue("account_number", request.getAccountNumber());
    }
}
