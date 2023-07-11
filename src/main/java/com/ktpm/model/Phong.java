package com.ktpm.model;

public class Phong {
	private int ID;
	private String tenPhong;
	
	public Phong(int ID,String tenPhong) {
		this.ID=ID;
		this.tenPhong=tenPhong;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTenPhong() {
		return tenPhong;
	}
	public void setTenPhong(String tenPhong) {
		this.tenPhong = tenPhong;
	}
	
}
