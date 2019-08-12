package buyinggoods.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * t_purchase_record
 * @author 
 */
@Alias("purchaseRecord")
@Data
public class PurchaseRecord implements Serializable {
    /**
     * '编号'
     */
    private Long id;

    /**
     * '用户编号'
     */
    private Long userId;

    /**
     * '产品编号'
     */
    private Long productId;

    /**
     * '价格'
     */
    private BigDecimal price;

    /**
     * '数量'
     */
    private Integer quantity;

    /**
     * '总价'
     */
    private BigDecimal sum;

    /**
     * '购买日期'
     */
    private Date purchaseDate;

    /**
     * '备注'
     */
    private String note;

    private static final long serialVersionUID = 1L;


}