package io.github.sudharsan_selvaraj.autowait.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WaitConfig {

    /**
     * Whether to ignore the wait logic on this method.
     */
    boolean ignore() default false;

    /**
     * Timeout in seconds; 0 means no specific timeout set.
     */
    int timeout() default 0;

    /**
     * List of elements to exclude from waiting.
     */
    String[] exclude() default {};
}

