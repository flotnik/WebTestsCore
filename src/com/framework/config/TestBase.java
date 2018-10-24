package com.framework.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import com.framework.common.DriverInitializer;
import com.framework.common.WebActionBot;
import com.params_manager.DataBlock;
import com.params_manager.DataBlocks;
import com.params_manager.ParamNotFoundException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/***
 * 
 * @author nshevtsov
 * 
 */
public class TestBase {
	// устанавливаются для всего suite в beforeSuite
	public static String output_folder;
	public static String log_level;
	public static String browser;
	public static DataBlocks db = new DataBlocks();

	public Logger getLogger(Method m) {
		String filename = m.getDeclaringClass().getCanonicalName() + "." + m.getName() + ".log";
		File logFile = new File(TestBase.output_folder + "/logs", filename);

		LoggerContext loggerContext = new LoggerContext();
		FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
		fileAppender.setContext(loggerContext);
		fileAppender.setName(m.getName());
		fileAppender.setFile(logFile.getAbsolutePath());
		PatternLayout pl = new PatternLayout();

		pl.setPattern("[%d] %-5level : %message%n");
		pl.setContext(loggerContext);
		pl.start();
		fileAppender.setLayout(pl);
		fileAppender.start();

		Logger logbackLogger = loggerContext.getLogger("");
		logbackLogger.addAppender(fileAppender);
		logbackLogger.setLevel(Level.toLevel(log_level, Level.DEBUG));

		logbackLogger.info("==============================================================================");
		logbackLogger.info("Before browser start");
		return logbackLogger;
	}

	@DataProvider(name = "actionBot")
	public Object[][] dataSet(Method m) throws Throwable {
		DataBlock block = db.GetBlock("MAIN");
		String platform =  block.getString("platform");//TODO на всякий случай
		String hub = 		block.getString("hub");
		int maxLoadTime =  block.getInteger("maxLoadTime");
		String baseURL =   block.getString("baseURL");
		String browser = (TestBase.browser == null? block.getString("browser"):TestBase.browser);
		boolean headless = block.getString("headless").equalsIgnoreCase("true")?true:false;

		Capabilities capabilities;
		// initializeBrowserType
		if (browser.equalsIgnoreCase("firefox")) {
			FirefoxOptions capabilitiesFF = new FirefoxOptions();
			capabilitiesFF.setAcceptInsecureCerts(true);
			capabilitiesFF.setHeadless(headless);

			try{
				String firefoxProfile =   block.getString("firefoxProfile");

				FirefoxProfile profile = new ProfilesIni().getProfile(firefoxProfile);
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(false);

				capabilitiesFF.setProfile(profile);
			}catch(ParamNotFoundException e){

			}

			//capabilities = DesiredCapabilities.firefox();
			//FirefoxProfile firefoxProfile = new FirefoxProfile();
			// firefoxProfile.setPreference("browser.tabs.remote.autostart", false);
			// firefoxProfile.setPreference("browser.tabs.remote.autostart.1", false);
			// firefoxProfile.setPreference("browser.tabs.remote.autostart.2", false);
			// firefoxProfile.setPreference("browser.tabs.remote.force-enable", false);
			//capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

			// ProfilesIni profileIni= new ProfilesIni();
			// FirefoxProfile profile = profileIni.getProfile("default");
			// capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			//capabilities.setJavascriptEnabled(true);
			//capabilities.setBrowserName(BrowserType.FIREFOX);
			capabilities=capabilitiesFF;
		} else if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions capabilitiesFF = new ChromeOptions();
			capabilitiesFF.setHeadless(headless);
			capabilitiesFF.setAcceptInsecureCerts(true);

			capabilities = capabilitiesFF;
		} else if (browser.equalsIgnoreCase("IE")) {
			capabilities = DesiredCapabilities.internetExplorer();
			
			((DesiredCapabilities)capabilities).setCapability("ie.ensureCleanSession", true);
			// capability.setCapability("ignoreProtectedModeSettings", true);
			// capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			((DesiredCapabilities)capabilities).setCapability("requireWindowFocus", true);
			((DesiredCapabilities)capabilities).setCapability("enablePersistentHover", false);
		} else if (browser.equalsIgnoreCase("EDGE")) {
			capabilities = DesiredCapabilities.edge();
			((DesiredCapabilities)capabilities).setCapability("nativeEvents", true);
			((DesiredCapabilities)capabilities).setCapability("cssSelectorsEnabled", true);
			((DesiredCapabilities)capabilities).setCapability("acceptSslCerts", true);
			((DesiredCapabilities)capabilities).setCapability("javascriptEnabled", true);
			((DesiredCapabilities)capabilities).setCapability("requireWindowFocus", true);
			((DesiredCapabilities)capabilities).setCapability("enablePersistentHover", false);
			/*
			 * System.out.println("=============================="); Map<String,
			 * ?> map = capabilities.asMap(); for (Entry<String, ?> e :
			 * map.entrySet()) { System.out.println(e.getKey() + " " +
			 * e.getValue()); }
			 */
		} else {
			throw new Exception("unknown type of browser in TestParameters");
		}
		/*// initializePlatform
		if (platform.contains("linux")) {
			((DesiredCapabilities)capabilities).setPlatform(Platform.LINUX);
		} else if (platform.contains("windows")) {
			((DesiredCapabilities)capabilities).setPlatform(Platform.WINDOWS);
		} else if (platform.contains("mac")) {
			((DesiredCapabilities)capabilities).setPlatform(Platform.MAC);
		} else if (platform.contains("any")) {
			((DesiredCapabilities)capabilities).setPlatform(Platform.ANY);
		} else if (platform.contains("win10")) {
			((DesiredCapabilities)capabilities).setPlatform(Platform.WIN10);
		} else {
			throw new Exception("unknown platform in TestParameters");
		}*/

