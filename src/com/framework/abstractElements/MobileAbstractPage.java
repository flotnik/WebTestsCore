package com.framework.abstractElements;

import ch.qos.logback.classic.Logger;

import com.framework.common.MobileActionBot;


/***
 * 
 * @author atyurin
 * 
 */
abstract public class MobileAbstractPage {

	protected MobileActionBot bot;
	protected String url = ""; // адрес конктерной страницы (baseUrl + что-то)

	public MobileAbstractPage(MobileActionBot params) {
		this.bot = params;		
		//bot.log.debug("Construct page: " + getClass());
	}

	public void open() throws Exception {
		waitForPageLoad();
	}

	public void close() {
		bot.quit();
	}

	public abstract void waitForPageLoad() throws Exception;

}
