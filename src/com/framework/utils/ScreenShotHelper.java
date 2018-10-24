package com.framework.utils;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class ScreenShotHelper {

    String screenShotsRoot;

    final String ext = "png";

    public ScreenShotHelper(String screenShotsRoot) throws Exception {
	super();
	File file = new File(screenShotsRoot);
	FileUtils.forceMkdir(file);
	if (!file.exists()) {
	    throw new Exception("can't create " + file.getAbsolutePath());
	}
	this.screenShotsRoot = file.getAbsolutePath() + "/";
    }

    public void CaptureScreenShotGUI(String fileName) throws Exception {
	CaptureScreenShotGUI(fileName, ext);
    }

    public void CaptureScreenShotGUI(String fileName, String ext) throws Exception {
	try {
	    Robot robot = new Robot();
	    Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	    BufferedImage bufferedImage = robot.createScreenCapture(captureSize);

	    save("G_" + fileName, bufferedImage, ext);
	} catch (Exception e) {
	    throw e;
	}
    }

    private void save(String fileName, BufferedImage image, String ext) throws IOException {
	File file = new File(screenShotsRoot + fileName + "." + ext);
	try {
	    ImageIO.write(image, ext, file); // ignore returned boolean
	} catch (IOException e) {
	    System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
	    throw e;
	}
    }
}
