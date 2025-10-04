package com.DPRIHKAT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DpriImpotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpriImpotsApplication.class, args);
	}
}