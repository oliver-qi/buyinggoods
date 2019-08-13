package buyinggoods.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ExcelUtilWithXSSF {
    public static void main(String[] args) {
        try {
            getExcelAsFile("E:/data/students2.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


//		try {
//			CreateExcelDemo1();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}


    }

    /**
     * 得到Excel，并解析内容  对2007及以上版本 使用XSSF解析
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void getExcelAsFile(String file) throws FileNotFoundException, IOException, InvalidFormatException{
//		//1.得到Excel常用对象
//		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("d:/FTP/new1.xls"));
//		//2.得到Excel工作簿对象
//		HSSFWorkbook wb = new HSSFWorkbook(fs);



        InputStream ins = null;
        Workbook wb = null;
        ins=new FileInputStream(new File(file));
        wb = WorkbookFactory.create(ins);
        ins.close();


        //3.得到Excel工作表对象
        Sheet sheet = wb.getSheetAt(0);
        //总行数
        int trLength = sheet.getLastRowNum();
        //4.得到Excel工作表的行
        Row row = sheet.getRow(0);
        //总列数
        int tdLength = row.getLastCellNum();
        //5.得到Excel工作表指定行的单元格
        Cell cell = row.getCell((short)1);
        //6.得到单元格样式
        CellStyle cellStyle = cell.getCellStyle();

        for(int i=0;i<=trLength;i++){
            //得到Excel工作表的行
            Row row1 = sheet.getRow(i);
            for(int j=0;j<tdLength;j++){
                //得到Excel工作表指定行的单元格
                Cell cell1 = row1.getCell(j);
                /**
                 * 为了处理：Excel异常Cannot get a text value from a numeric cell
                 * 将所有列中的内容都设置成String类型格式
                 */
                if(cell1!=null){
                    cell1.setCellType(Cell.CELL_TYPE_STRING);
                }

                if(j==5&&i<=10){
                    cell1.setCellValue("1000");
                }

                //获得每一列中的值
                System.out.print(cell1+"                   ");
//                System.out.print(cell1.getStringCellValue()+"\t\t\t");
            }
            System.out.println();
        }

        //将修改后的数据保存
        OutputStream out = new FileOutputStream(file);
        wb.write(out);
    }


}
