package com.nowcoder.wenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WendaApplication {

	@PostConstruct
	public void init(){
		// 解决netty启动冲突问题
		// see netty4Utils
		System.setProperty("es.set.netty.runtime.available.processors","false");
	}
	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}

}
