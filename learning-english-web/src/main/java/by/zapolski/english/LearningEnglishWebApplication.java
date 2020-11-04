package by.zapolski.english;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "by.zapolski.english")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class LearningEnglishWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningEnglishWebApplication.class, args);
    }

}
