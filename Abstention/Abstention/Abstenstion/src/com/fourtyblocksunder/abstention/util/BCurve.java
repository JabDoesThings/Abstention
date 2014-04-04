package com.fourtyblocksunder.abstention.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 * 
 * @author Joshua Edwards
 * 
 *         Class that aids in construction and execution of Bezier Curves in an
 *         AWT enviornment.
 * 
 */
public class BCurve {

	public static BasicStroke dashed = new BasicStroke(2.0f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[] { 4,
					8 }, 0);
	
	/**
	 * The color of the BCurve's start point.
	 */
	private Color curveColorStart;

	/**
	 * The color of the BCurve's end point.
	 */
	private Color curveColorEnd;

	/**
	 * GeneralPath instance used for drawing paths.
	 */
	private static GeneralPath path = new GeneralPath();

	/**
	 * The starting point of the curve.
	 */
	private int[] curveStart;

	/**
	 * The end point of the curve.
	 */
	private int[] curveEnd;

	/**
	 * The first node in the curve.
	 */
	private int[] nodeA;

	/**
	 * The second node in the curve.
	 */
	private int[] nodeB;

	/**
	 * Main Constructor.
	 * 
	 * @param curveColor
	 * @param curveStart
	 * @param curveEnd
	 * @param nodeA
	 * @param nodeB
	 */
	BCurve(Color curveColorStart, Color curveColorEnd, int[] curveStart,
			int[] curveEnd, int[] nodeA, int[] nodeB) {
		// Set our data.
		setCurveColorStart(curveColorStart);
		setCurveColorEnd(curveColorEnd);
		setCurveStart(curveStart);
		setCurveEnd(curveEnd);
		setNodeA(nodeA);
		setNodeB(nodeB);
		// Create a new path object.
		path = new GeneralPath();
	}

	private void setCurveColorEnd(Color curveColorEnd) {
		this.curveColorEnd = curveColorEnd;
	}

	private void setCurveColorStart(Color curveColorStart) {
		this.curveColorStart = curveColorStart;
	}

	/**
	 * Main Constructor.
	 * 
	 * @param curveColor
	 * @param x1
	 *            - beginning curve X position
	 * @param y1
	 *            - beginning curve y position
	 * @param x2
	 *            - first node x position
	 * @param y2
	 *            - first node y position
	 * @param x3
	 *            - second node x position
	 * @param y3
	 *            - second node y position
	 * @param x4
	 *            - end of curve's x position
	 * @param y4
	 *            - end of curve's y position
	 */
	public BCurve(Color curveColorStart, Color curveColorEnd, int x1, int y1,
			int x2, int y2, int x3, int y3, int x4, int y4) {
		// Set our data.
		setCurveColorStart(curveColorStart);
		setCurveColorEnd(curveColorEnd);
		setCurveStart(x1, y1);
		setNodeA(x2, y2);
		setNodeB(x3, y3);
		setCurveEnd(x4, y4);
		// Create a new path object.
		path = new GeneralPath();
	}

	/**
	 * Sets NodeA.
	 * 
	 * @param nodeA
	 */
	private void setNodeA(int[] nodeA) {
		this.nodeA = nodeA;
	}

	/**
	 * Sets the NodeA coordinate.
	 * 
	 * @param x
	 * @param y
	 */
	public void setNodeA(int x, int y) {
		this.nodeA = new int[] { x, y };
	}

	/**
	 * Sets NodeB.
	 * 
	 * @param nodeB
	 */
	private void setNodeB(int[] nodeB) {
		this.nodeB = nodeB;
	}

	/**
	 * Sets the NodeB coordinate.
	 * 
	 * @param x
	 * @param y
	 */
	public void setNodeB(int x, int y) {
		this.nodeB = new int[] { x, y };
	}

	/**
	 * Sets the starting point of the BCurve.
	 * 
	 * @param curveStart
	 */
	private void setCurveStart(int[] curveStart) {
		this.curveStart = curveStart;
	}

	/**
	 * Sets the starting point of the BCurve.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCurveStart(int x, int y) {
		this.curveStart = new int[] { x, y };
	}

	/**
	 * Sets the end point of the BCurve.
	 * 
	 * @param curveEnd
	 */
	private void setCurveEnd(int[] curveEnd) {
		this.curveEnd = curveEnd;
	}

