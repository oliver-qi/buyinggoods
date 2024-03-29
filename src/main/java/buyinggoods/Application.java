package buyinggoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"buyinggoods.*"})
@EnableScheduling// 开启定时机制
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
