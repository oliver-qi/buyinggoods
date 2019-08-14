package buyinggoods.util.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeParameter {
    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;
}
