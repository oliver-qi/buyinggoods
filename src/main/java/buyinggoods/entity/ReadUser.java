package buyinggoods.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadUser extends BaseRowModel {
    @ExcelProperty(value = "用户id", index = 0)
    @Setter@Getter
    private String id;

    @ExcelProperty(value = "姓名", index = 1)
    @Setter@Getter
    private String name;

    @ExcelProperty(value = "年龄", index = 2)
    @Setter@Getter
    private int age;

    @ExcelProperty(value = "生日", index = 3, format = "yyyy/MM/dd")
    @Setter@Getter
    private Date birthday;
}
