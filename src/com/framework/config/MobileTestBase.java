package com.framework.config;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

import java.lang.reflect.Method;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.framework.common.DriverInitializer;
import com.framework.common.MobileActionBot;

/***
 * 
 * @author atyurin
 * 
 */
public class MobileTestBase extends TestBase {

    @DataProvider(name = "MobileTestParams")
    public Object[][] mobileDataSet(Method m) throws Throwable {
	String hub = db.GetBlock("MAIN").getString("hub");
	// String browser = TestDispatcher.db.GetBlock("MAIN").getString("browser");
	//String locale = db.GetBlock("MAIN").getString("locale");
	int maxLoadTime = db.GetBlock("MAIN").getInteger("maxLoadTime");
	String platfomName = db.GetBlock("MAIN").getString(MobileCapabilityType.PLATFORM_NAME);

	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, db.GetBlock("MAIN").getString(MobileCapabilityType.DEVICE_NAME));
	capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platfomName);
	capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, db.GetBlock("MAIN").getString(MobileCapabilityType.PLATFORM_VERSION));
	capabilities.setCapability(CapabilityType.BROWSER_NAME, ""); // Name of mobile web browser to automate. Should be an empty string if automating an app instead.
	// capabilities.setCapability(CapabilityType.VERSION, "7.1");
	//capabilities.setCapability(CapabilityType.PLATFORM, db.GetBlock("MAIN").getString(CapabilityType.PLATFORM));
	// capabilities.setCapability(MobileCapabilityType.UDID,
	// TestDispatcher.db.GetBlock("MAIN").getString(MobileCapabilityType.UDID));
	capabilities.setCapability(MobileCapabilityType.UDID, db.GetBlock("MAIN").getString(MobileCapabilityType.UDID));
	if (platfomName.equals(MobilePlatform.ANDROID)) {
	    //capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, db.GetBlock("MAIN").getString(MobileCapabilityType.APP_PACKAGE));
	    //capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, db.GetBlock("MAIN").getString(MobileCapabilityType.APP_ACTIVITY));
	} else if (platfomName.equals(MobilePlatform.IOS)) {
	    capabilities.setCapability(MobileCapabilityType.APP, db.GetBlock("MAIN").getString(MobileCapabilityType.APP));
	}

	System.out.println("Capabilities:" + capabilities.toString());

	MobileActionBot bot = new MobileActionBot(new DriverInitializer().getAppiumDriver(hub, capabilities), maxLoadTime);

	bot.setLogger(getLogger(m));
	//mobileTestParams.getLogger().info("Device connected");
	return new Object[][] { { bot } };
    }

	@AfterMethod
	public void afterMethod(ITestResult tr) throws Exception {
		MobileActionBot mobileTestParameters = (MobileActionBot) tr
				.getParameters()[0];

		mobileTestParameters.quit();
		//mobileTestParameters.getLogger().info("End");
	}
}
