package com.github.fnpac.manager.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by 刘春龙 on 2018/3/5.
 */
@Configuration
@PropertySource("classpath:druid.properties")
/**
 * 启用Spring的注释驱动事务管理功能
 *
 * @EnableTransactionManagement 按类型查找容器中任何PlatformTransactionManager bean
 *
 * 如果同一个容器中存在两个PlatformTransactionManager bean，
 * 实现TransactionManagementConfigurer回调接口为@EnableTransactionManagement指定确切事务管理器bean
 *
 * 此外，如果存在多个数据源，可以使用分布式事务进行管理
 * 项目进行读写分离及分库分表，在一个业务中，在一个事务中处理时候将切换多个数据源，需要保证同一事务多个数据源数据的一致性。可以使用atomikos来实现。
 *
 * 一般不会出现同时使用多个ORM框架的情况（如Hibernate+JPA+JDO）
 *
 * 如果配置多个PlatformTransactionManager bean，会报如下错误：
 * org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type [org.springframework.transaction.PlatformTransactionManager] is defined: expected single matching bean but found 3: jpaTransactionManager,jdoTransactionManager,jdbcTransactionManager
 *
 * 下面有两种处理方式：
 * 1. 通过@Primary指定候选PlatformTransactionManager bean
 * 2. 通过@Transactional的transactionManager属性指定事务管理器，参见 com.github.ittalks.fn.annotation.JdbcTransactional
 */
@EnableTransactionManagement
public class DataSourceConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Value("${spring.datasource.driverClassName}")
    String driver;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.initialSize}")
    int initialSize;
    @Value("${spring.datasource.maxActive}")
    int maxActive;
    @Value("${spring.datasource.maxWait}")
    long maxWait;
    @Value("${spring.datasource.minIdle}")
    int minIdle;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validationQuery}")
    String validationQuery;
    @Value("${spring.datasource.testWhileIdle}")
    boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow}")
    boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn}")
    boolean testOnReturn;
    @Value("${spring.datasource.poolPreparedStatements}")
    boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.connectionProperties}")
    String connectionProperties;
    @Value("${spring.datasource.filters}")
    String filters;

    //===============================================
    // 阿里数据库连接池
    //===============================================
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setDefaultAutoCommit(false);
        // additional
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setConnectionProperties(connectionProperties);
        try {
            druidDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    /**
     * {@link EnableTransactionManagement} 按类型查找容器中任何PlatformTransactionManager
     *
     * @return
     */
    @Bean
    @Primary
    public PlatformTransactionManager jdbcTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }
}
