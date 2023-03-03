package com.project.backend.savings.validation.savings;

import com.project.backend.savings.models.requests.SavingsCreateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateSavingsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SavingsCreateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SavingsCreateRequest request = (SavingsCreateRequest) target;
        // Code validation
        if (request.getCode() == null || request.getCode().length() < 3 || request.getCode().length() > 5) errors.rejectValue("code", request.getCode());
        // Name validation
        if (request.getName() == null || request.getName().length() < 4) errors.rejectValue("name", request.getName());
    }
}
