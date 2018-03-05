package com.github.fnpac.hibernate.dao;

import com.github.fnpac.hibernate.domain.Product;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/5.
 * <p>
 * 在Spring和Hibernate的早期岁月中，编写Repository类将会涉及到使用Spring的HibernateTemplate。
 * HibernateTemplate能够保证每个事务使用同一个Session。但是这种方式的弊端在于我们的Repository实现会直接与Spring耦合
 * <p>
 * 最佳实践：不再使用HibernateTemplate，而是使用上下文Session（Contextual session）。
 * 通过这种方式，会直接将Hibernate SessionFactory装配到Repository中，并使用它来获取Session
 * <p>
 * Hibernate上下文Session而不是Hibernate模板的话， 那异常转换会怎么处理呢？
 * 为了给不使用模板的Hibernate Repository添加异常转换功能，我们只需在Spring应用上下文中添加一个PersistenceExceptionTranslationPostProcessor bean
 * <p>
 * PersistenceExceptionTranslationPostProcessor是一个bean 后置处理器（bean post-processor），
 * 它会在所有拥有@Repository注解的类上添加一个通知器（advisor） ，
 * 这样就会捕获任何平台相关的异常并以Spring非检查型数据访问异常的形式重新抛出
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    /**
     * 1.getCurrentSession()与openSession()的区别？
     * <p>
     * - 采用getCurrentSession()创建的session会绑定到当前线程中，而采用openSession()创建的session则不会
     * 为了保证一个线程一个Session，即一个线程中使用的Session是同一个对象，一般在获取Session对象时，使用SessionFactory的getCurrentSession()方法。
     * <p>
     * - 采用getCurrentSession()创建的session在commit或rollback时会{@code 自动关闭}，而采用openSession()创建的session必须手动关闭
     * <p>
     * 2.使用getCurrentSession()需要在hibernate.cfg.xml文件中加入如下配置：
     * <p>
     * - 如果使用的是本地事务（jdbc事务） - {@code <property name="hibernate.current_session_context_class">thread</property>}
     * <p>
     * - 如果使用的是全局事务（jta事务） - {@code <property name="hibernate.current_session_context_class">jta</property> }
     * <p>
     * - 把事务交给Spring管理 - {@code <property name="hibernate.current_session_context_class">org.springframework.orm.hibernate5.SpringSessionContext</property>}
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public ProductDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @param category
     * @return
     */
    @Override
    @Transactional
    public List<Product> loadProductsByCategory(String category) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("from Product p where p.category = ?", Product.class)
                .setParameter("category", category).list();
    }
}
