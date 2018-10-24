package com.framework.common.objects;

public class Token {

	String pan;
	String valid_date;
	String cvv2;
	String pin;
	String hash;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Token(String pan, String valid_date, String cvv2, String pin, String hash) {
		super();
		this.pan = pan;
		this.valid_date = valid_date;
		this.cvv2 = cvv2;
		this.pin = pin;
		this.hash = hash;

	}

	public Token() {
		super();
	}

	@Override
	public String toString() {
		return pan + " " + cvv2 + " " + valid_date;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getValid_date() {
		return valid_date;
	}

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
}
