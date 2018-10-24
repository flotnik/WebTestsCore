package com.framework.abstractElements;

import java.net.URI;

import com.framework.common.WebActionBot;


/***
 * 
 * @author nshevtsov
 * 
 */
abstract public class AbstractPage {

	protected String url = ""; // адрес конктерной страницы (baseUrl + что-то)
	protected WebActionBot bot;
	
	
	public AbstractPage(WebActionBot params) {
		this.bot = params;
		this.bot.getLogger().debug("Construct page: " + getClass());
	}

	public AbstractPage open() throws Exception {
		return open(new URI(bot.getBaseURL() + url));
	}

	public AbstractPage open(URI url_common) throws Exception {
		this.bot.getLogger().debug("Before open page: " + getClass() + " on " + url_common);
		bot.get(url_common.toString());
		waitForPageLoad();
		this.bot.getLogger().debug("After open page: " + getClass());
		return this;
	}

	abstract public void waitForPageLoad() throws Exception;
}