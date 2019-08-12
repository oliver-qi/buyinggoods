package buyinggoods.entity;

import lombok.Data;

@Data
public class RecordVo {
    private boolean success = false;
    private String message = null;

    public RecordVo(){}
    public RecordVo(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
