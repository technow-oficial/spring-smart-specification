package io.github.technowoficial.springsmartspecification.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SmartSpecificationField {

    String path() default "";

    String condition() default "";

    String customMethod() default "";

}
