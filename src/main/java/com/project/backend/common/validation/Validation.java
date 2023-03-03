package com.project.backend.common.validation;

import com.project.backend.common.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class Validation {

    public void validateRequest(Object request, String name, Validator validator) throws ValidationException {
        Errors validation = new BeanPropertyBindingResult(request, name);
        validator.validate(request, validation);
        if (validation.hasErrors()) throw new ValidationException("Invalid Request", validation);
    }

}
