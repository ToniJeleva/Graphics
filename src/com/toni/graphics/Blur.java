package com.toni.graphics;

import java.awt.image.BufferedImage;
/*
 * Представя филтър за замазване (blur) на изображение 
 */
public class Blur {
	private BufferedImage[] images;
	private int index;
	private int width;
	private int height;
	private int[] src;
	private int[] dst;
	private int blurWidth = 15;

	public Blur(BufferedImage[] images, int index) {
		this.images = images;
		this.index = index;
		width = images[index].getWidth();
		height = images[index].getHeight();

		src = images[index].getRGB(0, 0, width, height, null, 0, width);
		dst = new int[src.length];
	}

	public void blur() {

		int sidePixels = (blurWidth - 1) / 2;
		for (int index = 0; index < 0 + src.length; index++) {
			// Calculate average.
			float rt = 0, gt = 0, bt = 0;
			for (int mi = -sidePixels; mi <= sidePixels; mi++) {
				int mindex = Math.min(Math.max(mi + index, 0), src.length - 1);
				int pixel = src[mindex];
				rt += (float) ((pixel & 0x00ff0000) >> 16) / blurWidth;
				gt += (float) ((pixel & 0x0000ff00) >> 8) / blurWidth;
				bt += (float) ((pixel & 0x000000ff) >> 0) / blurWidth;
			}

			// Re-assemble destination pixel.
			int dpixel = (0xff000000) | (((int) rt) << 16) | (((int) gt) << 8) | (((int) bt) << 0);
			dst[index] = dpixel;

		}
		images[index] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		images[index].setRGB(0, 0, width, height, dst, 0, width);
	}

}
