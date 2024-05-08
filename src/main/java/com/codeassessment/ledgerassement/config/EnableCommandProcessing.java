package com.codeassessment.ledgerassement.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CommandModuleConfiguration.class})
public @interface EnableCommandProcessing {

}
