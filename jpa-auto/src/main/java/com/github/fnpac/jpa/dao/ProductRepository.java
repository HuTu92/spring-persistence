package com.github.fnpac.jpa.dao;

import com.github.fnpac.jpa.config.repository.CustomJpaRepository;
import com.github.fnpac.jpa.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/9.
 *
 * 1. Specification - 自定义查询准则，继承JpaSpecificationExecutor接口,从而具备该接口提供的通过Specification参数查询的方法
 * 2. 排序和分页
 */
@Repository
public interface ProductRepository extends CustomJpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findById(Long id);

    List<Product> findByCategory(String category);

    List<Product> findByCategory(String category, Sort sort);

    Page<Product> findByCategory(String category, Pageable pageable);
}
