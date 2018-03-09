package com.github.fnpac.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by 刘春龙 on 2018/3/5.
 */
@Entity
@Table(name = "t_products")
public class Product {

    @Id
    private long id;
    @Column(name = "p_name")
    private String name;
    @Column(name = "p_price")
    private BigDecimal price;
    @Column(name = "p_category")
    private String category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
