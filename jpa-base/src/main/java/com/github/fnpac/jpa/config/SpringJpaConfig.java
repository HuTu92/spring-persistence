package com.github.fnpac.jpa.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
@Configuration
public class SpringJpaConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 配置应用程序管理类型的JPA
     * <p>
     * 创建应用程序管理类型的EntityManagerFactory都是在persistence.xml中进行的，而这正是应用程序管理的本意。
     * 在应用程序管理的场景下（不考虑Spring时），完全由应用程序本身来负责获取EntityManagerFactory，
     * 这是通过JPA实现的PersistenceProvider做到的。
     * 如果每次请求EntityManagerFactory时都需要定义持久化单元，那代码将会迅速膨胀。
     * 通过将其配置在persistence.xml中，JPA就能够在这个特定的位置查找持久化单元定义了。
     * <p>
     * 适用于那些仅使用JPA进行数据访问的项目，该FactoryBean将根据JPA PersistenceProvider自动检测配置文件进行工作，一般从"META-INF/persistence.xml"读取配置信息，
     * 这种方式最简单，但不能设置Spring中定义的DataSource，且不支持Spring管理的全局事务，不建议使用这种方式。
     *
     * @return
     */
    @Bean
    public LocalEntityManagerFactoryBean jpaLocalEntityManagerFactory() {
        LocalEntityManagerFactoryBean emfb = new LocalEntityManagerFactoryBean();
        // 指定persistence.xml中的特定持久化单元
        emfb.setPersistenceUnitName("productPersisUnit");
        return emfb;
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        // 配置基于应用程序管理类型的JPA事务管理器
        transactionManager.setEntityManagerFactory(jpaLocalEntityManagerFactory().getObject());

        // 设置用于此事务管理器的JPA方言。方言对象可用于检索底层的JDBC连接，从而允许将JPA事务公开为JDBC事务。
        transactionManager.setJpaDialect(hibernateJpaDialect());
        return transactionManager;
    }

    /**
     * JPA方言
     * <p>
     * 以同时支持JPA/JDBC访问，除了HibernateJpaDialect，其他的还有EclipseLinkJpaDialect、OpenJpaDialect
     * <p>
     * 默认的DefaultJpaDialect不支持，因为其getJdbcConnection()方法返回null
     *
     * @return
     */
    private JpaDialect hibernateJpaDialect() {
        return new HibernateJpaDialect();
    }

    /**
     * {@link PersistenceUnit}和{@link PersistenceContext}并不是Spring的注解，它们是由JPA规范提供的。
     * 为了让Spring理解这些注解，并注入EntityManager Factory或EntityManager，我们必须要配置Spring的PersistenceAnnotationBeanPostProcessor。
     * 如果你已经使用了{@code <context:annotation-config>}或{@code <context:componentscan>}，那么你就不必再担心了，
     * 因为这些配置元素会自动注册PersistenceAnnotationBeanPostProcessor bean。
     * 否则的话，我们需要显式地注册这个bean
     *
     * @return
     */
    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }
}
