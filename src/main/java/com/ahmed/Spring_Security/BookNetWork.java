package com.ahmed.Spring_Security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // for the createdAt and lastModifiedAt and reference on the bean of the auditing
@EnableAsync // used in email service
public class BookNetWork {

	public static void main(String[] args) {
		SpringApplication.run(BookNetWork.class, args);
	}

}
