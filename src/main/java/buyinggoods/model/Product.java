package buyinggoods.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * t_product
 * @author 
 */
@Alias("product")
@Data
public class Product implements Serializable {
    /**
     * '编号'
     */
    private Long id;
    /**
     * '产品名称'
     */
    private String productName;

    /**
     * '库存'
     */
    private Integer stock;

    /**
     * '单价'
     */
    private BigDecimal price;

    /**
     * '版本号'
     */
    private Integer version;

    /**
     * '备注'
     */
    private String note;

    private static final long serialVersionUID = 1L;

}