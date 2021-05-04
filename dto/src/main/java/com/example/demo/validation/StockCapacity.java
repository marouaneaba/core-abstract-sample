package com.example.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CollectionCapacityValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StockCapacity {
    String message() default "Invalid collection capacity";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
