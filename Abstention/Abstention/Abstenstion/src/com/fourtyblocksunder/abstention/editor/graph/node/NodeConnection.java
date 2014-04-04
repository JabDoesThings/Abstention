package com.fourtyblocksunder.abstention.editor.graph.node;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.GraphComponent;
import com.fourtyblocksunder.abstention.util.Cache;
import com.fourtyblocksunder.abstention.util.RenderNodeUtil;

public class NodeConnection extends GraphComponent {

	private Cache<Rectangle> cachedCurve;

	private Cache<BufferedImage> cachedRender;

	private boolean visible = false;

	private String key = "";

	private Node nodeOutput;

	private Node nodeInput;

	private int[] locationOutput = new int[] { 0, 0 };

	private int[] locationInput = new int[] { 0, 0 };

	public NodeConnection(Node nodeOutput, Node nodeInput) {
		this.nodeOutput = nodeOutput;
		this.nodeInput = nodeInput;
		key = nodeOutput.getName() + " " + nodeInput.getName();
		cachedCurve = new Cache<>();
		cachedRender = new Cache<>();
	}
	
	public Cache<BufferedImage> getCachedRender()
	{
		return this.cachedRender;
	}

	public boolean processChange(GraphCamera camera) {
		locationOutput = nodeOutput.getLocationOutput();
		locationInput = nodeInput.getLocationInput();
		String newKey = " " + locationInput[0] + " " + locationInput[1] + " "
				+ locationOutput[0] + " " + locationOutput[1];

		if (!cachedCurve.keysMatch(newKey)) {
			updateCache(newKey, camera);
		}

		Rectangle rect = (Rectangle) cachedCurve.getCache();
		return visible = !camera.isWithinScreen(rect);
	}

	private void updateCache(String newKey, GraphCamera camera) {
		int[] curveData = RenderNodeUtil.getBCurvePointsFromNodes(
				nodeOutput.getLocationOutput(), nodeInput.getLocationInput(), camera);

		int[] point1 = new int[2];
		int[] point2 = new int[2];

		point1[0] = Math.min(Math.min(curveData[0], curveData[2]),
				Math.min(curveData[4], curveData[6]));
		point1[1] = Math.min(Math.min(curveData[1], curveData[3]),
				Math.min(curveData[5], curveData[7]));

		point2[0] = Math.max(Math.max(curveData[0], curveData[2]),
				Math.max(curveData[4], curveData[6]));
		point2[1] = Math.max(Math.max(curveData[1], curveData[3]),
				Math.max(curveData[5], curveData[7]));

		Rectangle rect = new Rectangle(point1[0], point1[1], Math.abs(point2[0]
				+ 1 - point1[0]), Math.abs(point2[1] + 1 - point1[1]));
		cachedCurve.setCache(rect, newKey);
	}

	/**
	 * 
	 * @param g2d
	 * @param cameraLocation
	 * @param cameraScale
	 */
	public void drawLine(Graphics2D g2d, GraphCamera camera) {

		Color curveStart = nodeOutput.getColor();
		if (nodeOutput.isSelected()) {
			curveStart = nodeOutput.getColorSelected();
		}

		Color curveEnd = nodeInput.getColor();
		if (nodeInput.isSelected()) {
			curveEnd = nodeInput.getColorSelected();
		}

		RenderNodeUtil.drawNodePath(g2d, curveStart, curveEnd, false, false,
				locationOutput, locationInput, camera);

	}

	public String getKey() {
		return this.key;
	}

	@Override
	public boolean changed() {
		return !locationsMatch();
	}

	@Override
	public void setChanged() {

		setLastLocation(getLocation());
	}

	public boolean locationsMatch() {
		int[] lastLocation = getLastLocation();
		int[] location = getLocation();
		boolean match = true;
		for (int index = 0; index < lastLocation.length; index++) {
			if (location[index] != lastLocation[index]) {
				match = false;
				break;
			}
		}
		return match;
	}

	public int[] getLocation() {
		return new int[] { locationOutput[0], locationOutput[1], locationInput[0], locationInput[1] };
	}

	public void update(GraphCamera camera) {
		if (camera.changed()) {
			processChange(camera);
		}
	}

	public boolean isVisible() {
		return this.visible;
	}
}
