package com.github.fnpac.jpa.config.repository;

import com.github.fnpac.jpa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by 刘春龙 on 2018/3/13.
 */
@NoRepositoryBean
public interface CustomJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T doSomething(Class<T> type, ID id);
}
