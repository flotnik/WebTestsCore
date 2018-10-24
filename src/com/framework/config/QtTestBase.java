package com.framework.config;

import java.lang.reflect.Method;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.framework.common.QtActionBot;
import com.framework.common.WebActionBot;

/***
 * 
 * @author nshevtsov
 * 
 */
public class QtTestBase extends TestBase {

	@DataProvider(name = "qtBot")
	public Object[][] qtdataSet(Method m) throws Throwable {
		String qtwebdriver_url = db.GetBlock("MAIN").getString("qtwebdriver_url");
		int maxLoadTime = db.GetBlock("MAIN").getInteger("maxLoadTime");
		String itc_execution_file = db.GetBlock("MAIN").getString("itc_execution_file");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserStartWindow", "*");

		Runtime.getRuntime().exec("\"" + itc_execution_file+ "\"" + " QTWEBDRIVER");
		Thread.sleep(2000);
		WebActionBot bot = new WebActionBot(new RemoteWebDriver(new URL(qtwebdriver_url), capabilities), maxLoadTime,"", getLogger(m));
		bot.getLogger().info("ITC started");
		return new Object[][] { { bot } };
	}

	
	@Override
	@AfterMethod
	public void afterMethod(ITestResult tr) throws Exception {
		WebActionBot bot = (WebActionBot) tr.getParameters()[0];

		bot.getLogger().info("quit the ITC");
		bot.quit2();	
	}
}
