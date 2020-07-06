package com.recipe.vo;

public class Postal {
	private String buildingno; //건물관리번호 pk
	private String zipcode;  //우편번호
	private String city;
	private String doro;
	private String building;
	
	public Postal() {}

	public Postal(String buildingno, String zipcode, String city, String doro, String building) {
		super();
		this.buildingno = buildingno;
		this.zipcode = zipcode;
		this.city = city;
		this.doro = doro;
		this.building = building;
	}

	public String getBuildingno() {
		return buildingno;
	}

	public void setBuildingno(String buildingno) {
		this.buildingno = buildingno;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getDoro() {
		return doro;
	}

	public void setDoro(String doro) {
		this.doro = doro;
	}
	
	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}
	
	@Override
	public String toString() {
		return "";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
}
