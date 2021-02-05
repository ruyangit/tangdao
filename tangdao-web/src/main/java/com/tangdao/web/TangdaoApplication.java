package com.tangdao.web;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.tangdao" })
public class TangdaoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(TangdaoApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TangdaoApplication.class);
	}
}
