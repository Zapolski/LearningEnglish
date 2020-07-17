package by.zapolski.english;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "by.zapolski.english")
public class LearningEnglishWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningEnglishWebApplication.class, args);
	}

}
