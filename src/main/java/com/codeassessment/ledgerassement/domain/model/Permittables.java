package com.codeassessment.ledgerassement.domain.model;

import java.lang.annotation.*;

/**
 * @author Myrle Krantz
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permittables {
    Permittable[] value();
}
