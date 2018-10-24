package com.framework.config;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/***
 * 
 * @author nshevtsov
 * 
 */
public class RetryTest implements IRetryAnalyzer {
    public static int maxRetryCount;
    int current_count = 0;

    public boolean retry(ITestResult result) {
	if(result.getThrowable().getClass().equals(UnsupportedOperationException.class)){//не перезапускаем тесты, которые ещё не реализованы
	    return false;
	}
	if (current_count < maxRetryCount) {
	    current_count++;
	    return true;
	} else {
	    return false;
	}
    }
}