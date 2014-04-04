package com.fourtyblocksunder.abstention.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;

public class GraphicsUtil {

	public static Color alpha = new Color(0, 0, 0, 0);

	/**
	 * @author http://stackoverflow.com/questions/14551534/how-to-draw-a-round-
	 *         rectangle-in-java-with-normal-rectangle-outline
	 * @param nodeColor
	 * @return
	 */
	public static BufferedImage createMaskedImage(int width, int height,
			Color nodeColor) {
		System.out.println(nodeColor);
		BufferedImage outter = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = outter.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(nodeColor);
		g2d.fillRect(0, 0, width, height);
		g2d.dispose();
		BufferedImage inner = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		g2d = inner.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(nodeColor);
		g2d.fillRoundRect(3, 3, width - 6, height - 6, 6, 6);
		g2d.dispose();

		return applyMask(outter, inner, AlphaComposite.DST_OUT);
	}

	/**
	 * @author http://stackoverflow.com/questions/14551534/how-to-draw-a-round-
	 *         rectangle-in-java-with-normal-rectangle-outline
	 * @param sourceImage
	 * @param maskImage
	 * @param method
	 * @return
	 */
	public static BufferedImage applyMask(BufferedImage sourceImage,
			BufferedImage maskImage, int method) {
		BufferedImage maskedImage = null;
		if (sourceImage != null) {

			int width = maskImage.getWidth();
			int height = maskImage.getHeight();

			maskedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D mg = maskedImage.createGraphics();

			int x = (width - sourceImage.getWidth()) / 2;
			int y = (height - sourceImage.getHeight()) / 2;

			mg.drawImage(sourceImage, x, y, null);
			mg.setComposite(AlphaComposite.getInstance(method));
			mg.drawImage(maskImage, 0, 0, null);
			mg.dispose();
		}

		return maskedImage;
	}

	public static BufferedImage flipImageHorizontal(BufferedImage image) {
		BufferedImage imageFlipped = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				imageFlipped.setRGB(x, y, image.getRGB(63 - x, y));
			}
		}
		return imageFlipped;
	}

	public static Color getColorDifference(Color color, int amount) {
		// Get our color values from the node color and subtract it to form a
		// smooth gradient.
		int red = color.getRed() + amount;
		if (red < 0) {
			red = 0;
		} else if (red > 255) {
			red = 255;
		}
		int green = color.getGreen() + amount;
		if (green < 0) {
			green = 0;
		} else if (green > 255) {
			green = 255;
		}
		int blue = color.getBlue() + amount;
		if (blue < 0) {
			blue = 0;
		} else if (blue > 255) {
			blue = 255;
		}
		int alpha = color.getAlpha() + amount;
		if (alpha < 0) {
			alpha = 0;
		} else if (alpha > 255) {
			alpha = 255;
		}
		// Return our new Color object.
		return new Color(red, green, blue, alpha);
	}

	/**
	 * Sets the Graphics2D instance's anti-alias feature.
	 * 
	 * @param g2d
	 * @param use
	 */
	public static void setAntiAlias(Graphics2D g2d, boolean use) {
		if (use) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	/**
	 * Sets the Graphics2D instance's anti-alias feature.
	 * 
	 * @param g2d
	 * @param use
	 */
	public static void setRenderQuality(Graphics2D g2d, Key hint, Object setting) {
		g2d.setRenderingHint(hint, setting);
	}

}
