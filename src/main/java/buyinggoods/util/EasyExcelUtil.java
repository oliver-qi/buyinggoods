package buyinggoods.util;

import buyinggoods.util.excel.ExcelParameter;
import buyinggoods.util.excel.MergeParameter;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class EasyExcelUtil {
    /**
     * excel读数据量少于1千行数据自动转成javamodel，内部采用回调方法.
     * (经测试，大于1千行也是可以正常读取的)
     *
     * @param inputStream 输入流
     * @param sheetNo     工作表编号
     * @param headLineMun 标题占用行数
     */
    public static List<Object> simpleReadJavaModel(InputStream inputStream, int sheetNo, int headLineMun, Class<? extends BaseRowModel> clazz) {
        List<Object> data = null;
        try {
            data = EasyExcelFactory.read(inputStream, new Sheet(sheetNo, headLineMun, clazz));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 无模板写excel（当前版本有bug）
     * @param filePath 输出文件位置
     * @param parameter Excel相关参数
     * @param data Excel数据
     * @throws IOException
     */
    public static void writeExcel(String filePath, ExcelParameter parameter, List<List<Object>> data){
        try (OutputStream outputStream = new FileOutputStream(filePath)){
            ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet = new Sheet(parameter.getSheetNo(), parameter.getHeadLineMun());
            sheet.setSheetName(parameter.getSheetName());

            if (parameter.getListStringHead() != null && parameter.getListStringHead().size() > 0) {
                sheet.setHead(parameter.getListStringHead());
            }
            //设置列宽 设置每列的宽度
            if (parameter.getColumnWidth() != null && parameter.getColumnWidth().size() > 0) {
                sheet.setColumnWidthMap(parameter.getColumnWidth());
            } else {
                // 设置自适应宽度
                sheet.setAutoWidth(Boolean.TRUE);
            }
            writer.write1(data, sheet);
            //合并单元格
            if (parameter.getMergeParameterList() != null && parameter.getMergeParameterList().size() > 0) {
                for (MergeParameter mergeParameter : parameter.getMergeParameterList()) {
                    writer.merge(mergeParameter.getFirstRow(), mergeParameter.getLastRow(), mergeParameter.getFirstCol(), mergeParameter.getLastCol());
                }
            }
            writer.finish();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 有模板写excel
     * @param filePath 输出文件位置
     * @param parameter Excel相关参数
     * @param data Excel数据
     * @throws IOException
     */
    public static void writeExcelWithTemplate(String filePath, ExcelParameter parameter, List<? extends BaseRowModel> data){
        try (OutputStream outputStream = new FileOutputStream(filePath)){
            ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
            Class t;
            if (data != null && data.size() > 0) {
                t = data.get(0).getClass();
            } else {
                throw new RuntimeException("文件数据为空");
            }
            Sheet sheet = new Sheet(parameter.getSheetNo(), parameter.getHeadLineMun(), t, parameter.getSheetName(), null);

            //设置列宽 设置每列的宽度
            if (parameter.getColumnWidth() != null && parameter.getColumnWidth().size() > 0) {
                sheet.setColumnWidthMap(parameter.getColumnWidth());
            } else {
                // 设置自适应宽度
                sheet.setAutoWidth(Boolean.TRUE);
            }
            writer.write(data, sheet);
            //合并单元格
            if (parameter.getMergeParameterList() != null && parameter.getMergeParameterList().size() > 0) {
                for (MergeParameter mergeParameter : parameter.getMergeParameterList()) {
                    writer.merge(mergeParameter.getFirstRow(), mergeParameter.getLastRow(), mergeParameter.getFirstCol(), mergeParameter.getLastCol());
                }
            }
            writer.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
