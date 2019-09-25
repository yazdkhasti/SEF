package edu.rmit.command.security;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CommandAuthorities.class)
public @interface CommandAuthority {
    String value() default "";
}


