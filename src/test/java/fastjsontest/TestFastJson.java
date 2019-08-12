package fastjsontest;

import buyinggoods.Application;
import buyinggoods.model.User;
import buyinggoods.model.UserGroup;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestFastJson {
    /**
     * java对象转 json字符串
     */
    @Test
    public void objectTOJson(){
        //简单java类转json字符串
        User user = new User("dmego", "123456");
        String UserJson = JSON.toJSONString(user);
        System.out.println("简单java类转json字符串:"+UserJson);

        //List<Object>转json字符串
        User user1 = new User("zhangsan", "123123");
        User user2 = new User("lisi", "321321");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        String ListUserJson = JSON.toJSONString(users);
        System.out.println("List<Object>转json字符串:"+ListUserJson);

        //复杂java类转json字符串
        UserGroup userGroup = new UserGroup("userGroup", users);
        String userGroupJson = JSON.toJSONString(userGroup);
        System.out.println("复杂java类转json字符串:"+userGroupJson);
    }

    /**
     * json字符串转java对象
     * 注：字符串中使用双引号需要转义 (" --> \"),这里使用的是单引号
     */
    @Test
    public void JsonTOObject(){
        /* json字符串转简单java对象
         * 字符串：{"password":"123456","username":"dmego"}*/

        String jsonStr1 = "{'password':'123456','username':'dmego'}";
        User user = JSON.parseObject(jsonStr1, User.class);
        System.out.println("json字符串转简单java对象:"+user.toString());

        /*
         * json字符串转List<Object>对象
         * 字符串：[{"password":"123123","username":"zhangsan"},{"password":"321321","username":"lisi"}]
         */
        String jsonStr2 = "[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]";
        List<User> users = JSON.parseArray(jsonStr2, User.class);
        System.out.println("json字符串转List<Object>对象:"+users.toString());

        /*json字符串转复杂java对象
         * 字符串：{"name":"userGroup","users":[{"password":"123123","username":"zhangsan"},{"password":"321321","username":"lisi"}]}
         * */
        String jsonStr3 = "{'name':'userGroup','users':[{'password':'123123','username':'zhangsan'},{'password':'321321','username':'lisi'}]}";
        UserGroup userGroup = JSON.parseObject(jsonStr3, UserGroup.class);
        System.out.println("json字符串转复杂java对象:"+userGroup);
    }

    @Test
    public void test() throws Exception {
        String numStr = JSONObject.numberToString(1);
        System.out.println("number = " + numStr);
    }
}
