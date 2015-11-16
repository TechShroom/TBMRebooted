package com.techshroom.mods.tbm.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Like {@link SideOnly} + {@link Side#SERVER}, except not stripped on the
 * client...
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CalledOnServerOnly {
}
