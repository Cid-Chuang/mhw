package com.riease.gamer.mhw.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JewelBean extends Jewel {

	
	private static final long serialVersionUID = -1931183656173070189L;
	
	
	public JewelBean() {
	}
	
	public JewelBean(String name, String rare, String socket) {
		super.setName(name);
		super.setRare(Integer.valueOf(rare));
		super.setSocket(Integer.valueOf(socket));
	}
	
	@JsonProperty("fullName")
	public String toName() {
		List<String> list = new ArrayList<>();
		list.add("R" + this.getRare());
		list.add(this.getName());
		list.add("S" + this.getSocket());
		return StringUtils.join(list, "-");
	}
}
