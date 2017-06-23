package com.toni.graphics;

import java.awt.image.BufferedImage;

public class MyThread extends Thread {
	private BufferedImage[] images = null;
	private int index;
	private String type;

	public MyThread(BufferedImage[] image, int index, String type) {
		this.images = image;
		this.index = index;
		this.type = type;
	}

	public void run() {

		switch (type) {
		case "blur":
			Blur bl = new Blur(images,index);
			bl.blur();
			break;
		case "grayscale":
			Grayscale grscl = new Grayscale(images[index]);
			grscl.convert();
			break;
		case "median":
			MedianFilter filter = new MedianFilter(images[index]);
			filter.filter();
			break;
		}
	}

}
