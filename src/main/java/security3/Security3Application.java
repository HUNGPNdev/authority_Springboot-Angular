package security3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Security3Application {

	private static final Logger log = LoggerFactory.getLogger(Security3Application.class);

	public static void main(String[] args) {
		log.info("START PROJECT");
		SpringApplication.run(Security3Application.class, args);
		log.info("PROJECT START FINISH");
	}

}
