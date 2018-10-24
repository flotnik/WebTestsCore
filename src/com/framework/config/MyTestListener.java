package com.framework.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.framework.common.AbstractActionBot;
import com.framework.common.QtActionBot;
import com.framework.common.WebActionBot;
import com.framework.utils.UtilFunctions;

import ch.qos.logback.core.FileAppender;

/***
 * 
 * @author nshevtsov
 * 
 */
public class MyTestListener implements ITestListener {

    Date begin;
    Date end;

    public HashMap<String, String> failedTests;
    public HashMap<String, Integer> passedTests;
    public HashMap<String, Integer> skipedTests;
    public HashMap<String, Integer> notImplTests;

    public MyTestListener() throws IOException {
	super();	
	failedTests = new HashMap<>();
	passedTests = new HashMap<>();
	skipedTests = new HashMap<>();
	notImplTests = new HashMap<>();
    }

    public void onFinish(ITestContext arg0) {
	end = new Date();	
	int failed = failedTests.size();
	int passed = passedTests.size();
	int skipped = skipedTests.size();
	int notImpl = notImplTests.size();

	StringBuilder sb = new StringBuilder();
	sb = sb.append(System.lineSeparator()).append("Time spent: " + UtilFunctions.TimeBetween(begin, end)).append(System.lineSeparator());
	sb = sb.append("Total : " + (failed + skipped + passed + notImpl)).append(System.lineSeparator());
	sb = sb.append("Passed : " + passed).append(System.lineSeparator());
	sb = sb.append("Failed : " + failed).append(System.lineSeparator());
	for (Entry<String, String> e : failedTests.entrySet()) {
	    sb = sb.append("   " + e.getKey()).append(" : ").append(e.getValue()).append(System.lineSeparator());
	}
	sb = sb.append("Skipped: " + skipped).append(System.lineSeparator());
	for (Entry<String, Integer> e : skipedTests.entrySet()) {
	    sb = sb.append("   " + e.getKey()).append(System.lineSeparator());
	}	
	sb = sb.append("Not implemented : " + notImpl).append(System.lineSeparator());
	for (Entry<String, Integer> e : notImplTests.entrySet()) {
	    sb = sb.append("   " + e.getKey()).append(System.lineSeparator());
	}	
	System.out.println(sb.toString());
	try {
	    FileUtils.writeStringToFile(new File(arg0.getOutputDirectory()+"/"+arg0.getCurrentXmlTest().getName()+".txt"), sb.toString(), Charset.forName("UTF-8"));
	} catch (IOException e1) {	    
	    e1.printStackTrace();
	}
    }

    public void onStart(ITestContext arg0) {
	begin = new Date();
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
    }

    public void onTestFailure(ITestResult arg0) {
	System.out.println("onTestFailure " + arg0.getMethod().getMethodName());
	AbstractActionBot params = (AbstractActionBot) arg0.getParameters()[0];
	if (params instanceof WebActionBot) {
	    params = (WebActionBot) arg0.getParameters()[0];
	} else if (params instanceof QtActionBot) {
	    params = (QtActionBot) arg0.getParameters()[0];
	} else	{
	    try {
		throw new Exception("unknown params type " + params.getClass());
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
		
	if (arg0.getThrowable().getClass().equals(UnsupportedOperationException.class)) {
	    notImplTests.put(arg0.getClass().getSimpleName() + "." + arg0.getMethod().getMethodName(), 0);
	    params.getLogger().error("ERROR: ", arg0.getThrowable());
	    return;
	}

	failedTests.put(arg0.getMethod().getRealClass().getSimpleName() + "." + arg0.getMethod().getMethodName(), arg0.getThrowable().getMessage());

	if (params instanceof WebActionBot) {
	    try {
		String screen_name = arg0.getMethod().getMethodName();
		FileAppender<ILoggingEvent> f = (FileAppender<ILoggingEvent>) params.getLogger().getAppender(arg0.getMethod().getMethodName());
		File root = new File(f.getFile()).getParentFile();
		screen_name = ((WebActionBot) params).getScreenShot(screen_name, root);
		params.getLogger().debug("SCREENSHOT FILE NAME = " + screen_name);
		params.getLogger().error("ERROR: ", arg0.getThrowable());
	    } catch (Exception e1) {
		params.getLogger().error("ERROR: NO SCREENSHOT ADDED becouse of exception: " + e1.getLocalizedMessage());
	    }
	}
	params.getLogger().error(arg0.getThrowable().getMessage());
    }

    public void onTestSkipped(ITestResult arg0) {
	System.out.println("onTestSkipped " + arg0.getMethod().getMethodName());
	skipedTests.put(arg0.getMethod().getRealClass().getSimpleName() + "." + arg0.getMethod().getMethodName(), 0);
    }

    public void onTestStart(ITestResult arg0) {
    }

    public void onTestSuccess(ITestResult arg0) {
	System.out.println("onTestSuccess " + arg0.getMethod().getMethodName());
	failedTests.remove(arg0.getMethod().getMethodName());
	passedTests.put(arg0.getMethod().getMethodName(), 0);
    }

}
