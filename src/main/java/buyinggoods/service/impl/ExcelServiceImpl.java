package buyinggoods.service.impl;

import buyinggoods.entity.MultiLineHeadExcelModel;
import buyinggoods.service.ExcelService;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public void uplaodExcel(String filePath){
        try (OutputStream out = new FileOutputStream(filePath)) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            //写sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("第一个sheet");
            Table table2 = new Table(2);
            table2.setClazz(MultiLineHeadExcelModel.class);
            List<MultiLineHeadExcelModel> data = new ArrayList<>();
            for (int i=0;i<150;i++){
                data.add(new MultiLineHeadExcelModel(i+"-1",i+"-2",i+"-3",i+"-4",i+"-5",i+"-6",i+"-7",i+"-8",i+"-9"));
            }
            writer.write(data, sheet1, table2);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
