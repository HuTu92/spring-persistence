package com.github.fnpac.jpa.dao;

import com.github.fnpac.jpa.domain.Product;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
public interface ProductDao {

    Product loadProductsById(Long id);

    List<Product> loadProductsByCategory(String category);
}
