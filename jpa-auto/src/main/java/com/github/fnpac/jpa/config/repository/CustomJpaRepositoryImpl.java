package com.github.fnpac.jpa.config.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by 刘春龙 on 2018/3/13.
 */
public class CustomJpaRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements CustomJpaRepository<T, ID> {

    private static Logger logger = LoggerFactory.getLogger(CustomJpaRepositoryImpl.class);

    private EntityManager entityManager;

    /**
     * v1
     *
     * 使用{@link CustomJpaRepositoryFactoryBean}实现自定义Repository
     *
     * @param domainClass
     * @param em
     */
    public CustomJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        logger.info("Using v1");
        this.entityManager = em;
    }

    /**
     * v2
     *
     * 不依赖{@link CustomJpaRepositoryFactoryBean}实现自定义Repository
     *
     * @param entityInformation
     * @param entityManager
     */
    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        logger.info("Using v2");
        this.entityManager = entityManager;
    }

    @Override
    public T doSomething(Class<T> type, ID id) {
        return this.entityManager.find(type, id);
    }
}
