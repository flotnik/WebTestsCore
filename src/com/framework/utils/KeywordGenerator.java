package com.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class KeywordGenerator {

    public static String getKeyword() {
	UtilFunctions.sleep(1);
	return Long.toHexString(Double.doubleToLongBits(new Random(new Date().getTime()).nextDouble()));
    }

    public static String getRandomName() {
	String hash = "GFDSOPLKJHAZQWERTYUIXCVBNM";
	int ind1 = 0;
	String name = "";
	for (int i = 0; i < 1; i++) {
	    for (int j = 0; j < 3; j++) {
		ind1 = (int) (Math.random() * 1000) % (hash.length() - 1);
		name += hash.charAt(ind1);
	    }
	    name += '-';
	}
	name += new SimpleDateFormat("HHmmss").format(new Date());
	return name;
    }

    public static int GetRandomInt(int min, int max) {
	UtilFunctions.sleep(1);
	Random rn = new Random(new Date().getTime());
	int n = max - min + 1;
	int i = rn.nextInt() % n;
	return Math.abs(min + i);
    }
}
