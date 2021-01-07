package by.zapolski.english;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "by.zapolski.english")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSwagger2
public class LearningEnglishWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningEnglishWebApplication.class, args);
    }

}
