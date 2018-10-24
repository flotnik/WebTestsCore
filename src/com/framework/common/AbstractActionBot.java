package com.framework.common;

import ch.qos.logback.classic.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public abstract class AbstractActionBot {
    RemoteWebDriver driver;
    Logger log;
    int maxLoadTime;
    String baseURL;

    public AbstractActionBot(RemoteWebDriver driver, int maxLoadTime, String baseURL, Logger log) {
        this.driver = driver;
        this.maxLoadTime = maxLoadTime;
        this.log = log;
        this.baseURL = baseURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public Logger getLogger() {
        return log;
    }

    public void setLogger(Logger log) {
        this.log = log;
    }

    public DesiredCapabilities getCapabilities() {
        return (DesiredCapabilities) driver.getCapabilities();
    }

    public void get(String string) {

    }

    public void quit() {
        driver.quit();
    }

	public void waitElement(final Selector selector) throws Exception {
		waitElement(selector, maxLoadTime);
	}

    public void waitElement(final Selector selector, int secs) throws Exception {
        try {
            log.debug(String.format("Wait element '%s' (%s)", selector.name, selector.xpath));
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(java.time.Duration.ofSeconds(secs))
                    // .pollingEvery(java.time.Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class);
            wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.xpath(selector.xpath));
                }
            });
        } catch (Exception e) {
            throw new Exception(String.format("Element '%s' doesn't present after awaiting %s sec. (%s)", selector.name,
                    maxLoadTime, selector.xpath));
        }
    }

    /**
     * Highlight Element. ATTENTION! this remove any border from element!!
     *
     * @param selector
     * @param mills
     * @throws Exception
     */
    public void highlightElement(final Selector selector, String c, int mills) throws Exception {
        waitElement(selector);
        WebElement elem = driver.findElement(By.xpath(selector.xpath));
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            driver.executeScript("arguments[0].style.border='3px solid " + c + "'", elem);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.executeScript("arguments[0].style.border=''", elem);

        }
    }

    public void click(Selector selector) throws Exception {
        waitElement(selector);
        try {
            log.debug(String.format("Click by element '%s' (%s)", selector.name, selector.xpath));
            WebElement e = driver.findElement(By.xpath(selector.xpath));
            e.click();
        } catch (Exception e) {
            // String[] lines = e.getMessage().split("\\(Session");
            // String first_line_of_message = lines[0];
            throw new Exception(String.format(" Error during click element '%s' (%s)", selector.name, selector.xpath)
                    + " " + e.getLocalizedMessage());
        }
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void explore(Selector selector) {
        for (int i = 1; i <= 19; i++) {
            WebElement e = driver.findElement(By.xpath(selector.child("/*[" + i + "]", "").xpath));
            System.out.println(e.getTagName() + "-" + e.getText());
        }
    }

    abstract public String getScreenShot(String string, File file) throws IOException;

}
