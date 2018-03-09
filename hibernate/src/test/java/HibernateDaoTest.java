import com.alibaba.fastjson.JSON;
import com.github.fnpac.config.DataSourceConfig;
import com.github.fnpac.config.WebApplicationConfig;
import com.github.fnpac.hibernate.config.SpringHibernateConfig;
import com.github.fnpac.hibernate.dao.ProductDao;
import com.github.fnpac.hibernate.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
                WebApplicationConfig.class,
                DataSourceConfig.class,
                SpringHibernateConfig.class
        })
public class HibernateDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(HibernateDaoTest.class);

    @Autowired
    private ProductDao productDao;

    @Test
    public void loadProductsByCategory() {
        List<Product> products = productDao.loadProductsByCategory("计算机图书");
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void loadProductsByCategoryUsingHibernateTemplate() {
        List<Product> products = productDao.loadProductsByCategoryUsingHibernateTemplate("计算机图书");
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void loadProductsByCategoryUsingJdbcTemplate() {
        List<Product> products = productDao.loadProductsByCategoryUsingJdbcTemplate("计算机图书");
        logger.info(JSON.toJSONString(products));
    }

    @Test
    public void rollbackTest() {
        productDao.updateProductsByCategory("计算机图书");
    }
}
