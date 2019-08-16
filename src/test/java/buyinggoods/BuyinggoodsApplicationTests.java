package buyinggoods;

import buyinggoods.model.Product;
import buyinggoods.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyinggoodsApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    public void contextLoads() {
        Product product = productService.findAll(1L);
        System.out.println(product);
    }

    @Test
    public void test() {
        Product product = productService.selectAll(2L);
        System.out.println(product);
    }

}
