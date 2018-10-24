package com.framework.utils;

import com.framework.utils.MultyLangString;

public class XPATH_UTILS {

    public static String toLowerCaseXpath(String text) {
	return "translate(" + text + ",  'ABCDEFGHJIKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЭЪЮЯ', " + 
					"'abcdefghjiklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщьэъюя')";
    }

    public static String textEqualsIgnoreCase(String text) {
	return smthEqualsIgnoreCase("text()", text);	
    }
    
    public static String textEquals(String text) {
	return smthEquals("text()", text);	
    }
    
    public static String textEqualsIgnoreCase(MultyLangString t) {
 	return smthEqualsIgnoreCase("text()", t);
    }
    
    public static String textEquals(MultyLangString t) {
 	return smthEquals("text()", t);
     }
    
    public static String textContainsIgnoreCase(MultyLangString t) {
	return smthContainsIgnoreCase("text()", t);
    }
    
    public static String textContains(MultyLangString t) {
	return smthContains("text()", t);
    }

    public static String valueEqualsIgnoreCase(MultyLangString t) {
	return smthEqualsIgnoreCase("@value", t);
    }
    
    public static String valueEquals(MultyLangString t) {
	return smthEquals("@value", t);
    }
    
    public static String valueEqualsIgnoreCase(String text) {
	return smthEqualsIgnoreCase("@value", text);
    }
    
    public static String valueEquals(String text) {
	return smthEquals("@value", text);
    }

    //===================================================================================
    public static String smthContainsIgnoreCase(String smth, String text) {
	return "[contains(" + toLowerCaseXpath(smth) + "," + toLowerCaseXpath("'" + text + "'") + ")]";
    }
    
    public static String smthContains(String smth, String text) {
 	return "[contains(" + smth + "," + "'" + text + "'" + ")]";
     }
    
    public static String smthContainsIgnoreCase(String smth, MultyLangString t) {
	return "[contains(" + toLowerCaseXpath(smth) + "," + toLowerCaseXpath("'" + t.getEng() + "'") + ") or contains(" + toLowerCaseXpath(smth) + "," + toLowerCaseXpath("'" + t.getRus() + "'") + ")]";
    }
    
    public static String smthContains(String smth, MultyLangString t) {
 	return "[contains(" + smth + "," + "'" + t.getEng() + "'" + ") or contains(" + smth + "," + "'" + t.getRus() + "'" + ")]";
     }
    
    public static String smthEqualsIgnoreCase(String smth, String text) {
	return "[" + toLowerCaseXpath(smth) + "=" + toLowerCaseXpath("'" + text + "'") + "]";
    }
    
    public static String smthEquals(String smth, String text) {
	return "[" + smth + "=" + "'" + text + "'" + "]";
    }
    
    public static String smthEqualsIgnoreCase(String smth, MultyLangString t) {
	return "[" + toLowerCaseXpath(smth) + "=" + toLowerCaseXpath("'" + t.getEng() + "'") + " or " + toLowerCaseXpath(smth) + "=" + toLowerCaseXpath("'" + t.getRus() + "'") + "]";
    }
    
    public static String smthEquals(String smth, MultyLangString t) {
	return "[" + smth + "=" + "'" + t.getEng() + "'" + " or " + smth + "=" + "'" + t.getRus() + "'" + "]";
    }

}
