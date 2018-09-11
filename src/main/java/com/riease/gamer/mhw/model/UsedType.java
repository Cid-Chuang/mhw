package com.riease.gamer.mhw.model;

public enum UsedType {

	Mission1,
	Mission2,
	Alchemy,
	;
	
	private UsedType() {
	}
	
	public String getText() {
		switch(this) {
		case Alchemy:
			return "煉金";
		case Mission1:
			return "任務過1";
		case Mission2:
			return "任務過2";
		default:
			return "";
		}
	}
	
	public Integer getCount() {
		switch(this) {
		case Alchemy:
		case Mission1:
			return 1;
		case Mission2:
			return 2;
		default:
			return 0;
		}
	}
}
