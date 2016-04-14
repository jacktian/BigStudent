package com.jov.db;

public class MentalHealth {
	private int id;
	private String subject;
	private String answerA;
	private String answerB;
	private String answerC;
	private int answerAScore;
	private int answerBScore;
	private int answerCScore;
	private String factorId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAnswerA() {
		return answerA;
	}
	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}
	public String getAnswerB() {
		return answerB;
	}
	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}
	public String getAnswerC() {
		return answerC;
	}
	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}
	public int getAnswerAScore() {
		return answerAScore;
	}
	public void setAnswerAScore(int answerAScore) {
		this.answerAScore = answerAScore;
	}
	public int getAnswerBScore() {
		return answerBScore;
	}
	public void setAnswerBScore(int answerBScore) {
		this.answerBScore = answerBScore;
	}
	public int getAnswerCScore() {
		return answerCScore;
	}
	public void setAnswerCScore(int answerCScore) {
		this.answerCScore = answerCScore;
	}
	public String getFactorId() {
		return factorId;
	}
	public void setFactorId(String factorId) {
		this.factorId = factorId;
	}
}
