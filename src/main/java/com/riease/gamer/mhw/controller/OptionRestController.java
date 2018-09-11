package com.riease.gamer.mhw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riease.gamer.mhw.entity.Jewel;
import com.riease.gamer.mhw.entity.JewelExample;
import com.riease.gamer.mhw.mapper.JewelMapper;
import com.riease.gamer.mhw.model.UsedType;

@RestController
@RequestMapping("/api/option")
public class OptionRestController {

	@Autowired
	private JewelMapper jewelMapper;
	
	/**
	 * 裝飾品選項
	 * @return
	 */
	@GetMapping("/jewel")
	public ResponseEntity<List<Jewel>> jewel() {
		JewelExample example = new JewelExample();
		example.setOrderByClause("rare DESC, name ASC");
		List<Jewel> list = this.jewelMapper.selectByExample(example);
		return ResponseEntity.ok(list);
	}

	/**
	 * 使用種類
	 * @return
	 */
	@GetMapping("/usedType")
	public ResponseEntity<Map<String, Object>> usedType() {
		Map<String, Object> map = new HashMap<>();
		for(UsedType type : UsedType.values()) {
			Map<String, Object> data = new HashMap<>();
			data.put("text", type.getText());
			data.put("count", type.getCount());
			map.put(type.name(), data);
		}
		return ResponseEntity.ok(map);
	}
	
}
