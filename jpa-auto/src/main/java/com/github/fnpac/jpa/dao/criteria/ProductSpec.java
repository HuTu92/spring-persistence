package com.github.fnpac.jpa.dao.criteria;

import com.github.fnpac.jpa.domain.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
public class ProductSpec {

    public static Specification<Product> findByCategory(String category) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("category"), category);
            }
        };
    }
}
