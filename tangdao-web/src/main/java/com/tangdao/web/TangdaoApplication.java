package com.tangdao.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.tangdao.core","com.tangdao.modules","com.tangdao.web"})
public class TangdaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TangdaoApplication.class, args);
	}
}
