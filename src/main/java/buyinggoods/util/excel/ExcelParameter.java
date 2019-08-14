package buyinggoods.util.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelParameter {
    private int sheetNo;
    private String sheetName;
    private int headLineMun;
    private List<List<String>> listStringHead;
    private Map<Integer, Integer> columnWidth;
    private List<MergeParameter> mergeParameterList;
}