	/**
	 * Sets the end point of the BCurve.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCurveEnd(int x, int y) {
		this.curveEnd = new int[] { x, y };
	}

	/**
	 * Returns the starting position of the curve.
	 * 
	 * @return
	 */
	public int[] getCurveStart() {
		return this.curveStart;
	}

	/**
	 * Returns the first Node's position.
	 * 
	 * @return
	 */
	public int[] getNodeA() {
		return this.nodeA;
	}

	/**
	 * Returns the second Node's position.
	 * 
	 * @return
	 */
	public int[] getNodeB() {
		return this.nodeB;
	}

	/**
	 * Returns the end position of the curve.
	 * 
	 * @return
	 */
	public int[] getCurveEnd() {
		return this.curveEnd;
	}

	/**
	 * Draws the BCurve.
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {

		// Get our points.
		int[] curveStart = getCurveStart();
		int[] nodeA = getNodeA();
		int[] nodeB = getNodeB();
		int[] curveEnd = getCurveEnd();

		// Draw the curve.
		drawCurve(g, null, false, getColorCurveStart(),
				getColorCurveEnd(), curveStart[0], curveStart[1], nodeA[0],
				nodeA[1], nodeB[0], nodeB[1], curveEnd[0], curveEnd[1], 1.0F);
	}

	private Color getColorCurveEnd() {
		return this.curveColorEnd;
	}

	private Color getColorCurveStart() {
		return this.curveColorStart;
	}

	static Color shadow = new Color(10, 10, 10, 110);

	static BasicStroke strokeShadow = new BasicStroke(5);
	static BasicStroke strokeLine = new BasicStroke(3);
	static BasicStroke strokeOutline = new BasicStroke(0.1F);

	public static BasicStroke solid = new BasicStroke(0.4F);

	/**
	 * Statically draws a Bezier Curve.
	 * 
	 * @param g
	 * @param stroke
	 * @param path
	 * @param colorCurve
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public static void drawCurve(Graphics g, BasicStroke stroke,
			boolean useStroke, Color curveStart,
			Color curveEnd, int x1, int y1, int x2, int y2, int x3, int y3,
			int x4, int y4, float cameraScale) {
		// Temporarily store our previous color to set when done drawing.
		Color lastColor = g.getColor();

		//dashed = new BasicStroke(2.0f,
		////		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[] { 4 * cameraScale,
		//				8 * cameraScale }, 2.5F - cameraScale);
		// Create a 2D Graphics instance.
		Graphics2D g2d = (Graphics2D) g;

		if (useStroke) {
			BCurve.drawLine(g2d, stroke, shadow, shadow, x1, y1, x2, y2,
					x3, y3, x4, y4, 2, 2);
			BCurve.drawLine(g2d, stroke, curveStart, curveEnd, x1, y1,
					x2, y2, x3, y3, x4, y4, 0, 0);
		} else {
			BCurve.drawLine(g2d, strokeShadow, shadow, shadow, x1, y1,
					x2, y2, x3, y3, x4, y4, 2, 2);
			BCurve.drawLine(g2d, strokeLine, curveStart, curveEnd, x1,
					y1, x2, y2, x3, y3, x4, y4, 0, 0);
		}

		// Set our color to the last color.
		g.setColor(lastColor);
	}

	public static void drawLine(Graphics2D g2d,
			BasicStroke stroke, Color startColor, Color endColor, int x1,
			int y1, int x2, int y2, int x3, int y3, int x4, int y4,
			int offsetX, int offsetY) {
		path.reset();

		g2d.setStroke(stroke);
		// Sets the curve to a specific color.
		GradientPaint GP = new GradientPaint(x1, y1, startColor, x4, y4,
				endColor, true);
		g2d.setPaint(GP);

		// Move the path to the starting node.
		path.moveTo(x1 + offsetX, y1 + offsetY);

		// Set the curve of the path to the 3 nodes: A, B, End.
		path.curveTo(x2 + offsetX, y2 + offsetY, x3 + offsetX, y3 + offsetY, x4
				+ offsetX, y4 + offsetY);
		// Draw the path.
		g2d.draw(path);

		g2d.setPaint(null);
		g2d.setStroke(new BasicStroke(1));
	}

}
