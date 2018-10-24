package com.framework.common;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import ch.qos.logback.classic.Logger;

public class QtActionBot extends AbstractActionBot {

	public QtActionBot(RemoteWebDriver driver, int maxLoadTime, String baseURL, Logger log) {
		super(driver, maxLoadTime, baseURL, log);
		driver.setLogLevel(Level.ALL);
	}

	@Override
	public String getScreenShot(final String filename, File root) throws IOException {
		try {
			// perform scrolling to beginning of page
			// Actions action = new Actions(driver);
			// action.sendKeys(Keys.HOME).perform();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
			GregorianCalendar calendar = new GregorianCalendar();
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+6"));
			String today = dateFormat.format(calendar.getTime()).replaceAll(" ", "__");

			WebDriver wrappedDriver = new Augmenter().augment(driver);
			File scrFile = ((TakesScreenshot) wrappedDriver).getScreenshotAs(OutputType.FILE);

			File screenshot = new File(root, filename + "_" + today.replaceAll(":", "_") + ".png");
			FileUtils.copyFile(scrFile, screenshot.getAbsoluteFile());

			System.out.println("Screenshoted: " + screenshot);

			return screenshot.getName();
		} catch (Exception e) {
			log.error("getScreenShot: " + e);
		}
		return null;
	}
}
