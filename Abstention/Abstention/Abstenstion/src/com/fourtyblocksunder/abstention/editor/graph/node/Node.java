package com.fourtyblocksunder.abstention.editor.graph.node;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.data.NodeData;
import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.GraphComponent;
import com.fourtyblocksunder.abstention.editor.graph.GraphKeys;
import com.fourtyblocksunder.abstention.util.Cache;
import com.fourtyblocksunder.abstention.util.CursorController;
import com.fourtyblocksunder.abstention.util.RenderNodeUtil;

/**
 * Main Node class. This class handles the generic operations of nodes.
 * 
 * @author Joshua Edwards
 * 
 */
public abstract class Node extends GraphComponent {

	/**
	 * HashMap<Rectangle,Node> used for detecting if the user tries to establish
	 * a connection between a pair of nodes.
	 */
	public static HashMap<Rectangle, Node> mapNodeTargets = new HashMap<>();

	private static Random random = new Random();
	
	/**
	 * Our render cache for static. This is initialized once.
	 */
	private static volatile Cache<BufferedImage>[] renderCacheStatic;

	private NodeData nodeData;
	/**
	 * The GraphPanel instance carrying this node.
	 */
	private MainPanel mainPanel;

	/**
	 * The name of the node.
	 */
	private String name = "";

	/**
	 * The width of the node.
	 */
	private int width = 128;

	/**
	 * The height of the node.
	 */
	private int height = 64;

	/**
	 * The location of the node's input area for connections to be drawn. [0] =
	 * x [1] = y
	 */
	private int[] locationInput;

	/**
	 * The location of the node's output area for connections to be drawn. [0] =
	 * x [1] = y
	 */
	private int[] locationOutput;

	private float nodeOpacity = 0.0F;

	private boolean drawnOutputCircle = false;

	private boolean drawnInputCircle = false;

	/**
	 * list of Node objects connected to this node.
	 */
	private ArrayList<NodeConnection> listNodeConnections;

	/**
	 * The default color of the node when drawn.
	 */
	private Color colorNode = new Color(170, 170, 170);

	/**
	 * The color of the node when selected.
	 */
	private Color colorSelected = new Color(230, 230, 230);

	/**
	 * The color of the node's shadow. (use alpha)
	 */
	private Color colorShadow = new Color(10, 10, 10, 100);

	/**
	 * The interior color of the node when drawn.
	 */
	private Color colorInterior = Color.black;

	/**
	 * Whether or not the scene is selected in the editor.
	 */
	private boolean selected = false;

	private boolean movable = false;

	/**
	 * Initializes the node.
	 */
	public abstract void initialize();

	/**
	 * Updates the node.
	 * @param pass 
	 */
	public abstract void updateNode(GraphCamera camera, int pass);

	/**
	 * Draws the node.
	 * 
	 * @param g2d
	 * @param nodeData
	 * @param camera
	 * @param pass
	 */
	public abstract void drawNode(Graphics2D g2d, GraphCamera camera, int pass);

	/**
	 * Handles mouse pressed events.
	 * 
	 * @param mousePressed
	 * @param e
	 * @param camera
	 */
	public abstract void handleMousePressed(int[] mousePressed, MouseEvent e,
			GraphCamera camera, GraphKeys keys);

	/**
	 * Handles mouse dragged events.
	 * 
	 * @param mousePressed
	 * @param mouseDelta
	 * @param e
	 * @param camera
	 */
	public abstract void handleMouseDragged(int[] mousePressed,
			int[] mouseDelta, MouseEvent e, GraphCamera camera, GraphKeys keys,
			CursorController cursorController);

	public abstract void handleMouseReleased(int[] mousePressed,
			int[] mouseDelta, int[] mouseReleased, MouseEvent e,
			GraphCamera camera, GraphKeys keys,
			CursorController cursorController);

	public abstract boolean handleMouseMoved(int[] mouseMoved, MouseEvent e,
			GraphCamera camera, GraphKeys keys,
			CursorController cursorController);

	public abstract void handleKeyPressed(KeyEvent e, GraphCamera camera);

	public abstract void handleKeyReleased(KeyEvent e, GraphCamera camera);

	public abstract void handleKeyTyped(KeyEvent e, GraphCamera camera);
	
	public abstract void handleDoubleClicked(int[] mousePressed, GraphCamera camera);

	/**
	 * Main constructor.
	 * 
	 * @param mainPanel
	 * @param name
	 * @param width
	 * @param height
	 */
	public Node(MainPanel mainPanel, String name, int width, int height,
			NodeData nodeData) {
		setMainPanel(mainPanel);
		setName(name);
		setWidth(width);
		setHeight(height);
		listNodeConnections = new ArrayList<>();
		if (renderCacheStatic == null) {
			renderCachedStatic();
		}
		setData(nodeData);

	}

	public void update(GraphCamera camera, int pass) {
		if (nodeOpacity < 1F) {
			updateOpacity();
		}
		updateNode(camera, pass);
	}

	private void updateOpacity() {
		if (nodeOpacity < 1F) {
			if (nodeOpacity < 1F) {
				nodeOpacity += 0.01F;
			}
		} else if (nodeOpacity >= 1) {
			nodeOpacity = 1;
		}

	}

