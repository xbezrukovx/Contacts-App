package ru.codemeup.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, })
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageMethod {
    String value();
    String[] args() default {};
    String help() default "";
}
