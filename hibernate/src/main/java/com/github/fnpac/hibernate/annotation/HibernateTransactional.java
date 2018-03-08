package com.github.fnpac.hibernate.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Created by 刘春龙 on 2018/3/8.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional("hibernateTransactionManager")
public @interface HibernateTransactional {
}
