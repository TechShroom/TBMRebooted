package com.techshroom.mods.tbm.autogen.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoGenElement {

    String name();

    /**
     * The default {@link #texture()} is {@link #name()}.
     */
    String texture() default "";

    Face[] faces() default {};

    Orientation orientation();

}
