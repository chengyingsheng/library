package com.cheng.application.validator;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * JSR303
 */
public class ValidatorToolUtil {

    private static ValidatorToolUtil validatorToolUtil = new ValidatorToolUtil();
    private Validator validator = null;

    private ValidatorToolUtil() {

    }

    public static ValidatorToolUtil getInstance() {
        if (validatorToolUtil == null) {
            validatorToolUtil = new ValidatorToolUtil();
        }
        return validatorToolUtil;
    }

    public String validate(Object object) {
        if (validator == null) {
            validator = getValidator();
        }
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            return constraintViolation.getPropertyPath().toString() + ":" + constraintViolation.getMessage();
        }
        return "";
    }

    private Validator getValidator() {
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(true).buildValidatorFactory().getValidator();
        return validator;
    }
}
