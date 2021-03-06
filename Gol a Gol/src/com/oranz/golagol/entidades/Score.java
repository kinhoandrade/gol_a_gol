package com.oranz.golagol.entidades;

import java.util.Date;

public class Score {
	private int cd_score;
	private int idArena;
	private String Arena;
	private int quantity;
	private Date date;

	public Score() {
		super();
	}
	public int getIdArena() {
		return idArena;
	}
	public void setIdArena(int idArena) {
		this.idArena = idArena;
	}
	public String getArena() {
		return Arena;
	}
	public void setArena(String arena) {
		Arena = arena;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getCd_score() {
		return cd_score;
	}
	public void setCd_score(int cd_score) {
		this.cd_score = cd_score;
	}	
}
