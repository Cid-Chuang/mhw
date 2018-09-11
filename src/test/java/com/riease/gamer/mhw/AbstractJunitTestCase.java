package com.riease.gamer.mhw;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = {
		//"spring.profiles.active=dev"
	})
public abstract class AbstractJunitTestCase {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
}
