package buyinggoods.controller;

import buyinggoods.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @RequestMapping("/upload/{filePath}")
    public void uploadExcel(@PathVariable String filePath){
        excelService.uplaodExcel(filePath);
    }
}
