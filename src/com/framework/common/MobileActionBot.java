package com.framework.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
//import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;

import ch.qos.logback.classic.Logger;

public class MobileActionBot {
	protected final AppiumDriver<MobileElement> driver;
	Logger log;
	int maxLoadTime;
	private final String androidIdPrefix;

	public MobileActionBot(AppiumDriver<MobileElement> driver, int maxLoadTime) {
		this.driver = driver;
		this.maxLoadTime = maxLoadTime;
		if (driver instanceof AndroidDriver) {
			androidIdPrefix = driver.getCapabilities().getCapability(MobileCapabilityType.APP) + ":id/";
		} else {
			androidIdPrefix = "";
		}
	}

	public void setLogger(Logger logger) {
		this.log = logger;
	}

	public void sleep(int mileseconds) throws InterruptedException {
		Thread.sleep(mileseconds);
	}

	public void quit() {
		((MobileDriver) driver).closeApp();
		driver.quit();
	}

	/**
	 * return MobileElement with id or null if element not found
	 */
	public MobileElement find(String id) {
		log.debug(String.format("Find element with id '%s'", id));
		MobileElement element = null;
		if (driver instanceof AndroidDriver) {
			element = (MobileElement) ((AndroidDriver<MobileElement>) driver).findElementById(androidIdPrefix + id);
		} else {
			IOSDriver<MobileElement> iosDriver = (IOSDriver<MobileElement>) driver;
			element = (MobileElement) iosDriver.findElementByAccessibilityId(id);
			log.debug(element.toString());
		}
		if (element == null) {
			log.error("NoSuchElementException");
			throw new NoSuchElementException(String.format("Element with id '%s' not found", id));
		}
		return element;
	}

	public void click(String id) throws Exception {
		log.debug(String.format("Click element with id '%s'", id));
		try {
			MobileElement element = find(id);
			log.debug("Before click");
			element.click();
		} catch (Exception e) {
			throw new Exception(
					String.format("Error during click element with id '%s'. Error: %s", id, e.getMessage()));
		}
	}

/*	public void swipeElement(String id, SwipeElementDirection direction) {
		log.debug(String.format("Swipe element with id '%s'", id));
		MobileElement element = find(id);
		element.swipe(direction, 100, 100, 1000);
		// element.swipe(direction, 1000);
	}*/

	public void waitForElement(String id, int seconds) {
		log.debug(String.format("Waiting for element with id '%s'", id));
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		MobileElement element = find(id);
	}
}
