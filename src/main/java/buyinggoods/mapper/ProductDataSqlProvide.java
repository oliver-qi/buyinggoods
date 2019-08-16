package buyinggoods.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

/**
 * @author
 */
public class ProductDataSqlProvide {

    public String selectAll(Long id){
        BEGIN();
        SELECT("id");
        SELECT("product_name");
        SELECT("stock");
        SELECT("price");
        SELECT("version");
        SELECT("note");
        FROM("t_product");
        WHERE("id = #{id}");
        return SQL();
    }
}
