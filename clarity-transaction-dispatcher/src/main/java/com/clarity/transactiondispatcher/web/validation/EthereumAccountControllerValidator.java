package com.clarity.transactiondispatcher.web.validation;

import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

@Validated
@Slf4j
@Component
public class EthereumAccountControllerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "password", "password empty");
        AccountRequestDTO account = (AccountRequestDTO) target;
        if (!account.getPassword().matches("/(?=.*[a-z]+)(?=.*[0-9]+)(?=.*[A-Z]+)(?=.*[!@#$%^&*()_+\\[\\]{}:\\\\\";,.<>?|=-_]+).{8,20}/")) {
            errors.rejectValue("password", "Password must contain at least one uppercase letter, one lowercase letter and one special character");
        }
    }
}
