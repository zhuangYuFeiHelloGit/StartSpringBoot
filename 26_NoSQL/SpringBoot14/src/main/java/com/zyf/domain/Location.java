package com.zyf.domain;

/**
 * Created by zyf on 2018/3/14.
 */
public class Location {

	private String place;

	private String year;

	public Location(String place, String year) {
		this.place = place;
		this.year = year;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
