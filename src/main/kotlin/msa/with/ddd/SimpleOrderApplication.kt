package codereview.simpleorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimpleOrderApplication {

	public static void main(String[] args) {

		SpringApplication.run(SimpleOrderApplication.class, args);
	}
}
