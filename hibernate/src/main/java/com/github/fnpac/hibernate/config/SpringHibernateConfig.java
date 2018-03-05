package com.github.fnpac.hibernate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by 刘春龙 on 2018/3/5.
 * <p>
 * 在Spring中集成hibernate
 */
@Configuration
public class SpringHibernateConfig {

    /**
     * 如果你使用Hibernate 4的话，那么就应该使用org.springframework.orm.hibernate4中的LocalSessionFactoryBean。
     * 尽管它与Hibernate 3包中的LocalSessionFactoryBean使用了相同的名称，
     * 但是Spring 3.1新引入的这个Session工厂类似于Hibernate 3中LocalSessionFactoryBean和AnnotationSessionFactoryBean的结合体。
     *
     * @param dataSource 数据源
     * @return
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setPackagesToScan(new String[]{"com.github.fnpac.hibernate.domain"});
        Properties props = new Properties();
        // 数据库方言，特定的关系数据库生成优化的SQL
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // 是否在控制台打印sql语句
        props.setProperty("hibernate.show_sql", "true");
        // 输出格式化后的sql，更方便查看
        props.setProperty("hibernate.format_sql", "true");
        // 把事务交给Spring管理
        props.setProperty("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        sfb.setHibernateProperties(props);
        return sfb;
    }
}
