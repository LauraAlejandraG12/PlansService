package com.plan.qv_ms_plans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class QvMsPlansApplication {

	public static void main(String[] args) {
		SpringApplication.run(QvMsPlansApplication.class, args);
	}

}
