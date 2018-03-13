import com.alibaba.fastjson.JSON;
import com.github.fnpac.config.DataSourceConfig;
import com.github.fnpac.config.WebApplicationConfig;
import com.github.fnpac.jpa.config.SpringJpaConfig;
import com.github.fnpac.jpa.dao.ProductRepository;
import com.github.fnpac.jpa.dao.criteria.ProductSpec;
import com.github.fnpac.jpa.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
                WebApplicationConfig.class,
                DataSourceConfig.class,
                SpringJpaConfig.class
        })
public class JpaRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(JpaRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findById() {
        Product product = productRepository.findById(1L);
        logger.info(JSON.toJSONString(product));
    }

    /*

        [
            {
                "category":"计算机图书",
                "id":1,
                "name":"Java编程思想",
                "price":111
            },
            {
                "category":"计算机图书",
                "id":5,
                "name":"Spring 实战",
                "price":123
            },
            {
                "category":"计算机图书",
                "id":4,
                "name":"Spring Boot 实战",
                "price":234
            },
            {
                "category":"计算机图书",
                "id":2,
                "name":"Spring 实战",
                "price":512
            }
        ]

     */
    @Test
    public void findByCategoryAndSort() {
        List<Product> products = productRepository.findByCategory("计算机图书", new Sort(Sort.Direction.ASC, "price"));
        logger.info(JSON.toJSONString(products));
    }

    /*
        {
            "content":[
                {
                    "category":"计算机图书",
                    "id":1,
                    "name":"Java编程思想",
                    "price":111
                },
                {
                    "category":"计算机图书",
                    "id":2,
                    "name":"Spring 实战",
                    "price":512
                }
            ],
            "first":true,
            "last":false,
            "number":0,
            "numberOfElements":2,
            "size":2,
            "totalElements":4,
            "totalPages":2
        }
     */
    @Test
    public void findByCategoryAndPageable() {
        // return the first page.
        Page<Product> products = productRepository.findByCategory("计算机图书", new PageRequest(0, 2));
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void findBySpec() {
        List<Product> products = productRepository.findAll(ProductSpec.findByCategory("计算机图书"));
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void findByCustom() {
        List<Product> products = productRepository.findAll(ProductSpec.findByCategory("计算机图书"));
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void findByCategory() {
        Product product = productRepository.doSomething(Product.class, 2L);
        logger.info(JSON.toJSONString(product));
    }
}
