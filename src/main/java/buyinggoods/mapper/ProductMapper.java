package buyinggoods.mapper;

import buyinggoods.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    /**
     * 获取产品
     * @param id
     * @return
     */
    Product getProduct(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 减少库存
     * @param id
     * @param quantity
     * @return
     */
    int decreaseProduct(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("version") Integer version);

    /**
     *
     * @param id
     * @param quantity
     * @return
     */
    int deleteProduct(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     *
     * @param id
     * @return
     */
    @Select("select * from t_product where id = #{id}")
    Product findAll(Long id);

    /**
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProductDataSqlProvide.class, method = "selectAll")
    Product selectAll(Long id);
}