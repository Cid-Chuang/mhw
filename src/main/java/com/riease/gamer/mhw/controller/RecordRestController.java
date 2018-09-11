package com.riease.gamer.mhw.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riease.gamer.mhw.entity.AlchemyRecord;
import com.riease.gamer.mhw.entity.AlchemyRecordExample;
import com.riease.gamer.mhw.mapper.AlchemyRecordMapper;
import com.riease.gamer.mhw.model.form.AlchemyRecordForm;

@RestController
@RequestMapping("/api/record")
public class RecordRestController {

	private static Logger log = LoggerFactory.getLogger(RecordRestController.class);

	@Autowired
	private AlchemyRecordMapper alchemyRecordMapper;
	
	@PostMapping("/add")
	public ResponseEntity<AlchemyRecord> add(
			AlchemyRecord record, HttpServletRequest request, HttpServletResponse response) {
		
		Long total = this.alchemyRecordMapper.countByExample(new AlchemyRecordExample());
		
		record.setUid(UUID.randomUUID().toString());
		record.setSn(total.intValue() + 1);
		record.setIsUsed(false);
		record.setCreateTime(new Date());
		
		int rc = alchemyRecordMapper.insert(record);
		if(rc == 0) {
			log.warn("insert alchemy record failed. use {}", record);
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(record);
	}

	@PostMapping("/update")
	public ResponseEntity<AlchemyRecord> update(
			@RequestBody AlchemyRecordForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("form data >>> {}", ToStringBuilder.reflectionToString(form, ToStringStyle.JSON_STYLE));
		
		if(StringUtils.isBlank(form.getUid())) {
			log.warn("update record uid not found.");
			return ResponseEntity.badRequest().build();
		}
		
		AlchemyRecord record = this.alchemyRecordMapper.selectByPrimaryKey(form.getUid());
		if(record == null) {
			log.warn("record not found. use uid {}", form.getUid());
			return ResponseEntity.badRequest().build();
		}
		
		record.setIsUsed(form.getIsUsed());
		record.setJewel1(form.getJewel1());
		record.setJewel2(form.getJewel2());
		record.setJewel3(form.getJewel3());
		record.setTargetIndex(form.getTargetIndex());
		record.setUsedType(form.getUsedType());
		
		int rc = alchemyRecordMapper.updateByPrimaryKey(record);
		if(rc == 0) {
			log.warn("update record failed.");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(record);
	}
	
	@PostMapping("/updateUsed")
	public ResponseEntity<Integer> updateUsed(
			AlchemyRecordForm form) {
		
		AlchemyRecord record = new AlchemyRecord();
		record.setIsUsed(form.getIsUsed());
		record.setUsedType(form.getUsedType());
		
		AlchemyRecordExample example = new AlchemyRecordExample();
		example.createCriteria().andUidEqualTo(form.getUid());
		
		int rc = this.alchemyRecordMapper.updateByExampleSelective(record, example);
		return ResponseEntity.ok(new Integer(rc));
	}
	
	@PostMapping("/updateTargetIndex")
	public ResponseEntity<Integer> updateTargetIndex(
			AlchemyRecordForm form) {
		
		AlchemyRecord record = new AlchemyRecord();
		record.setTargetIndex(form.getTargetIndex());
		
		AlchemyRecordExample example = new AlchemyRecordExample();
		example.createCriteria().andUidEqualTo(form.getUid());
		
		int rc = this.alchemyRecordMapper.updateByExampleSelective(record, example);
		return ResponseEntity.ok(new Integer(rc));
	}
	
	
	@GetMapping("")
	public ResponseEntity<List<AlchemyRecord>> list() {
		AlchemyRecordExample example = new AlchemyRecordExample();
		example.createCriteria();
		example.setOrderByClause("create_time DESC");
		RowBounds rowBounds = new RowBounds(0, 100);
		List<AlchemyRecord> list = this.alchemyRecordMapper.selectByExampleWithRowbounds(example, rowBounds);
		Collections.reverse(list);
		return ResponseEntity.ok(list);
	}
	
}
