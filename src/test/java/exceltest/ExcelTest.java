package exceltest;

import buyinggoods.Application;
import buyinggoods.entity.MultiLineHeadExcelModel;
import buyinggoods.entity.NoAnnModel;
import buyinggoods.entity.ReadUser;
import buyinggoods.listener.ExcelListener;
import buyinggoods.util.EasyExcelUtil;
import buyinggoods.util.excel.ExcelParameter;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Cleanup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ExcelTest {

    @Autowired
    ExcelListener excelListener;

    /**
     * 读取文件内容
     */
    @Test
    public void test(){
        try {
//            InputStream inputStream = ExcelTest.class.getClassLoader().getResourceAsStream("exceltest/qxw.xlsx");
            InputStream inputStream = new FileInputStream("E:\\data\\qxw.xlsx");
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 0), excelListener);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对较少的记录数(20W以内大概)可以调用该方法一次性查出然后写入到EXCEL的一个SHEET中
     * 注意： 一次性查询出来的记录数量不宜过大，不会内存溢出即可。
     *
     * @throws IOException
     */
    @Test
    public void testWrite1() throws IOException{
        // 生成EXCEL并指定输出路径
        @Cleanup
        OutputStream out = new FileOutputStream("E:\\data\\qxw2.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

        // 设置SHEET
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("sheet1");

        // 设置标题
        Table table = new Table(1);
        List<List<String>> titles = new ArrayList<>();
        titles.add(Arrays.asList("用户ID"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        table.setHead(titles);

        // 查询数据导出即可 比如说一次性总共查询出100条数据
        List<List<String>> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            userList.add(Arrays.asList("ID_" + i, "小明" + i, String.valueOf(i), new Date().toString()));
        }


        // 设置SHEET
        Sheet sheet2 = new Sheet(2, 0);
        sheet2.setSheetName("sheet2");
        // 查询数据导出即可 比如说一次性总共查询出100条数据
        List<List<String>> userList2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            userList2.add(Arrays.asList("ID_" + i, "小明" + i, String.valueOf(i), new Date().toString()));
        }

        writer.write0(userList, sheet, table);
        writer.write0(userList2, sheet2, table);
        writer.finish();
    }

    /**
     * 针对105W以内的记录数可以调用该方法分多批次查出然后写入到EXCEL的一个SHEET中
     * 注意：
     * 每次查询出来的记录数量不宜过大，根据内存大小设置合理的每次查询记录数，不会内存溢出即可。
     * 数据量不能超过一个SHEET存储的最大数据量105W
     * @throws IOException
     */
    @Test
    public void testWrite2() throws IOException{
        // 生成EXCEL并指定输出路径
        @Cleanup
        OutputStream out = new FileOutputStream("E:\\data\\qxw3.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

        // 设置SHEET
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName("sheet1");

        // 设置标题
        Table table = new Table(1);
        List<List<String>> titles = new ArrayList<>();
        titles.add(Arrays.asList("用户ID","111"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        table.setHead(titles);

        // 模拟分批查询：总记录数50条，每次查询20条，  分三次查询 最后一次查询记录数是10
        Integer totalRowCount = 50;
        Integer pageSize = 20;
        Integer writeCount = totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize + 1);

        // 注： 此处仅仅为了模拟数据，实用环境不需要将最后一次分开，合成一个即可， 参数为： currentPage = i+1;  pageSize = pageSize
        for (int i = 0; i < writeCount; i++) {

            // 前两次查询 每次查20条数据
            if (i < writeCount - 1) {

                List<List<String>> userList = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
                }
                writer.write0(userList, sheet, table);

            } else if (i == writeCount - 1) {

                // 最后一次查询 查多余的10条记录
                List<List<String>> userList = new ArrayList<>();
                Integer lastWriteRowCount = totalRowCount - (writeCount - 1) * pageSize;
                for (int j = 0; j < lastWriteRowCount; j++) {
                    userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
                }
                writer.write0(userList, sheet, table);
            }
        }
        writer.finish();
    }

    /**
     * 针对几百万的记录数可以调用该方法分多批次查出然后写入到EXCEL的多个SHEET中
     * 注意：
     * perSheetRowCount % pageSize要能整除  为了简洁，非整除这块不做处理
     * 每次查询出来的记录数量不宜过大，根据内存大小设置合理的每次查询记录数，不会内存溢出即可。
     *
     * @throws IOException
     */
    @Test
    public void testWrite3() throws IOException {

        // 生成EXCEL并指定输出路径
        @Cleanup
        OutputStream out = new FileOutputStream("E:\\data\\qxw4.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);

        // 设置SHEET名称
        String sheetName = "测试SHEET";

        // 设置标题
        Table table = new Table(1);
        List<List<String>> titles = new ArrayList<>();
        titles.add(Arrays.asList("用户ID"));
        titles.add(Arrays.asList("名称"));
        titles.add(Arrays.asList("年龄"));
        titles.add(Arrays.asList("生日"));
        table.setHead(titles);

        // 模拟分批查询：总记录数250条，每个SHEET存100条，每次查询20条  则生成3个SHEET，前俩个SHEET查询次数为5， 最后一个SHEET查询次数为3 最后一次写的记录数是10
        // 注：该版本为了较少数据判断的复杂度，暂时perSheetRowCount要能够整除pageSize， 不去做过多处理  合理分配查询数据量大小不会内存溢出即可。
        Integer totalRowCount = 250;
        Integer perSheetRowCount = 100;
        Integer pageSize = 20;
        Integer sheetCount = totalRowCount % perSheetRowCount == 0 ? (totalRowCount / perSheetRowCount) : (totalRowCount / perSheetRowCount + 1);
        Integer previousSheetWriteCount = perSheetRowCount / pageSize;
        Integer lastSheetWriteCount = totalRowCount % perSheetRowCount == 0 ?
                previousSheetWriteCount :
                (totalRowCount % perSheetRowCount % pageSize == 0 ? totalRowCount % perSheetRowCount / pageSize : (totalRowCount % perSheetRowCount / pageSize + 1));
        for (int i = 0; i < sheetCount; i++) {
            // 创建SHEET
            Sheet sheet = new Sheet(i+1, 0);
            sheet.setSheetName(sheetName + i);
            if (i < sheetCount - 1) {
                // 前2个SHEET, 每个SHEET查5次 每次查20条 每个SHEET写满100行  2个SHEET合计200行  实用环境：参数： currentPage: j+1 + previousSheetWriteCount*i, pageSize: pageSize
                for (int j = 0; j < previousSheetWriteCount; j++) {
                    List<List<String>> userList = new ArrayList<>();
                    for (int k = 0; k < 20; k++) {
                        userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
                    }
                    writer.write0(userList, sheet, table);
                }
            } else if (i == sheetCount - 1) {
                // 最后一个SHEET 实用环境不需要将最后一次分开，合成一个即可， 参数为： currentPage = i+1;  pageSize = pageSize
                for (int j = 0; j < lastSheetWriteCount; j++) {
                    // 前俩次查询 每次查询20条
                    if (j < lastSheetWriteCount - 1) {
                        List<List<String>> userList = new ArrayList<>();
                        for (int k = 0; k < 20; k++) {
                            userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
                        }
                        writer.write0(userList, sheet, table);
                    } else if (j == lastSheetWriteCount - 1) {
                        // 最后一次查询 将剩余的10条查询出来
                        List<List<String>> userList = new ArrayList<>();
                        Integer lastWriteRowCount = totalRowCount - (sheetCount - 1) * perSheetRowCount - (lastSheetWriteCount - 1) * pageSize;
                        for (int k = 0; k < lastWriteRowCount; k++) {
                            userList.add(Arrays.asList("ID_" + Math.random(), "小明1", String.valueOf(Math.random()), new Date().toString()));
                        }
                        writer.write0(userList, sheet, table);

                    }
                }
            }
        }
        writer.finish();
    }

    /**
     * 有模型读取Excel表格
     */
    @Test
    public void testReadJavaModel() throws IOException{
        InputStream is = new FileInputStream("E://data//qxw2.xlsx");
        List<Object> objects = EasyExcelUtil.simpleReadJavaModel(is, 1, 1, ReadUser.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        objects.forEach(object->{
//            if (object instanceof ReadUser) {
//                ReadUser readUser = (ReadUser)object;
//                StringBuilder sb = new StringBuilder();
//                sb.append(readUser.getId()).append("\t").append(readUser.getName())
//                        .append("\t").append(readUser.getAge()).append("\t")
//                        .append(sdf.format(readUser.getBirthday()));
//                System.out.println(sb);
//            }
//        });
        List<ReadUser> readUserList = objects.stream().map(object -> {
            ReadUser readUser = null;
            if (object instanceof ReadUser) {
                readUser = (ReadUser) object;
                StringBuilder sb = new StringBuilder();
                sb.append(readUser.getId()).append("\t").append(readUser.getName())
                        .append("\t").append(readUser.getAge()).append("\t")
                        .append(sdf.format(readUser.getBirthday()));
                System.out.println(sb);
            }
            return readUser;
        }).collect(Collectors.toList());
    }

    /**
     * 无模板写Excel表格
     */
    @Test
    public void testWriteExcel(){
        String filePath = "E://data//writer_excel.xlsx";
        ExcelParameter parameter = new ExcelParameter();
        parameter.setSheetNo(1);
        parameter.setSheetName("test1");
//        parameter.setHeadLineMun(1);
        Map<Integer, Integer> columnWidthMap = new HashMap<>();
        columnWidthMap.put(0,4000);
        columnWidthMap.put(1,3000);
        columnWidthMap.put(2,2000);
        columnWidthMap.put(3,8000);
        parameter.setColumnWidth(columnWidthMap);
        List<List<String>> listStringHead = new ArrayList<>();
        listStringHead.add(Arrays.asList("用户ID"));
        listStringHead.add(Arrays.asList("姓名"));
        listStringHead.add(Arrays.asList("年龄"));
        listStringHead.add(Arrays.asList("生日"));
        parameter.setListStringHead(listStringHead);
        List<List<Object>> data = new ArrayList<>();
        for (int i=0;i<4;i++){
            data.add(Arrays.asList("ID_"+i, "小明", i+"", new Date().toString()));
        }
        EasyExcelUtil.writeExcel(filePath, parameter, data);
    }

    /**
     * 有模板写Excel表格
     */
    @Test
    public void testWriteJavaModel(){
        String filePath = "E://data//writerJavaModel_excel.xlsx";
        ExcelParameter parameter = new ExcelParameter();
        parameter.setSheetNo(1);
        parameter.setSheetName("test1");
//        parameter.setHeadLineMun(1);
        Map<Integer, Integer> columnWidthMap = new HashMap<>();
        columnWidthMap.put(0,4000);
        columnWidthMap.put(1,3000);
        columnWidthMap.put(2,2000);
        columnWidthMap.put(3,8000);
        parameter.setColumnWidth(columnWidthMap);
        List<ReadUser> readUserList = new ArrayList<>();
        for (int i=0;i<4;i++){
            readUserList.add(new ReadUser("ID_"+i, "小明", i, new Date(), "多余"+i));
        }
        EasyExcelUtil.writeExcelWithTemplate(filePath, parameter, readUserList);
    }

    /**
     * 多表头模型Excel表
     */
    @Test
    public void testMultiLineHeadExcelModel(){
        String filePath = "E://data//MultiLineHead_excel.xlsx";
        ExcelParameter parameter = new ExcelParameter();
        parameter.setSheetNo(1);
        parameter.setSheetName("test1");
        List<MultiLineHeadExcelModel> multiLineHeadList = new ArrayList<>();
        for (int i=0;i<100000;i++){
            multiLineHeadList.add(new MultiLineHeadExcelModel(i+"-1",i+"-2",i+"-3",i+"-4",i+"-5",i+"-6",i+"-7",i+"-8",i+"-9"));
        }
        EasyExcelUtil.writeExcelWithTemplate(filePath, parameter, multiLineHeadList);
    }

    /**
     * 无模型读Excel表格
     * 抛弃了许多不需要的信息(如：样式...)
     */
    @Test
    public void noModelMultipleSheet() {
//        String filePath = "E://data//modelWrite.xlsx";
        String filePath = "E://data//tables.xlsx";
        try (InputStream inputStream = new FileInputStream(filePath)){

            ExcelReader reader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null,
                    new AnalysisEventListener<List<String>>() {
                        int i = 0;
                        @Override
                        public void invoke(List<String> object, AnalysisContext context) {
                            System.out.println("当前sheet:" + context.getCurrentSheet().getSheetNo() + " 当前行：" +
                                    context.getCurrentRowNum() + " data:" + object);
                            i++;
                        }
                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            System.out.println(i);
                        }
                    });
            reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 有模型写
     * 抛弃了许多不需要的信息(如：样式...)
     */
    @Test
    public void modelWrite() {
        try (OutputStream out = new FileOutputStream("E://data//modelWrite.xlsx")){
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet = new Sheet(1, 0, MultiLineHeadExcelModel.class);
            sheet.setSheetName("test1");
            List<MultiLineHeadExcelModel> data = new ArrayList<>();
            for (int i=0;i<100000;i++){
                data.add(new MultiLineHeadExcelModel(i+"-1",i+"-2",i+"-3",i+"-4",i+"-5",i+"-6",i+"-7",i+"-8",i+"-9"));
            }
            writer.write(data, sheet);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一个Excel中有多个sheet
     */
    @Test
    public void multipleSheet() {
        try (OutputStream out = new FileOutputStream("E://data//sheets.xlsx")){
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX,false);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("第一个sheet");
            // 查询数据导出即可 比如说一次性总共查询出100条数据
            List<List<String>> userList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                userList.add(Arrays.asList("ID_" + i, "小明" + i, String.valueOf(i), new Date().toString()));
            }
            writer.write0(userList, sheet1);

            //写第二个sheet sheet2  模型上打有表头的注解，合并单元格
            Sheet sheet2 = new Sheet(2, 0, MultiLineHeadExcelModel.class, "第二个sheet", null);
            sheet2.setTableStyle(new TableStyle());
            List<MultiLineHeadExcelModel> data = new ArrayList<>();
            for (int i=0;i<1000;i++){
                data.add(new MultiLineHeadExcelModel(i+"-1",i+"-2",i+"-3",i+"-4",i+"-5",i+"-6",i+"-7",i+"-8",i+"-9"));
            }
            writer.write(data, sheet2);

            //写sheet3  模型上没有注解，表头数据动态传入
            List<List<String>> head = new ArrayList<>();
            List<String> headCoulumn1 = new ArrayList<>();
            List<String> headCoulumn2 = new ArrayList<>();
            List<String> headCoulumn3 = new ArrayList<>();
            headCoulumn1.add("第一列");
            headCoulumn2.add("第二列");
            headCoulumn3.add("第三列");
            head.add(headCoulumn1);
            head.add(headCoulumn2);
            head.add(headCoulumn3);
            Sheet sheet3 = new Sheet(3, 1, NoAnnModel.class, "第三个sheet", head);
            List<NoAnnModel> noAnnModels = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                noAnnModels.add(new NoAnnModel(i+"-1", i+"-2", i+"-3"));
            }
            writer.write(noAnnModels, sheet3);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws FileNotFoundException
     */
    @Test
    public void multipleTable(){
        try (OutputStream out = new FileOutputStream("E://data//tables.xlsx")) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            //写sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("第一个sheet");
            Table table1 = new Table(1);
            // 查询数据导出即可 比如说一次性总共查询出100条数据
            List<List<String>> userList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                userList.add(Arrays.asList("ID_" + i, "小明" + i, String.valueOf(i), new Date().toString()));
            }
            writer.write0(userList, sheet1, table1);
            //写sheet2  模型上打有表头的注解
            Table table2 = new Table(2);
            table2.setClazz(MultiLineHeadExcelModel.class);
            List<MultiLineHeadExcelModel> data = new ArrayList<>();
            for (int i=0;i<150;i++){
                data.add(new MultiLineHeadExcelModel(i+"-1",i+"-2",i+"-3",i+"-4",i+"-5",i+"-6",i+"-7",i+"-8",i+"-9"));
            }
            writer.write(data, sheet1, table2);
            //写sheet3  模型上没有注解，表头数据动态传入,此情况下模型field顺序与excel现实顺序一致
            List<List<String>> head = new ArrayList<>();
            head.add(Arrays.asList("第一列","第一列"));
            head.add(Arrays.asList("第二列","第二列"));
            head.add(Arrays.asList("第三列","第四列"));
            Table table3 = new Table(3);
            table3.setHead(head);
            table3.setClazz(NoAnnModel.class);
            List<NoAnnModel> noAnnModels = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                noAnnModels.add(new NoAnnModel(i+"-1", i+"-2", i+"-3"));
            }
            writer.write(noAnnModels, sheet1, table3);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
