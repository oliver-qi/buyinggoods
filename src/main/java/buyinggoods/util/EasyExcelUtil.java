package buyinggoods.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;

import java.io.IOException;
import java.io.InputStream;
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
}
