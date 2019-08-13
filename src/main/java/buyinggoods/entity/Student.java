package buyinggoods.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    private String name;
    private int age;
    private boolean sex;
    private Date birthday;

    public boolean getSex(){
        return this.sex;
    }
}
