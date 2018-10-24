package com.framework.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/***
 * 
 * @author nshevtsov
 * 
 */
public class DriverInitializer {
	public RemoteWebDriver getWebDriver(String hub, Capabilities capabilities) throws Exception {
		int tryCount = 0;
		RemoteWebDriver driver;
		URL url = new URL(hub);

		while (true) {
			try {
				driver = new RemoteWebDriver(url, capabilities);
				// driver.setFileDetector(new LocalFileDetector());
				break;
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
				if (tryCount++ > 5) {
					throw new Exception("Selenium can't init after 10 attempts: " + e.getMessage());
				}
			}
			Thread.sleep(2000);
		}

		driver.manage().window().setSize(new Dimension(1280, 960));
		driver.manage().window().maximize();
		return driver;
	}

	public AppiumDriver<MobileElement> getAppiumDriver(String hub, DesiredCapabilities capabilities) throws Exception {
		AppiumDriver<MobileElement> driver = null;

		if (capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME).equals(MobilePlatform.ANDROID)) {
			driver = new AndroidDriver<MobileElement>(new URL(hub), capabilities);
		} else if (capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME).equals(MobilePlatform.IOS)) {
			driver = new IOSDriver<MobileElement>(new URL(hub), capabilities);
		}
		driver.manage().window().maximize();
		return driver;
	}
}
