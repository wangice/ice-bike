package com.ice.bike.common.mogodb.dao;

import java.io.Serializable;

public class LocationVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9113448592852458792L;

	private Double longitude;
	private Double latitude;
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
}
