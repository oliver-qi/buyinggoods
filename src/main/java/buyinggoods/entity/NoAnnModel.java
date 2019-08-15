package buyinggoods.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoAnnModel extends BaseRowModel {
    @ExcelProperty(index = 0)
    private String p1;

    @ExcelProperty(index = 1)
    private String p2;

    @ExcelProperty(index = 2)
    private String p3;
}
