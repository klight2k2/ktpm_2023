package com.ktpm.model;

public class LoaiCoSoVatChat{
	private String loaiDoDung;
	private String tinhTrang;
	private int soLuong;
	 public LoaiCoSoVatChat(String loaiDoDung, String tinhTrang, int soLuong) {
	        this.loaiDoDung = loaiDoDung;
	        this.tinhTrang = tinhTrang;
	        this.soLuong = soLuong;
	    }
	 public String getLoaiDoDung() {
	        return loaiDoDung;
	    }

	    public void setLoaiDoDung(String loaiDoDung) {
	        this.loaiDoDung = loaiDoDung;
	    }

	    public String getTinhTrang() {
	        return tinhTrang;
	    }

	    public void setTinhTrang(String tinhTrang) {
	        this.tinhTrang = tinhTrang;
	    }

	    public int getSoLuong() {
	        return soLuong;
	    }

	    public void setSoLuong(int soLuong) {
	        this.soLuong = soLuong;
	    }
	
}
