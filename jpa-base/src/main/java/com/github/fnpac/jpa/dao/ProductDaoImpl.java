package com.github.fnpac.jpa.dao;

import com.github.fnpac.jpa.domain.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/9.
 * <p>
 * 在ProductDaoImpl中，唯一的问题在于每个方法都会调用createEntityManager()。
 * 除了引入易出错的重复代码以外，这还意味着每次调用Repository的方法时，都会创建一个新的EntityManager。
 * 这种复杂性源于事务。如果我们能够预先准备好EntityManager，那会不会更加方便呢？
 * 这里的问题在于EntityManager并不是线程安全的，一般来讲并不适合注入到像Repository这样共享的单例bean中。
 * 但是，这并不意味着我们没有办法要求注入EntityManager。
 * 借助@PersistentContext注解为ProductDaoImpl设置EntityManager。
 * <p>
 * 这样的话，在每个方法中就没有必要再通过EntityManagerFactory创建EntityManager了。
 * 尽管这种方式非常便利，但是你可能会担心注入的EntityManager会有线程安全性的问题。
 * 这里的真相是@PersistenceContext并不会真正注入EntityManager——至少，精确来讲不是这样的。
 * 它没有将真正的EntityManager设置给Repository，而是给了它一个EntityManager的代理。
 * 真正的EntityManager是与当前事务相关联的那一个，如果不存在这样的EntityManager的话，就会创建一个新的。
 * 这样的话，我们就能始终以线程安全的方式使用实体管理器
 * <p>
 * TODO Issues Caused by: java.lang.UnsupportedOperationException: The application must supply JDBC connections
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product loadProductsById(Long id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> loadProductsByCategory(String category) {
        return entityManagerFactory.createEntityManager()
                .createQuery("from Product p where p.category = :category")
                .setParameter("category", category)
                .getResultList();
    }
}
