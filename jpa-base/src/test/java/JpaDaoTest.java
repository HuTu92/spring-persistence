import com.alibaba.fastjson.JSON;
import com.github.fnpac.config.DataSourceConfig;
import com.github.fnpac.config.WebApplicationConfig;
import com.github.fnpac.jpa.config.SpringJpaConfig;
import com.github.fnpac.jpa.dao.ProductDao;
import com.github.fnpac.jpa.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JpaDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(JpaDaoTest.class);

    @Autowired
    private ProductDao productDao;

    @Test
    public void loadProductsById() {
        Product product = productDao.loadProductsById(1L);
        logger.info(JSON.toJSONString(product));
    }

    @Test
    public void loadProductsByCategory() {
        List<Product> products = productDao.loadProductsByCategory("计算机图书");
        logger.info(JSON.toJSONString(products));
    }

}
