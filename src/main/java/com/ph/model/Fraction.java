package com.ph.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Akshay
 *
 */
public class Fraction implements Serializable{

	private static final long serialVersionUID = 1L;

	private String profile;
	private Month month;
	private Float value;

	public Fraction(){}
	
	public Fraction(String profile, Month month, Float value) {
		this.profile = profile;
		this.month = month;
		this.value = value;
	}

	public static final Comparator<Fraction> MONTH_COMPARATOR = new Comparator<Fraction>() {

		@Override
		public int compare(Fraction o1, Fraction o2) {
			return o1.getMonth().compareTo(o2.getMonth());
		}
	};
	
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

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Fraction [profile=" + profile + ", month=" + month + ", value=" + value + "]";
	}
	
}
