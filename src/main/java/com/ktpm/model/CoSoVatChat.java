package com.ktpm.model;

public class CoSoVatChat {
    private int maDoDung;

    private String tenDoDung, tinhTrang, tenLoaiDoDung;

    public CoSoVatChat() {
    }

    public CoSoVatChat(int maDoDung, String tenDoDung, String tinhTrang, String tenLoaiDoDung) {
        this.maDoDung = maDoDung;
        this.tenDoDung = tenDoDung;
        this.tinhTrang = tinhTrang;
        this.tenLoaiDoDung = tenLoaiDoDung;
    }

	public int getMaDoDung() {
		return maDoDung;
	}

	public void setMaDoDung(int maDoDung) {
		this.maDoDung = maDoDung;
	}

	public String getTenLoaiDoDung() {
		return tenLoaiDoDung;
	}

	public void setMaLoaiDoDung(String tenLoaiDoDung) {
		this.tenLoaiDoDung = tenLoaiDoDung;
	}

	public String getTenDoDung() {
		return tenDoDung;
	}

	public void setTenDoDung(String tenDoDung) {
		this.tenDoDung = tenDoDung;
	}

	public String getTinhTrang() {
		return tinhTrang;
	}

	public void setTinhTrang(String tinhTrang) {
		this.tinhTrang = tinhTrang;
	}

    
}
