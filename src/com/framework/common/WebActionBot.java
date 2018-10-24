package com.framework.common;

import ch.qos.logback.classic.Logger;
import com.framework.utils.UtilFunctions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;


/***
 *
 * @author nshevtsov
 *
 */
public class WebActionBot extends AbstractActionBot {

    public WebActionBot(RemoteWebDriver driver, int maxLoadTime, String baseURL, Logger log) {
        super(driver, maxLoadTime, baseURL, log);
    }

    public void typeText(Selector selector, String text, Boolean clean, boolean byChar) throws Exception {
        waitElement(selector);
        log.debug(String.format("Type text byChar=" + byChar + " '%s' into element '%s' (%s)", text, selector.name, selector.xpath));
        try {
            WebElement element = driver.findElement(By.xpath(selector.xpath));
            //new Actions(driver).moveToElement(element).click().perform();
            if (clean) {
                element.clear();
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            }
            if (byChar) {
                Actions action = new Actions(driver);
                // если для поля задана проверка вводимого значнеия, то нужно печатать так
                for (int i = 0; i < text.length(); i++) {
                    //element.click(); //после обноления до 45 версии selenium на 36 версии FF этот клик стал ненужным
                    String k = Character.toString(text.charAt(i));
                    //element.sendKeys(k);
                    action.sendKeys(element, k).perform();
                    Thread.sleep(200);
                }
            } else {
                element.sendKeys("");// focus on element
                //Actions action = new Actions(driver);
                //action.sendKeys(element, text).perform();
                element.sendKeys(text);
            }
        } catch (Exception e) {
            throw new Exception("Error during type into '" + selector.name + "' (" + selector.xpath + ")  " + e.getLocalizedMessage());
        }
    }

    public String getAttribute(Selector selector, String attribute) throws Exception {
        waitElement(selector);
        attribute = attribute.replaceAll("@", "");
        String result = driver.findElement(By.xpath(selector.xpath)).getAttribute(attribute);
        if (result.contains("#")) {
            result = result.substring(result.indexOf("#"));
        }
        return result;
    }

    public String getTagName(Selector selector) throws Exception {
        waitElement(selector);
        WebElement element = driver.findElement(By.xpath(selector.xpath));
        return element.getTagName();
    }

    public void chooseInSelect(Selector selector, String value) throws Exception {
        waitElement(selector);
        try {
            (new Select(driver.findElement(By.xpath(selector.xpath)))).selectByValue(value);
        } catch (Exception e) {
            throw new Exception(String.format("Error during select '%s' value '%s' (%s) : " + e.getLocalizedMessage(), selector.name, value, selector.xpath));
        }
    }

    public String getFirstSelectedOptionInSelect(Selector selector) throws Exception {
        waitElement(selector);
        try {
            return (new Select(driver.findElement(By.xpath(selector.xpath)))).getFirstSelectedOption().getAttribute("value");
        } catch (Exception e) {
            throw new Exception(String.format("Error during getFirstSelectedOption '%s' (%s)", selector.name, selector.xpath));
        }
    }

    public void selectByText(Selector selector, String text) throws Exception {
        waitElement(selector);
        try {
            (new Select(driver.findElement(By.xpath(selector.xpath)))).selectByVisibleText(text);
        } catch (Exception e) {
            throw new Exception(
                    String.format("Error during select '%s' text '%s' (%s)", selector.name, text, selector.xpath));
        }
    }

    /**
     * Select the random value from Select list
     *
     * @param selector
     * @throws Exception
     */
    @Deprecated
    public void selectRandom(Selector selector) throws Exception {
        try {
            By by = By.xpath(selector.xpath);
            Select select = new Select(driver.findElement(by));
            List<WebElement> elements = select.getOptions();

            select.selectByIndex(new Random().nextInt(elements.size() - 1));

        } catch (Exception e) {
            throw new Exception("Error during select (" + selector.xpath + ")");
        }
    }

    public void doubleClick(Selector selector) throws Exception {
        waitElement(selector);
        try {
            (new Actions(driver)).doubleClick(driver.findElement(By.xpath(selector.xpath))).build().perform();
        } catch (Exception e) {
            throw new Exception(String.format("Error during double click element '%s' (%s) " + e.getLocalizedMessage(),
                    selector.name, selector.xpath));
        }
    }

    public void dragAndDrop(Selector from, Selector to) throws Exception {
        waitElement(from);
        waitElement(to);
        try {
            (new Actions(driver))
                    .dragAndDrop(driver.findElement(By.xpath(from.xpath)), driver.findElement(By.xpath(to.xpath)))
                    .build().perform();
        } catch (Exception e) {
            throw new Exception(String.format("Error during Drag and drop: from %s (%s) to %s (%s)", from.name,
                    from.xpath, to.name, to.xpath));
        }
    }

