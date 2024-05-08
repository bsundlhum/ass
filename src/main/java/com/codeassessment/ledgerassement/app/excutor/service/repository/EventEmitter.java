package com.codeassessment.ledgerassement.app.excutor.service.repository;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface EventEmitter {

    String selectorName();

    String selectorValue();
}
