package com.moroccantube.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.moroccantube.app.repository")
@EntityScan(basePackages = "com.moroccantube.app.model")
public class MoroccanTubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoroccanTubeApplication.class, args);
	}

}
