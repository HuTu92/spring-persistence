package com.github.fnpac.jpa.config;

import com.github.fnpac.jpa.config.repository.CustomJpaRepositoryImpl;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
@Configuration
/**
 * Enable spring data JPA
 */
@EnableJpaRepositories(
        basePackages = "com.github.fnpac.jpa.dao",
        entityManagerFactoryRef = "jpaLocalContainerEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager",
//        repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class // Using v1
        repositoryBaseClass = CustomJpaRepositoryImpl.class // Using v2
)
public class SpringJpaConfig extends WebMvcConfigurerAdapter implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * JPA EntityManagerFactory for persistence unit 'default'
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean jpaLocalContainerEntityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.valueOf(environment.getProperty("jpa.database")));
        jpaVendorAdapter.setGenerateDdl(Boolean.parseBoolean(environment.getProperty("jpa.generateDdl")));
        jpaVendorAdapter.setShowSql(Boolean.parseBoolean(environment.getProperty("jpa.showSql")));

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.github.fnpac.jpa");
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        return emf;
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager(DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        // 配置基于容器管理类型的JPA事务管理器
        transactionManager.setEntityManagerFactory(jpaLocalContainerEntityManagerFactory(dataSource).getObject());

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
     * 在web项目中我们经常会遇到在控制器或者页面访问数据时，会遇到会话连接已关闭的错误，这个时候我们会配置OpenEntityManagerInViewInterceptor这个过滤器
     * <p>
     * API：Spring Web请求拦截器，用于将JPA EntityManager绑定到线程以执行整个请求处理。
     * 适用于"Open EntityManager in View"模式，即尽管已完成原始事务，但仍允许在Web视图中进行延迟加载。
     * 该拦截器使JPA EntityManagers可以通过当前线程获取使用，并由事务管理器自动检测。
     * 它适用于通过org.springframework.orm.jpa.JpaTransactionManager或org.springframework.transaction.jta.JtaTransactionManager以及非事务性只读execution的服务层事务。
     * <p>
     * 与OpenEntityManagerInViewFilter相反，这个拦截器是在Spring应用程序上下文中设置的，因此可以利用bean注入。
     */
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        return new OpenEntityManagerInViewInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
    }
}
