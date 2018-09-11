package com.riease.gamer.mhw.crawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.riease.gamer.mhw.AbstractJunitTestCase;

public class JewelCrawlerTest extends AbstractJunitTestCase {

	@Autowired
	private JewelCrawler crawler;
	
	@Test
	public void testRun() {
		try {
			log.info("use crawler {}", crawler.getClass());
			crawler.run();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
