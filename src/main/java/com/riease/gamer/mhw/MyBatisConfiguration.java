package com.riease.gamer.mhw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {
		"com.riease.gamer.mhw.mapper",
	 	"com.riease.gamer.mhw.dao"
	})
public class MyBatisConfiguration {
	
}
