package security3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "security3.*")
public class Security3Application {

	public static void main(String[] args) {
		SpringApplication.run(Security3Application.class, args);
	}

}