	/**
	 * Renders the node.
	 * 
	 * @param g2
	 * @param cameraLocation
	 * @param cameraScale
	 */
	public void draw(Graphics2D g2d, GraphCamera camera, int pass) {
		//System.out.println("ee");
		/*
		 * Reference the last Composite the graphics instance is using to
		 * restore when done.
		 */
		Composite lastComposite = g2d.getComposite();

		Composite composite = null;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				Math.min(nodeOpacity, 1));
		g2d.setComposite(composite);

		// Draws the node sending the camera-space.
		drawNode(g2d, camera, pass);

		// Resets our original Composite object.
		g2d.setComposite(lastComposite);
	}

	public BufferedImage getNextRandomStaticFrame() {
		return renderCacheStatic[random.nextInt(renderCacheStatic.length)]
				.getCache();
	}

	public void drawStatic(Graphics2D g2d, int[] nodeData) {
		BufferedImage image = getNextRandomStaticFrame();

		g2d.drawImage(image, nodeData[0] + 5, nodeData[1] + 5, nodeData[0]
				+ nodeData[2] - 5, nodeData[1] + nodeData[3] - 5, 0, 0,
				image.getWidth() - 1, image.getHeight() - 1, null);
		// BufferedImage imageStatic = RenderNodeUtil.renderStatic(
		// nodeData[2] - 10, nodeData[3] - 10);
		// g2d.drawImage(imageStatic, nodeData[0] + 5, nodeData[1] + 5, null);
	}

	@SuppressWarnings("unchecked")
	private void renderCachedStatic() {
		int staticFrameCount = 32;
		int resolutionMultiplier = 1;
		renderCacheStatic = new Cache[staticFrameCount];
		for (int x = 0; x < staticFrameCount; x++) {
			renderCacheStatic[x] = new Cache<BufferedImage>();
			renderCacheStatic[x].setCache(
					RenderNodeUtil.renderStatic(getWidth()
							* resolutionMultiplier, getHeight()
							* resolutionMultiplier), "");
		}
	}

	/**
	 * If the node contains the mouse's location on the GraphPanel instance.
	 * 
	 * @param mouse
	 * @param cameraLocation
	 * @param cameraScale
	 * @return
	 */
	public boolean containsMouse(int[] mouseLocation, GraphCamera camera) {

		// Our nodeData in cameraSpace.
		int[] nodeData = getNodeSpace(this, camera);

		/*
		 * if(x is less than or equal to the maximum x value of the node's space
		 * (x + width) AND x is greater than or equal to the mimimum x value of
		 * the node's space (x).
		 */
		if (mouseLocation[0] <= nodeData[0] + nodeData[2]
				&& mouseLocation[0] >= nodeData[0]) {
			/*
			 * if(y is less than or equal to the maximum y value of the node's
			 * space (y + height) AND y is greater than or equal to the mimimum
			 * y value of the node's space (y).
			 */
			if (mouseLocation[1] <= nodeData[1] + nodeData[3]
					&& mouseLocation[1] >= nodeData[1]) {
				// Return true to say it is within our node's position on camera
				// space.
				return true;
			}
		}
		// Return false to say it is not within the node's position on camera
		// space.
		return false;
	}

	/**
	 * Updates the Input/Output coordinates used to draw and interpret Node
	 * connectivity.
	 * 
	 * @param drawData
	 */
	public void updateIOPositions(int[] drawData) {

		// Setup our intput as int[] { x, y + (height / 2) } .
		int[] inputLocation = new int[] { drawData[0],
				drawData[1] + (drawData[3] / 2) };
		// Set our input location.
		setLocationInput(inputLocation);

		// Setup our output as int[] { x + width, y + (height / 2) } .
		int[] outputLocation = new int[] { drawData[0] + drawData[2],
				drawData[1] + (drawData[3] / 2) };
		// Set our output location.
		setLocationOutput(outputLocation);
	}

	/**
	 * Transforms our localized node data to data used for rendering and
	 * locating. [0] = x [1] = y [2] = width [3] = scale
	 * 
	 * @param node
	 * @param camera
	 * @return
	 */
	public static int[] getNodeSpace(Node node, GraphCamera camera) {
		// Our node's location in local-space.
		int[] location = node.getLocation();

		// Our node's width, and height.
		int width = node.getWidth();
		int height = node.getHeight();

		// return getNodeSpace(new int[] { location[0], location[1], width,
		// height }, camera);
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

	public static int[] getNodeSpace(int[] localSpace, GraphCamera camera) {
		// Our camera data.
		int[] cameraLocation = camera.getLocation();
		float cameraScale = camera.getScale();

		// x = ((locationInLocalSpaceX + cameraLocationInSpaceX) * cameraScale);
		int x = (int) ((localSpace[0] + cameraLocation[0]) * cameraScale);

		// y = ((locationInLocalSpaceY + cameraLocationInSpaceY) * cameraScale);
		int y = (int) ((localSpace[1] + cameraLocation[1]) * cameraScale);

		// Multiply width * height by the scale.
		int w = (int) (localSpace[2] * cameraScale);
		int h = (int) (localSpace[3] * cameraScale);

		// Return our data as a int[4].
		return new int[] { x, y, w, h };
	}

	/**
	 * Sets the node's input location.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocationInput(int x, int y) {
		// Sets the location as a new integer array to prevent data poisoning.
		this.locationInput = new int[] { x, y };
	}

	/**
	 * Sets the node's output location.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocationOutput(int x, int y) {
		// Sets the location as a new integer array to prevent data poisoning.
		this.locationOutput = new int[] { x, y };
	}

	/**
	 * Sets the node's input location.
	 * 
	 * @param location
	 */
	public void setLocationInput(int[] location) {
		this.locationInput = location;
	}

	/**
	 * Sets the node's output location.
	 * 
	 * @param location
	 */
	public void setLocationOutput(int[] location) {
		this.locationOutput = location;
	}

	/**
	 * Gets the node's input location.
	 * 
	 * @return
	 */
	public int[] getLocationInput() {
		return this.locationInput;
	}

	/**
	 * Gets the node's output location.
	 * 
	 * @return
	 */
	public int[] getLocationOutput() {
		return this.locationOutput;
	}

	/**
	 * Sets the name of the node.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the node.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the interior color for drawing this node.
	 * 
	 * @return
	 */
	public Color getColorInterior() {
		return this.colorInterior;
	}

	/**
	 * Sets the interior color for drawing this node.
	 * 
	 * @param colorInterior
	 */
	public void setColorInterior(Color colorInterior) {
		this.colorInterior = colorInterior;
	}

	/**
	 * Gets the color of the node when selected.
	 * 
	 * @return
	 */
	public Color getColorSelected() {
		return this.colorSelected;
	}

	/**
	 * Sets the color of the node when selected.
	 * 
	 * @param color
	 */
	public void setColorSelected(Color color) {
		this.colorSelected = color;
	}

	/**
	 * Gets the default color of the node.
	 * 
	 * @return
	 */
	public Color getColor() {
		return this.colorNode;
	}

	/**
	 * Sets the default color of the node.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.colorNode = color;
	}

	/**
	 * Gets the color of the node's shadow.
	 * 
	 * @return
	 */
	public Color getColorShadow() {
		return this.colorShadow;
	}

	/**
	 * Sets the color of the node's shadow.
	 * 
	 * @param color
	 */
	public void setColorShadow(Color color) {
		this.colorShadow = color;
	}

	/**
	 * Adds a connected node to the list for draw reference.
	 * 
	 * @param node
	 */
	public NodeConnection addConnectedNode(Node node) {
		NodeConnection nodeConnection = new NodeConnection(this, node);
		listNodeConnections.add(nodeConnection);
		return nodeConnection;
	}

	/**
	 * Removes a connected node from the list.
	 * 
	 * @param node
	 */
	public void removeConnectedNode(NodeConnection nodeConnection) {
		listNodeConnections.remove(nodeConnection);
	}

	/**
	 * Returns an ArrayList<NodeConnection> of all nodes connected to this node
	 * FROM this node only.
	 * 
	 * @return
	 */
	public ArrayList<NodeConnection> getConnectedNodes() {
		return listNodeConnections;
	}

	/**
	 * Sets the location of the node in local space.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {

		getData().setLocation(x, y);
	}

	/**
	 * Sets the location of the node in local space.
	 * 
	 * @param newLocation
	 */
	public void setLocation(int[] newLocation) {
		getData().setLocation(newLocation);
	}

	/**
	 * Gets the location of the node in local space.
	 * 
	 * @return
	 */
	public int[] getLocation() {
		return getData().getLocation();
	}

	/**
	 * Returns the JSON data of a node.
	 * 
	 * @return
	 */
	public NodeData getData() {
		return this.nodeData;
	}

	/**
	 * Sets the GraphPanel instance using this node.
	 * 
	 * @param mainPanel
	 */
	public void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * Gets the GraphPanel instance using this node.
	 * 
	 * @return
	 */
	public MainPanel getMainPanel() {
		return this.mainPanel;
	}

	/**
	 * Sets the node to be selected.
	 * 
	 * @param b
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Returns if the node s selected or not.
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Gets the height of the node.
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the width of the node.
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the height of the node.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Sets the width of the node.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Sets whether or not this node can be moved.
	 * 
	 * @param movable
	 */
	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean hasDrawnOutputCircle() {
		return this.drawnOutputCircle;
	}

	public boolean hasDrawnInputCircle() {
		return this.drawnInputCircle;
	}

	public void setDrawnOutputCircle(boolean b) {
		this.drawnOutputCircle = b;
	}

	public void setDrawnInputCircle(boolean b) {
		this.drawnInputCircle = b;
	}

	/**
	 * Returns whether or not this node can be moved.
	 * 
	 * @return
	 */
	public boolean isMovable() {
		return this.movable;
	}

	/**
	 * Sets the JSson Data object appropriate.
	 * 
	 * @param nodeData
	 */
	private void setData(NodeData nodeData) {
		this.nodeData = nodeData;
	}

}
