package com.fourtyblocksunder.abstention.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;

/**
 * 
 * @author Joshua Edwards Class designed to store the render methods for Nodes.
 */
public class RenderNodeUtil {

	/**
	 * Draws a box container in the node's position in camera space.
	 * 
	 * @param g2d
	 * @param nodeData
	 * @param colorNode
	 * @param colorShadow
	 * @param innerBox
	 * @param colorInterior
	 * @param shadow
	 * @param selected
	 */
	public static void drawNodeBox(Graphics2D g2d, int x, int y, int width,
			int height, Color colorNode, Color colorShadow, boolean innerBox,
			Color colorInterior, boolean shadow, boolean selected,
			int headerOffset, GraphCamera camera) {
		// Get the last color to set when done with operations.
		Color lastColor = g2d.getColor();

		// Get the last stroke.
		Stroke lastStroke = g2d.getStroke();

		// Gradient color.
		Color colorGradient = GraphicsUtil.getColorDifference(colorNode, -20);
		Color colorHeader = GraphicsUtil.getColorDifference(colorNode, 20);

		Paint paintHeader = new GradientPaint(x, y
				+ (int) ((headerOffset) * camera.getScale()), colorHeader, x, y
				+ (int) ((headerOffset + 4) * camera.getScale()), colorGradient);

		// Set our stroke to a line with 6 pixels in width.
		g2d.setStroke(new BasicStroke(6));

		/*
		 * If we want to draw a shadow, AND if the node is NOT selected, draw
		 * our shadow.
		 */
		if (shadow && !selected) {
			// Set the shadow color.
			g2d.setColor(colorShadow);

			/*
			 * Draw a rectangle slightly downward and right, slightly larger
			 * with larger corner curves to have a nice cosmetic shadow effect.
			 */
			g2d.fillRoundRect(x + 1, y + 1, width + 4, height + 4, 24, 24);
		}

		// Set our color to the default node's color.
		g2d.setColor(colorNode);
		g2d.setPaint(paintHeader);
		// Draw our base rectangle.
		g2d.fillRoundRect(x, y, width, height, 16, 16);
		g2d.setPaint(null);
		// Set our color to shadow again.
		g2d.setColor(colorShadow);

		// Set our stroke to a line with 2 pixels in width.
		g2d.setStroke(new BasicStroke(2));

		/*
		 * Draw a line around the edge of our base rectangle for cosmetics /
		 * outlining.
		 */
		g2d.drawRoundRect(x, y, width - 1, height - 1, 16, 16);

		// If we want to draw a inner box or not.
		if (innerBox) {
			// Set our color to black. TODO: Color option for inner box.
			g2d.setColor(colorInterior);
			// Draw our inner box 4 pixels inside the base rectangle.
			g2d.fillRoundRect(x + 4, y + 4, width - 8, height - 8, 8, 8);
		}

		// Sets the graphics object's original parameters back.
		g2d.setStroke(lastStroke);
		g2d.setColor(lastColor);
	}

	public static void drawTitle(Graphics2D g2d, String title, int x, int y,
			int fontSize, Color colorText, Color colorShadow, boolean shadow,
			GraphCamera camera) {

		g2d.setFont(new Font(null, Font.LAYOUT_NO_START_CONTEXT,
				(int) (fontSize * 1 + (camera.getScale()))));
		if (shadow) {
			g2d.setColor(colorShadow);
			g2d.drawString(title, x + 8, y - 1);
		}
		g2d.setColor(colorText);
		g2d.drawString(title, x + 6, y - 3);

	}

	/**
	 * returns a int[x1,y1,x2,y2,x3,y3,x4,y4] array.
	 * 
	 * @param node1
	 * @param node2
	 * @param cameraLocation
	 * @param cameraScale
	 * @return
	 */
	public static int[] getBCurvePointsFromNodes(int[] locationOutput,
			int[] locationInput, GraphCamera camera) {

		int distanceX = Math.abs(locationOutput[0] - locationInput[0]);

		int leastX = Math.min(locationOutput[0], locationInput[0]);

		float scale = camera.getScale() + .1F;

		int reflectValue = (int) (distanceX * 1.8F);
		int effectYValue = 0;

		if (reflectValue > (int) (768 * scale)) {
			reflectValue = (int) (768 * scale);
		}

		effectYValue = (int) ((reflectValue - (768 * (scale))) * (Math
				.abs((locationOutput[1] - locationInput[1])) * 0.002F));

		int x1 = locationOutput[0];
		int y1 = locationOutput[1];

		int x2 = leastX + (distanceX / 2);
		int y2 = locationOutput[1];

		if (x2 < locationOutput[0]) {
			x2 += reflectValue;
			if (locationInput[1] > locationOutput[1]) {
				y2 -= effectYValue;
			} else {
				y2 += effectYValue;

			}
		}

		int x3 = leastX + (distanceX / 2);
		int y3 = locationInput[1];

		if (x3 > locationInput[0]) {
			x3 -= reflectValue;
			if (locationInput[1] > locationOutput[1]) {
				y3 += effectYValue;
			} else {
				y3 -= effectYValue;
			}
		}

		int x4 = locationInput[0];
		int y4 = locationInput[1];

		return new int[] { x1, y1, x2, y2, x3, y3, x4, y4 };
	}

	public static void drawNodePath(Graphics2D g2d, Color curveStart,
			Color curveEnd, boolean drawOutputOval, boolean drawInputOval,
			int[] locationOutput, int[] locationInput, GraphCamera camera) {

		int[] cameraLocation = camera.getLocation();
		float cameraScale = camera.getScale();

		int[] bCurvePoints = getBCurvePointsFromNodes(locationOutput,
				locationInput, camera);

		BCurve.drawCurve(g2d, BCurve.solid, true, curveStart, curveEnd,
				bCurvePoints[0], bCurvePoints[1], bCurvePoints[2],
				bCurvePoints[3], bCurvePoints[4], bCurvePoints[5],
				bCurvePoints[6], bCurvePoints[7], camera.getScale());

		int pointRadius = (int) (7 * cameraScale);

		if (drawOutputOval) {
			g2d.setColor(curveStart);
			g2d.fillOval(bCurvePoints[0] - pointRadius, bCurvePoints[1]
					- pointRadius, pointRadius * 2, pointRadius * 2);
		}
		if (drawInputOval) {
			g2d.setColor(curveEnd);
			g2d.fillOval(bCurvePoints[6] - pointRadius, bCurvePoints[7]
					- pointRadius, pointRadius * 2, pointRadius * 2);
		}

	}

	/**
	 * Renders an image with a random noise generation.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage renderStatic(int width, int height) {
		Random random = new Random();
		BufferedImage imageStatic = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int value = random.nextInt(128) + 32;
				Color color = new Color(value, value, value);
				imageStatic.setRGB(x, y, color.getRGB());
			}
		}
		return imageStatic;
	}

	public static int[] pointToInts(Point point) {
		return new int[] { (int) point.getX(), (int) point.getY() };
	}
}
