package org.wipro.saga.application;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@EnableProcessApplication
public class CamundaCamelBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaCamelBootApplication.class, args);
	}
}
