package org.dragon.aries.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AriesReference {
    String version() default "default";

    int retry() default 0;

    long retryInterval() default 0;

    long timeout() default 2000;
}
