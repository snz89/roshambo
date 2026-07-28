package io.github.snz89.roshambo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RoshamboApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoshamboApplication.class, args);
	}

}