    public void dragAndDrop(Selector selector, int xOffset, int yOffset) throws Exception {
        waitElement(selector);
        try {
            (new Actions(driver)).dragAndDropBy(driver.findElement(By.xpath(selector.xpath)), xOffset, yOffset).build()
                    .perform();
        } catch (Exception e) {
            throw new Exception(String.format("Error during Drag & Drop '%s' (%s)", selector.name, selector.xpath));
        }
    }

    public boolean isChecked(final Selector selector) throws Exception {
        waitElement(selector);
        return driver.findElement(By.xpath(selector.xpath)).isSelected();
    }

    public void verifyAlert() throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, maxLoadTime);
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (Exception e) {
            throw new Exception("Alert doesn't present");
        }
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    public void okAlert() {
        driver.switchTo().alert().accept();
    }

    public void cancelAlert() {
        driver.switchTo().alert().dismiss();
    }

    public String getTextAlert() {
        return driver.switchTo().alert().getText();
    }

    public void sendKeysToAlert(String text) {
        driver.switchTo().alert().sendKeys(text);
    }

    public boolean isElementPresent(final Selector selector) {
        try {
            driver.findElement(By.xpath(selector.xpath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitDisappear(final Selector selector) throws Exception {
        try {
            log.debug(String.format("Wait element disappear '%s' (%s)", selector.name, selector.xpath));
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(java.time.Duration.ofSeconds(maxLoadTime))
                    //.pollingEvery(java.time.Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    try {
                        WebElement r = driver.findElement(By.xpath(selector.xpath));
                        return !r.isDisplayed();
                    } /*
                         * catch (StaleElementReferenceException e) { return
						 * false; }
						 */ catch (NoSuchElementException e1) {
                        return Boolean.TRUE;
                    }
                }
            });
            // WebDriverWait wait = new WebDriverWait(driver, maxLoadTime);
            // wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(selector.xpath)));
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            // String first_line_of_message =
            // e.getMessage().split(System.lineSeparator())[0];
            throw new Exception(String.format("Error during waitDisappear '%s' (%s) " /* + first_line_of_message */,
                    selector.name, selector.xpath));
        }
    }

    public String getText(Selector selector) throws Exception {
        try {
            List<WebElement> texts = driver.findElements(By.xpath(selector.xpath));
            StringBuilder ret = new StringBuilder();
            for (WebElement t:texts) {
                ret.append(t.getText());
            }
            return ret.toString();
        } catch (Exception e) {
            throw new Exception(String.format("Error during getText element '%s' (%s)", selector.name, selector.xpath));
        }
    }

    public boolean isElementDisplayed(Selector selector) {
        return driver.findElement(By.xpath(selector.xpath)).isDisplayed();
    }

    public ArrayList<String> getArrayListText(Selector selector) throws Exception {
        try {
            ArrayList<String> result = new ArrayList<String>();
            List<WebElement> webElements = driver.findElements(By.xpath(selector.xpath));
            for (WebElement webElement : webElements) {
                String text = webElement.getText().trim();
                result.add(text);
            }
            return result;
        } catch (Exception e) {
            throw new Exception("Error during getArrayListText element '" + selector.name + "' (" + selector.xpath + ")");
        }
    }

    public int getCount(final Selector selector) {
        try {
            if (!isElementPresent(selector)) throw new Exception("get count failure");
            //waitElement(selector);
            return driver.findElements(By.xpath(selector.xpath)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public Object executeJS(String jsCode) throws Exception {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript(jsCode);
        } catch (Exception e) {
            throw new Exception("Exception during run js code: " + jsCode);
        }
    }

    @Deprecated
    public void goBack() {
        driver.navigate().back();
    }

    public void get(String url) {
        driver.get(url);
    }

    public void quit() {
        get("about:blank");
        if (driver != null) {
            driver.quit();
        } else {
            System.err.println("DRIVER IS NULL!!!!");
        }
    }

    public void quit2() {
        driver.close();
    }

    @Deprecated
    public void moveTo(Selector selector) throws Exception {
        waitElement(selector);
        try {
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(By.xpath(selector.xpath))).perform();
        } catch (Exception e) {
            throw new Exception("Exception during move to element '" + selector.name + "' (" + selector.xpath + ")");
        }
    }

    
/*	public void scrollTo(Selector selector) throws Exception {
		try {
			((RemoteWebElement) driver.findElement(By.xpath(selector.xpath))).getLocationOnScreenOnceScrolledIntoView();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception during scroll to element '" + selector.name + "' (" + selector.xpath + ")");
		}
	}*/

    public void switchToIFrame(String iFrameName) {
        driver.switchTo().frame(iFrameName);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void ninjaClick(Selector selector) throws Exception {
        waitElement(selector); // было закомментировано для автотестов в
        // CP_WEB_CONF, там нужно кликать по ещё
        // невидимому для пользователя элементу
        try {
            WebElement element = driver.findElement(By.xpath(selector.xpath));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            log.debug(String.format("ninjaClick by element '%s' (%s)", selector.name, selector.xpath));
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new Exception("Error during ninja click element ");
        }
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Switch  to the tab/window whose <i>title</i> matches <code>windowHandler</code>
     *
     * @param windowHandler human-readable part of tab/window title
     */
    public void switchToWindow(String windowHandler) {
        Set s = driver.getWindowHandles(); //this method will gives you the handles of all opened windows
        Iterator ite = s.iterator();
        while (ite.hasNext()) {
            String popupHandle = ite.next().toString();
            if (!popupHandle.contains(windowHandler)) {
                driver.switchTo().window(windowHandler);
            }
        }
    }

    public void close(String windowHandler) throws Exception {
        Set s = driver.getWindowHandles();
        if (s.contains(windowHandler)) {
            driver.switchTo().window(windowHandler);
            driver.close();
            s = driver.getWindowHandles();
            if (s.contains(windowHandler))
                throw new Exception("Error while close tab. Tab '" + windowHandler + "' still opened");
        } else {
            log.warn("Tab '" + windowHandler + "' is not found in all tabs set");
        }
    }

    public String getWindowTitle() {
        return driver.getTitle();
    }

    @Deprecated
    public void refresh() throws Throwable {
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            throw new Exception("Exception during refresh");
        }
    }

    public void ctrlClick(Selector selector) throws Exception {
        try {
            List<WebElement> elements = driver.findElements(By.xpath(selector.xpath));

            Actions builder = new Actions(driver);
            for (WebElement element : elements) {
                builder.keyDown(Keys.CONTROL).click(element).keyUp(Keys.CONTROL);
                Action ctrlClick = builder.build();
                ctrlClick.perform();
            }
        } catch (Exception e) {
            throw new Exception("Error during ctrl+click element '" + selector.name + "' (" + selector.xpath + ")");
        }
    }

    public void arrowDownClick(Selector selector) throws Exception {
        try {
            WebElement element = driver.findElement(By.xpath(selector.xpath));
            Actions builder = new Actions(driver);
            builder.click(element).sendKeys(Keys.ARROW_DOWN).build().perform();
        } catch (Exception e) {
            throw new Exception("Error during arrowDown element '" + selector.name + "' (" + selector.xpath + ")");
        }
    }

    public void arrowUpClick(Selector selector) throws Exception {
        try {
            WebElement element = driver.findElement(By.xpath(selector.xpath));
            Actions builder = new Actions(driver);
            builder.click(element).sendKeys(Keys.ARROW_UP).build().perform();
        } catch (Exception e) {
            throw new Exception("Error during arrowUp element '" + selector.name + "' (" + selector.xpath + ")");
        }
    }

    public void pressEnter(Selector selector) throws Exception {
        waitElement(selector);
        try {
            driver.findElement(By.xpath(selector.xpath)).sendKeys(Keys.RETURN);
        } catch (Exception e) {
            throw new Exception(String.format("Error during press Enter '%s' (%s)", selector.name, selector.xpath));
        }
    }

    public String getScreenShot(final String filename, File root) throws IOException {
        try {
            // perform scrolling to beginning of page
            //Actions action = new Actions(driver);
            //action.sendKeys(Keys.HOME).perform();
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
    
/*    public void waitForAjaxComplete() {
	WebDriverWait wait = new WebDriverWait(driver, maxLoadTime);
	wait.until(new ExpectedCondition<Boolean>() {
	    public Boolean apply(WebDriver d) {
		long counter = (Long) ((JavascriptExecutor) d).executeScript("return window.jQuery.active", new Object[] {});
		try {
		    Thread.sleep(50);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		long counter2 = (Long) ((JavascriptExecutor) d).executeScript("return window.jQuery.active", new Object[] {});
		
		 * try{ Thread.sleep(50); } catch (InterruptedException e) {
		 * e.printStackTrace(); } long counter3 = (Long)
		 * ((JavascriptExecutor) d).executeScript(
		 * "return window.jQuery.active", new Object[]{}); return
		 * counter == 0 && counter2 == 0 && counter3 == 0;
		 
		return counter == 0 && counter2 == 0;
	    }
	});
    }*/
}
