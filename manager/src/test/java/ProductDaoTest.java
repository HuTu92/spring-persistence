import com.alibaba.fastjson.JSON;
import com.github.fnpac.hibernate.config.SpringHibernateConfig;
import com.github.fnpac.hibernate.dao.ProductDao;
import com.github.fnpac.hibernate.domain.Product;
import com.github.fnpac.manager.config.WebApplicationConfig;
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
 * Created by 刘春龙 on 2018/3/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
                WebApplicationConfig.class,
                SpringHibernateConfig.class
        })
public class ProductDaoTest {

        private static final Logger logger = LoggerFactory.getLogger(ProductDaoTest.class);

        @Autowired
        private ProductDao productDao;

        @Test
        public void loadProductsByCategory() {
                List<Product> products = productDao.loadProductsByCategory("book");
                logger.info(JSON.toJSONString(products));
        }
}