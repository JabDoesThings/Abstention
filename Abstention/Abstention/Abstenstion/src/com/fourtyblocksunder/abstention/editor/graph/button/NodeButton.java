package com.fourtyblocksunder.abstention.editor.graph.button;

import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;

/**
 * Class designed to work with button mechanics in nodes.
 * 
 * @author Main
 * 
 */
public class NodeButton extends ToggledButton {

	private Node node;

	public NodeButton(Node node)
	{
		setNode(node);
	}
	
	/**
	 * Transforms our localized button data to data used for rendering and
	 * locating. [0] = x [1] = y [2] = width [3] = scale
	 * 
	 * @param button
	 * @param camera
	 * @return
	 */
	public static int[] getCameraSpace(NodeButton button, GraphCamera camera) {
		// Our node's location in local-space.
		int[] location = button.getNode().getLocation();

		int[] buttonLocation = button.getLocation();

		location[0] += buttonLocation[0];
		location[1] += buttonLocation[1];

		// Our node's width, and height.
		int width = button.getWidth();
		int height = button.getHeight();

		// Our camera data.
		int[] cameraLocation = camera.getLocation();
		float cameraScale = camera.getScale();

		// x = ((locationInLocalSpaceX + cameraLocationInSpaceX) * cameraScale);
		int x = (int) ((location[0] + cameraLocation[0]) * cameraScale);

		// y = ((locationInLocalSpaceY + cameraLocationInSpaceY) * cameraScale);
		int y = (int) ((location[1] + cameraLocation[1]) * cameraScale);

		// Multiply width * height by the scale.
		int w = (int) (width * cameraScale);
		int h = (int) (height * cameraScale);

		// Return our data as a int[4].
		return new int[] { x, y, w, h };
	}
	
	/**
	 * Transforms our localized button data to data used for rendering and
	 * locating. [0] = x [1] = y [2] = width [3] = scale
	 * 
	 * @param button
	 * @param camera
	 * @return
	 */
	public static int[] getCameraSpaceCache(int nodeX, int nodeY, NodeButton button, GraphCamera camera) {
		// Our node's location in local-space.

		int[] buttonLocation = button.getLocation();

		buttonLocation[0] += nodeX;
		buttonLocation[1] += nodeY;

		// Our node's width, and height.
		int width = button.getWidth();
		int height = button.getHeight();

		// Our camera data.
		int[] cameraLocation = camera.getLocation();
		float cameraScale = camera.getScale();

		// x = ((locationInLocalSpaceX + cameraLocationInSpaceX) * cameraScale);
		int x = (int) ((nodeX + cameraLocation[0]) * cameraScale);

		// y = ((locationInLocalSpaceY + cameraLocationInSpaceY) * cameraScale);
		int y = (int) ((nodeY + cameraLocation[1]) * cameraScale);

		// Multiply width * height by the scale.
		int w = (int) (width * cameraScale);
		int h = (int) (height * cameraScale);

		// Return our data as a int[4].
		return new int[] { x, y, w, h };
	}


	public boolean containsMouse(int[] mousePressed, GraphCamera camera) {
		
		int[] buttonData = getCameraSpace(this, camera);
		return containsPoint(buttonData, mousePressed);
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

}
