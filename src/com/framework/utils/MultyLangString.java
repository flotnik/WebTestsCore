package com.framework.utils;

/***
 * The MultyLangString class represents two String-objects. It is used for unified search and access to the element.
 * The current unification covers: Russian and English
 * @author nshevtsov
 *
 */
public class MultyLangString{

    String rus;
    String eng;      
    
    public MultyLangString() {
	super();	
    }

    /***
     * Constructs a new simple MultyLangString.
     * @param rus
     * @param eng
     */
    public MultyLangString(String rus, String eng) {

	super();
	this.rus = rus;
	this.eng = eng;
    }
    
    public String getRus() {
        return rus;
    }
    
    public void setRus(String rus) {
        this.rus = rus;
    }
    
    public String getEng() {

        return eng;
    }
    
    public void setEng(String eng) {
        this.eng = eng;
    }


    @Override
    public String toString() {        
        return "(" + getEng() +" = "+getRus()+")";
    }
    
    /***
     * функция сравнивает строку str с англ и русской строкой
     * @param str - сравниваема¤ строка
     * @return true если str совпадает хот¤бы с одной языковой строкой
     */
    public boolean compare(String str){
	return rus.equalsIgnoreCase(str)||eng.equalsIgnoreCase(str);
    }
}
