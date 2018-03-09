package com.github.fnpac.jpa.dao;
import com.github.fnpac.jpa.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(Long id);

    List<Product> findByCategory(String category);
}
