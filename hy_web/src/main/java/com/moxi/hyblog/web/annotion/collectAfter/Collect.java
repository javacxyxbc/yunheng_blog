package com.moxi.hyblog.web.annotion.collectAfter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/24 11:41
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface Collect {
    String value() default "";
}
