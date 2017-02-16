package com.ph.model.vo;

/**
 * @author Akshay
 *
 */
public class Status {

	private String profile;
	private String status;
	
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Status(String profile, String status) {
		super();
		this.profile = profile;
		this.status = status;
	}
}
