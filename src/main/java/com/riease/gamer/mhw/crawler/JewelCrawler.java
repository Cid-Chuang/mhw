package com.riease.gamer.mhw.crawler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.riease.gamer.mhw.entity.Jewel;
import com.riease.gamer.mhw.entity.JewelBean;
import com.riease.gamer.mhw.entity.JewelExample;
import com.riease.gamer.mhw.mapper.JewelMapper;

@Component
public class JewelCrawler {

	private static Logger log = LoggerFactory.getLogger(JewelCrawler.class);
	
	private static String WikiUrl = "https://www.mhchinese.wiki/adornments";
	private File outputFile = new File("/Users/chi/Desktop/mhw/jewel.csv");
	
	@Autowired
	private JewelMapper jewelMapper;
	
	public void run() throws MalformedURLException, IOException {
	
		log.info("prepare to read url {}", WikiUrl);
		
		Document doc = Jsoup.parse(new URL(WikiUrl), 10 * 1000);
		Elements elts = doc.body().select("th a.tip");
		log.info("match size {}", elts.size());
		
		List<String> recordList = new ArrayList<>();
		recordList.add("名稱");
		List<JewelBean> jewelList = new ArrayList<>();
		
		Iterator<Element> it = elts.iterator();
		while(it.hasNext()) {
			Element elt = it.next();
			String name = StringUtils.trim(elt.text());
			String s1 = "【", s2 = "】";
			String jewelName = StringUtils.substringBefore(name, s1);
			String socketNumber = StringUtils.split(StringUtils.substringAfter(name, s1), s2)[0];
			String rareValue = readRareValue(elt.attr("href"));
			jewelName = translate(jewelName);
			
			JewelBean jewel = new JewelBean(jewelName, rareValue, socketNumber);
			jewelList.add(jewel);
			log.info("fetch jewel data >>> {}", ToStringBuilder.reflectionToString(jewel));
		}
		
		// 排序 
		Collections.sort(jewelList, new Comparator<JewelBean>() {
			@Override
			public int compare(JewelBean b1, JewelBean b2) {
				if(b1.getRare().intValue() != b2.getRare().intValue()) {
					return b2.getRare().compareTo(b1.getRare());
				}
				return b1.getName().compareTo(b2.getName());
			}
		});
		
		if(outputFile.exists()) {
			outputFile.delete();
		}
		
		for(JewelBean jewel : jewelList) {
			recordList.add(jewel.toName());
			saveJewelData(jewel);
		}
		
		FileUtils.writeLines(outputFile, recordList);
		log.info("write to file {} completed.", outputFile.getAbsolutePath());
	}

	private void saveJewelData(JewelBean jewelBean) {
		Jewel jewel = null;
		JewelExample example = new JewelExample();
		example.createCriteria().andNameEqualTo(jewelBean.getName());
		List<Jewel> list = this.jewelMapper.selectByExample(example);
		if(list.isEmpty()) {			
			jewel = new Jewel()
					.withUid(UUID.randomUUID().toString())
					.withName(jewelBean.getName())
					.withRare(jewelBean.getRare())
					.withSocket(jewelBean.getSocket());
			this.jewelMapper.insert(jewel);
			return;
		}
		
		jewel = list.get(0)
				.withRare(jewelBean.getRare())
				.withSocket(jewelBean.getSocket());
		this.jewelMapper.updateByPrimaryKey(jewel);
	}

	private String translate(String jewelName) {
		@SuppressWarnings("serial")
		Map<String, String> dict = new HashMap<String, String>() {{
			put("采配珠", "指示珠");
		}};
		if(dict.containsKey(jewelName)) {
			return dict.get(jewelName);
		}
		return jewelName;
	}

	private String readRareValue(String url) throws MalformedURLException, IOException {
		String key = StringUtils.substringAfterLast(url, "/");
		String newUrl = WikiUrl + "/" + key;
		Document doc = Jsoup.parse(new URL(newUrl), 10 * 1000);
		Elements tables = doc.body().select("table.simple-table");
		Iterator<Element> it = tables.iterator();
		while(it.hasNext()) {
			Element table = it.next();
			if(table.html().indexOf("稀有度") == -1) {
				continue;
			}
			String rareValue = table.select("td").last().text();
			return rareValue;
		}
		return "";
	}
	
}
