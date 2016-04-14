package com.jov.db;

public class AppAbility {
	private String item;
	private String score;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return item + ", ÄúµÄµÃ·Ö" + score ;
	}
	
}
