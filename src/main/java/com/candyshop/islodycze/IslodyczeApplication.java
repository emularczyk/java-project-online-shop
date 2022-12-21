package com.candyshop.islodycze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"your.company.domain.package"})

public class IslodyczeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IslodyczeApplication.class, args);
	}

}
