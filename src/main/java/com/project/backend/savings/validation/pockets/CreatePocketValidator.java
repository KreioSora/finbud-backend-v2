package com.project.backend.savings.validation.pockets;

import com.project.backend.savings.models.requests.PocketCreateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreatePocketValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PocketCreateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PocketCreateRequest request = (PocketCreateRequest) target;
        if (request.getTitle() == null || request.getTitle().isEmpty()) errors.rejectValue("title", request.getTitle());
        if (request.getCurrency() == null || request.getCurrency().isEmpty()) errors.rejectValue("title", request.getTitle());
        if (request.getGoalAmount() == null || request.getGoalAmount() == 0D) errors.rejectValue("goal_amount", request.getGoalAmount().toString());
    }
}
