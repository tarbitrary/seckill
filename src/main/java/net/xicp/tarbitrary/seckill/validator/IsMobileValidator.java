package net.xicp.tarbitrary.seckill.validator;

import net.xicp.tarbitrary.seckill.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, Object> {
    @Override
    public void initialize(IsMobile constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String mobile = String.valueOf(value);
        final boolean isMobile = ValidatorUtil.isMobile(mobile);
        return isMobile;
    }
}
