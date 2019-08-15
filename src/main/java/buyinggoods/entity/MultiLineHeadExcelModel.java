package buyinggoods.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultiLineHeadExcelModel extends BaseRowModel {
    @ExcelProperty(value = {"表头1","表头1","表头11"},index = 0)
    private String p1;

    @ExcelProperty(value = {"表头1","表头1","表头12"},index = 1)
    private String p2;

    @ExcelProperty(value = {"表头2","表头2","表头2"},index = 2)
    private String p3;

    @ExcelProperty(value = {"表头3","表头3","表头3"},index = 3)
    private String p4;

    @ExcelProperty(value = {"表头4","表头41","表头41"},index = 4)
    private String p5;

    @ExcelProperty(value = {"表头5","表头51","表头511"},index = 5)
    private String p6;

    @ExcelProperty(value = {"表头5","表头51","表头512"},index = 6)
    private String p7;

    @ExcelProperty(value = {"表头5","表头52","表头521"},index = 7)
    private String p8;

    @ExcelProperty(value = {"表头5","表头52","表头522"},index = 8)
    private String p9;
}
