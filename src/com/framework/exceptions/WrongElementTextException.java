package com.framework.exceptions;

public class WrongElementTextException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

	public WrongElementTextException(String id, String expectedText, String actualText) {
		super("Element with id '"+ id + "' has wrong text. Expected - '" + expectedText + "', actual - '" + actualText + "'.");
	}

}
