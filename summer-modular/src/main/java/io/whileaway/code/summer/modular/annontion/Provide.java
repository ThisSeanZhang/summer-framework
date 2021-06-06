package io.whileaway.code.summer.modular.annontion;

import io.whileaway.code.summer.modular.ModuleDefine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Provide {

    Class<? extends ModuleDefine> value();
}
