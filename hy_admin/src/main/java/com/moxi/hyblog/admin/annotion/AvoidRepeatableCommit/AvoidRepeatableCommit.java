package com.moxi.hyblog.admin.annotion.AvoidRepeatableCommit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 避免重复提交
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatableCommit {
    /**
     * 指定时间内不可重复提交,单位毫秒，默认1秒
     */
    long timeout() default 1000;
}
