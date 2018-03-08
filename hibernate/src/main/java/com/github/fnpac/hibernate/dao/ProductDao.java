package com.github.fnpac.hibernate.dao;

import com.github.fnpac.hibernate.domain.Product;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/5.
 */
public interface ProductDao {

    List<Product> loadProductsByCategory(String category);

    List<Product> loadProductsByCategoryByHibernateTemplate(String category);

    List<Product> loadProductsByCategoryByJdbcTemplate(String category);

    void updateProductsByCategory(String category);
}
