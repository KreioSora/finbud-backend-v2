package com.project.backend.savings.validation.pockets;

import com.project.backend.savings.models.requests.PocketCreateRequest;
import com.project.backend.savings.models.requests.PocketUpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdatePocketValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PocketUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PocketUpdateRequest request = (PocketUpdateRequest) target;
        if (request.getTitle().isEmpty()) errors.rejectValue("title", request.getTitle());
        if (request.getGoalAmount() == 0D) errors.rejectValue("goal_amount", request.getGoalAmount().toString());
    }
}
