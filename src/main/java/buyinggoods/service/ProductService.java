package buyinggoods.service;

import buyinggoods.model.Product;

public interface ProductService {
    /**
     *
     * @param id
     * @return
     */
    Product findAll(Long id);

    /**
     *
     * @param id
     * @return
     */
    Product selectAll(Long id);
}
