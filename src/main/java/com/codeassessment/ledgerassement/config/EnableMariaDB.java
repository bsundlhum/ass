package com.codeassessment.ledgerassement.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings({"WeakerAccess", "unused"})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MariaDBJavaConfigurationImportSelector.class})
public @interface EnableMariaDB {
    boolean forTenantContext() default true;
}
