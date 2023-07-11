package com.ktpm.model;

public class BaseNhanKhau {
	protected String HoTen, GioiTinh, NgaySinh, NoiThuongTru;
	
	
	public BaseNhanKhau(String hoTen, String gioiTinh, String ngaySinh, String noiThuongTru) {
		this.GioiTinh = gioiTinh;
		this.HoTen = hoTen;
		this.NgaySinh = ngaySinh;
		this.NoiThuongTru = noiThuongTru;
	}
	
	public BaseNhanKhau() {
		
	}


	public String getHoTen() {
		return HoTen;
	}


	public void setHoTen(String hoTen) {
		HoTen = hoTen;
	}


	public String getGioiTinh() {
		return GioiTinh;
	}


	public void setGioiTinh(String gioiTinh) {
		GioiTinh = gioiTinh;
	}


	public String getNgaySinh() {
		return NgaySinh;
	}


	public void setNgaySinh(String ngaySinh) {
		NgaySinh = ngaySinh;
	}


	public String getNoiThuongTru() {
		return NoiThuongTru;
	}


	public void setNoiThuongTru(String noiThuongTru) {
		NoiThuongTru = noiThuongTru;
	}
	
	
}
