package com.joker.just4fun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.joker.just4fun.*"})
@MapperScan("com.joker.just4fun.mapper")
public class ClownCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClownCrawlerApplication.class, args);
	}

}
