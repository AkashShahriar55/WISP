package org.example.util;

public class Distance {
	
	private Integer id;
	private double distance;
	
	public Distance(Integer id, double distance){
		this.id = id;
		this.distance = distance;
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
