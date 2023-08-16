package com.mjuAppSW.joA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class JProjectApplication {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}
	public static void main(String[] args) {
		SpringApplication.run(JProjectApplication.class, args);
	}
}
