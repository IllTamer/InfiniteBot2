package me.illtamer.infinitebot.bot.event;

import java.lang.annotation.*;

/**
 * 监听方法注解
 * */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    Priority priority() default Priority.DEFAULT;

}
