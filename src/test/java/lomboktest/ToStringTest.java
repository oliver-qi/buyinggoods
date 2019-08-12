package lomboktest;

import lombok.Data;
import lombok.ToString;

public class ToStringTest {

    public static void main(String[] args) {
        Inner inner = new Inner();
        inner.setName("qxw");
        inner.setAge(23);
        System.out.println(inner);
    }

    @ToString
    @Data
    private static class Inner{
        private String name;
        private int age;
    }
}
