package buyinggoods.service;

import buyinggoods.model.Product;

/**
 * @author
 */
public interface ProductService {
    /**
     * 使用注解注入SQL语句
     * @param id
     * @return
     */
    Product findAll(Long id);

    /**
     * 使用供应类提供SQL语句
     * @param id
     * @return
     */
    Product selectAll(Long id);
}
