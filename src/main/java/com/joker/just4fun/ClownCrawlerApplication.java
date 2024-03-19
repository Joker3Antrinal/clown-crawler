package com.joker.just4fun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

@SpringBootApplication(scanBasePackages = {"com.joker.just4fun.*"})
@MapperScan("com.joker.just4fun.mapper")
public class ClownCrawlerApplication {

	public static void main(String[] args) {
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();//System.out的内容输出到SLF4J中
		SpringApplication.run(ClownCrawlerApplication.class, args);
	}

}
