package com.ph.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Akshay
 *
 */
public class MeterReading implements Serializable{

	private static final long serialVersionUID = 1L;

	private long meterId;
	private String profile;
	private Month month;
	private int meterReading;
	private int consumption;
	
	public MeterReading(long meterId, String profile, Month month, int meterReading) {
		this.meterId = meterId;
		this.profile = profile;
		this.month = month;
		this.meterReading = meterReading;
	}

	public static final Comparator<MeterReading> MONTH_COMPARATOR = new Comparator<MeterReading>() {

		@Override
		public int compare(MeterReading o1, MeterReading o2) {
			return o1.getMonth().compareTo(o2.getMonth());
		}
	};
	
	public long getMeterId() {
		return meterId;
	}

	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public int getMeterReading() {
		return meterReading;
	}

	public void setMeterReading(int meterReading) {
		this.meterReading = meterReading;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

	@Override
	public String toString() {
		return "MeterReading [meterId=" + meterId + ", profile=" + profile + ", month=" + month + ", meterReading="
				+ meterReading + ", consumption=" + consumption + "]";
	}
}
