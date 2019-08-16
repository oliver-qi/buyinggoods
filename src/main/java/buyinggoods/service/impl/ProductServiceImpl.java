package buyinggoods.service.impl;

import buyinggoods.mapper.ProductMapper;
import buyinggoods.model.Product;
import buyinggoods.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product findAll(Long id){
        return productMapper.findAll(id);
    }

    @Override
    public Product selectAll(Long id){
        return productMapper.selectAll(id);
    }
}