		WebActionBot bot = new WebActionBot(new DriverInitializer().getWebDriver(hub, capabilities), maxLoadTime, baseURL, getLogger(m));
		bot.getLogger().info("Browser started");
		return new Object[][] { { bot } };
	}

	@Parameters({ "maxRetryCount", "log_level", "data_file"})
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext context, String maxRetryCount, String log_level, String data_file) throws Exception {
		System.out.println("BeforeSuite...");
		TestBase.output_folder = new File(context.getOutputDirectory() + "\\Results").getAbsolutePath();
		TestBase.log_level = log_level;
		RetryTest.maxRetryCount = Integer.parseInt(maxRetryCount);

		try {
			db.loadParams(new File(data_file), null);
		} catch (Exception e) {
			System.out.println("Can't read data file " + data_file);
			e.printStackTrace();
			System.exit(1);
		}

		File browser_info_file = new File("browser");
		if(browser_info_file.exists()){
			TestBase.browser = FileUtils.readFileToString(browser_info_file, Charset.forName("utf-8")).trim();
		}
		
		for (ITestNGMethod method : context.getAllTestMethods()) {
			method.setRetryAnalyzer(new RetryTest());
		}
		System.out.println("BeforeSuite OK");
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite(ITestContext context) throws Exception {
		/*
		 * System.out.println("AfterSuite...");
		 * System.out.println("AfterSuite OK");
		 */
	}

	@BeforeClass
	public void beforeClass() {
		//System.out.println("beforeClass " + this.getClass().getName() + "...");
		//System.out.println("beforeClass " + this.getClass().getName() + " OK");
	}

	@BeforeMethod
	public void beforeMethod(Method method) throws Exception {
		System.out.println("Start of test: " + method.getName());
	}

	@AfterMethod
	public void afterMethod(ITestResult tr) throws Exception {
		WebActionBot bot = (WebActionBot) tr.getParameters()[0];

		bot.quit();
		bot.getLogger().info("End");
	}
}
