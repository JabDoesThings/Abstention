package com.fourtyblocksunder.abstention.editor.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Timer;
import java.util.TimerTask;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;
import com.fourtyblocksunder.abstention.util.GraphicsUtil;
import com.fourtyblocksunder.abstention.util.CursorController;

/**
 * Class designed to execute render methods and perform various tasks to keep
 * the render efficient.
 * 
 * @author JabJabJab
 * 
 */
public class GraphCamera extends GraphComponent {

	private Font fontFPSnew = new Font(null, Font.LAYOUT_NO_START_CONTEXT, 16);

	private CursorController cursorController;

	/**
	 * The gradient Color object for the vignette of the camera.
	 */
	private Color backgroundGradientBlack;

	/**
	 * The Color object for the background for this camera.
	 */
	private Color colorBackground;

	/**
	 * Location of the camera in graph-space.
	 */
	private int[] location = new int[] { 0, 0 };

	/**
	 * Integer array responsible for storing the position of the mouse local to
	 * the panel's position on the screen when calculating mouse position.
	 */
	private int[] cameraPressed = new int[] { 0, 0 };

	/**
	 * Integer Array[xMin, xMax, yMin, yMax] that stores the limits of the
	 * camera when setting the position relative to the Graph's space.
	 */
	private int[] cameraLimits;

	/**
	 * The size of the gradient that extends as a vignette for the window.
	 */
	private int gradientSize = 64;

	/**
	 * The "Frames-Per-Second" counter represented as an integer.
	 */
	private int fps = 0;

	/**
	 * The counter for the amount of frames, reset every second by our TimerTask
	 * instance.
	 */
	private int totalFrameCount = 0;

	/**
	 * Number of passes required to properly draw our graph.
	 */
	private int renderPasses = 1;

	/**
	 * Float for storing the last increment value for graph line draws.
	 */
	private int lastIncrementSize = 0;

	/**
	 * Float for the last x offset for graph line draws.
	 */
	private int lastXOffset = 0;

	/**
	 * Float for the last y offset for graph line draws.
	 */
	private int lastYOffset = 0;

	/**
	 * Float to record the last scale.
	 */
	private float lastScale = 0F;

	/**
	 * Scale of the camera.
	 */
	private float scale = 1.0F;

	/**
	 * Float storing the acceleration value on the x location of the camera.
	 */
	private float accelerationX;

	/**
	 * Float storing the acceleration value on the y location of the camera.
	 */
	private float accelerationY;

	/**
	 * Float storing the acceleration value on the scale of the camera.
	 */
	private float accelerationZoom;

	/**
	 * Float storing the limit of the speed of the location acceleration
	 * coordinates.
	 */
	private float accelerationLimit = 0.2F;

	/**
	 * The acceleration to add when presented with acceleration.
	 */
	private float acceleration = 0.005F;

	/**
	 * Boolean to store whether or not the camera should calculate the delta of
	 * the mouse on the screen for camera movement purposes.
	 */
	private boolean isMovable;

	private NodeGraph graph;

	/**
	 * Main constructor.
	 * 
	 * @param graphPanel
	 */
	public GraphCamera(NodeGraph nodeGraph) {
		// Sets the panel of the camera.
		setNodeGraph(nodeGraph);
		// Initializes the camera.
		initialize();
	}

	private void setNodeGraph(NodeGraph nodeGraph) {
		this.graph = nodeGraph;
	}

	private NodeGraph getNodeGraph() {
		return this.graph;
	}

	/**
	 * Initializes our camera.
	 */
	private void initialize() {
		// Create our Timer object to process FPS.
		Timer t = new Timer();

		/*
		 * Set our private TimerTask instance as our task for the Timer
		 * instance.
		 */
		t.scheduleAtFixedRate(updateFPS, 1000, 1000);

		// Declare our Color objects.
		colorBackground = new Color(20, 20, 25);
		backgroundGradientBlack = new Color(0, 0, 0, 50);
		// minorLine = new Color(35 * 2, 35 * 2, 35 * 2);
		cameraLimits = new int[] { -8192, -8192, 8192, 8192 };
		cursorController = new CursorController(getNodeGraph().getMainPanel());
	}

	/**
	 * Main Constructor.
	 * 
	 * @param location
	 * @param scale
	 */
	public GraphCamera(NodeGraph graph, int[] location, float scale,
			int renderPasses) {
		// Set our variables.
		setNodeGraph(graph);
		setLocation(location[0], location[1]);
		setScale(scale);
		setRenderPasses(renderPasses);
		// Initialize our variables.
		initialize();
	}

	/**
	 * Renders a graph panel.
	 * 
	 * @param panel
	 * @param fps
	 */
	public void renderPanel(Graphics2D g2d) {

		NodeGraph graph = getNodeGraph();
		GraphMenu menu = graph.getGraphMenu();
		MainPanel panel = graph.getMainPanel();
		int[] mouseLocation = panel.getMouseLocation();

		drawGraph(g2d, this);
		graph.renderGraph(g2d);

		// Draws our frames-Per-Second.
		drawFPS(g2d);

		// Draws our menu UI.
		menu.renderMenu(g2d, this);

		if (mouseLocation != null) {
			cursorController.setPanelCursorNull(true);
			if (mouseLocation[0] >= 0 && mouseLocation[1] >= 0) {
				cursorController.draw(g2d, mouseLocation[0], mouseLocation[1]);
			}
		} else {
			cursorController.setPanelCursorNull(false);
		}

		// Turn off anti-aliasing.
		GraphicsUtil.setAntiAlias(g2d, false);

		// Increments frame-count;
		totalFrameCount++;
	}

	/**
	 * Updates the values for zooming deltas and panning deltas and then applies
	 * them to the position and scale of the camera.
	 */
	public void updateAccelerations() {

		// Updates the acceleration of location.
		if (accelerationX != 0) {
			// Slows it down slowly.
			accelerationX /= 1.05F;
			// Slows down the acceleration value.
			if (accelerationX < 0.05F && accelerationX > -0.05F) {
				accelerationX = 0;
			}
		}
		if (accelerationY != 0) {
			// Slows down the acceleration value.
			accelerationY /= 1.05F;
			// Zeros out if the number becomes smaller than 0.05F.
			if (accelerationY < 0.05F && accelerationY > -0.05F) {
				accelerationY = 0;
			}
		}
		if (accelerationZoom != 0) {
			// Slows down the acceleration value.
			accelerationZoom /= 1.1F;
			// Zeros out if the number becomes smaller than 0.00005F.
			if (accelerationZoom < 0.00005F && accelerationZoom > -0.00005F) {
				accelerationZoom = 0;
			}
		}

		// Grab our location of the camera.
		int[] location = getLocation();

		// Add the accelerations to the position.
		location[0] += accelerationX;
		location[1] += accelerationY;

		// Grab and add our scale with acceleration.
		float newScale = getScale() + accelerationZoom;
		// Keeps the scale in-bounds.
		if (newScale > 2.5F) {
			newScale = 2.5F;
			accelerationZoom = 0;
		} else if (newScale < 0.25F) {
			newScale = 0.25F;
			accelerationZoom = 0;
		}

		// Sets our new location and zoom.
		setScale(newScale);
		setLocation(location);
	}

	/**
	 * Displays our Frames Per Second.
	 * 
	 * @param g2d
	 */
	private void drawFPS(Graphics2D g2d) {
		// Store the last Font & last Color in the graphics2D instance.
		Font lastFont = g2d.getFont();
		Color lastColor = g2d.getColor();

		// Sets our color to red.
		g2d.setColor(Color.RED);
		// Sets our new used font.
		g2d.setFont(fontFPSnew);
		g2d.drawString("FPS: " + fps, 48, 16);

		// Reset Font & color.
		g2d.setFont(lastFont);
		g2d.setColor(lastColor);
	}

	/**
	 * 
	 * @param g2d
	 * @param graphCamera
	 */
	public void drawGraph(Graphics2D g2d, GraphCamera graphCamera) {
		// Draws our graph components.
		paintGraphLines(g2d, graphCamera);
		drawVignette(g2d);

	}

	/**
	 * Draws the graph lines on the background.
	 * 
	 * @param g2d
	 * @param camera
	 */
	private void paintGraphLines(Graphics2D g2d, GraphCamera camera) {

		// Store our original paint to reset later on.
		Paint lastPaint = g2d.getPaint();

		NodeGraph graph = getNodeGraph();

		// Grab the width and height of the GraphPanel.
		int width = graph.getWidth();
		int height = graph.getHeight();

		// Set the paint to the background color.
		g2d.setPaint(new Color(Math.min(150, colorBackground.getRed()
				+ (int) (Math.abs(accelerationZoom * 256))), Math.min(
				150,
				colorBackground.getGreen()
						+ (int) (Math.abs(accelerationZoom * 256))), Math.min(
				150,
				colorBackground.getBlue()
						+ (int) (Math.abs(accelerationZoom * 256)))));
		// Draw the background color.
		g2d.fillRect(0, 0, width, height);

		// Grab our camera's location.
		int[] cameraLocation = getLocation();

		g2d.setPaint(new Color(Math.min(200, colorBackground.getRed() + 12
				+ (int) (Math.abs(accelerationZoom * 256))), Math.min(
				200,
				colorBackground.getGreen() + 12
						+ (int) (Math.abs(accelerationZoom * 256))), Math.min(
				200,
				colorBackground.getBlue() + 12
						+ (int) (Math.abs(accelerationZoom * 256)))));

		// Grab our camera's scale.
		float cameraScale = getScale();

		if (getScale() > 0.5) {
			int graphData[] = getGraphData(18, width, height, cameraLocation,
					cameraScale);
			for (int i = graphData[1]; i < width; i += graphData[0]) {
				g2d.drawLine(i, 0, i, height);
			}
			for (int i = graphData[2]; i < height; i += graphData[0]) {
				g2d.drawLine(0, i, width, i);
			}
		}

		// Reset the paint to the original paint.
		g2d.setPaint(lastPaint);

	}

	/**
	 * Returns the graphLine Draw data in the form of { incrementSize, xOffset,
	 * and yOffset.
	 * 
	 * @param length
	 * 
	 * @param width
	 * 
	 * @param height
	 * 
	 * @param cameraLocation
	 * 
	 * @param cameraScale
	 * 
	 * @return
	 */

	private int[] getGraphData(int length, int width, int height,
			int[] cameraLocation, float cameraScale) {
		if (!(cameraScale == lastScale)) {
			lastIncrementSize = (int) (length * cameraScale);
			lastXOffset = (cameraLocation[0] % lastIncrementSize) % width;
			lastYOffset = (cameraLocation[1] % lastIncrementSize) % height;
			lastScale = cameraScale;
		}
		return new int[] { lastIncrementSize, lastXOffset, lastYOffset };
	}

	/**
	 * Paints the vignette of the screen.
	 * 
	 * @param g2d
	 */
	private void drawVignette(Graphics2D g2d) {

		// Store our last paint object in g2d to reset later on.
		Paint lastPaint = g2d.getPaint();

		NodeGraph graph = getNodeGraph();

		// Get our panel width and height.
		int width = graph.getWidth();
		int height = graph.getHeight();

		// Define all four side gradients of the screen.
		Paint paintTopGradient = new GradientPaint(0, 0,
				backgroundGradientBlack, 0, gradientSize, GraphicsUtil.alpha);
		Paint paintBottomGradient = new GradientPaint(0, height
				- (gradientSize), GraphicsUtil.alpha, 0, height,
				backgroundGradientBlack);
		Paint paintLeftGradient = new GradientPaint(0, 0,
				backgroundGradientBlack, (gradientSize), 0, GraphicsUtil.alpha);
		Paint paintRightGradient = new GradientPaint(width - (gradientSize), 0,
				GraphicsUtil.alpha, width, 0, backgroundGradientBlack);

		// Set our paint for each side and paint them.
		// Top
		g2d.setPaint(paintTopGradient);
		g2d.fillRect(0, 0, width, gradientSize);
		// Bottom
		g2d.setPaint(paintBottomGradient);
		g2d.fillRect(0, height - (gradientSize), width, gradientSize);
		// Left
		g2d.setPaint(paintLeftGradient);
		g2d.fillRect(0, 0, (gradientSize), height);
		// Right
		g2d.setPaint(paintRightGradient);
		g2d.fillRect(width - (gradientSize), 0, (gradientSize), height);

		// Reset our paint.
		g2d.setPaint(lastPaint);

	}

	/**
	 * Sets the render pass amount needed. If the required passes needed is
	 * below the current pass amount, then the request is ignored.
	 * 
	 * @param renderPasses
	 */
	public void setRenderPasses(int renderPasses) {
		if (renderPasses > this.renderPasses) {
			this.renderPasses = renderPasses;
		}
	}

	/**
	 * Sets the scale of zoom for the Camera.
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Returns how many passes the camera does per render.
	 * 
	 * @return
	 */
	public int getRenderPasses() {
		return this.renderPasses;
	}

	/**
	 * Returns the current scale of the camera.
	 * 
	 * @return
	 */
	public float getScale() {
		return this.scale;
	}

	/**
	 * Returns the location of the camera in the form of { x, y }.
	 * 
	 * @return
	 */
	public int[] getLocation() {
		return this.location;
	}

	/**
	 * Sets the location of the camera using a int[x,y].
	 * 
	 * @param location
	 */
	public void setLocation(int[] location) {
		this.setLocation(location[0], location[1]);
	}

	/**
	 * Sets the location of the Camera.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		// The scale of the camera.
		float scale = getScale();

		NodeGraph graph = getNodeGraph();

		// Our panel Width & Height.
		int panelWidth = graph.getWidth();
		int panelHeight = graph.getHeight();

		// Our limits in array form.
		int[] cameraLimits = this.getLimits();

		// Our limits in variable form.
		int minX = cameraLimits[0];
		int minY = cameraLimits[1];
		int maxX = cameraLimits[2];
		int maxY = cameraLimits[3];

		// Check if coordinates are out-of-bounds. If so, correct them.
		// X
		if (x < (minX + panelWidth) * scale) {
			x = (int) ((minX + panelWidth) * scale);
		} else if (x > (maxX - panelWidth) * scale) {
			x = (int) ((maxX - panelWidth) * scale);
		}
		// Y
		if (y < (minY + panelHeight) * scale) {
			y = (int) ((minY + panelHeight) * scale);
		} else if (y > (maxY - panelHeight) * scale) {
			y = (int) ((maxY - panelHeight) * scale);
		}
		// Set the corrected location.
		this.location = new int[] { x, y };
	}

	/**
	 * Returns the limit bounds for the camera's position in the form of
	 * [xMin,xMax,yMin,yMax]
	 * 
	 * @return
	 */
	private int[] getLimits() {
		return this.cameraLimits;
	}

	/**
	 * Sets the limits of the camera's position in the form of
	 * [xMin,xMax,yMin,yMax]
	 * 
	 * @param limits
	 */
	public void setLimits(int[] limits) {
		if (limits.length == 4) {
			this.cameraLimits = limits;
		}
	}

	/**
	 * Checks if an object is within or intersecting camera-space.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean isWithinScreen(int x, int y, int width, int height) {
		// Create a rectangle for the object.
		Rectangle rect = new Rectangle(x, y, width, height);
		return isWithinScreen(rect);
	}

	/**
	 * Checks if an object is within or intersecting camera-space.
	 * 
	 * @param rect
	 * @return
	 */
	public boolean isWithinScreen(Rectangle rect) {

		NodeGraph graph = getNodeGraph();
		int width = graph.getWidth();
		int height = graph.getHeight();
		// Create a rectangle for the screen.
		Rectangle screen = new Rectangle(0, 0, width, height);
		// Returns true if the scren intersects OR contains the object.
		return screen.intersects(rect) || screen.contains(rect);
	}

	/**
	 * Handles the event of a mouse button being pressed.
	 * 
	 * @param e
	 */
	public void handleMousePressed(int[] mousePressed, MouseEvent e) {
		// Middle-Mouse button
		if (e.getButton() == 2) {
			// Sets new original position of pressed.
			cameraPressed = getLocation();

			// Sets the camera to movable until release.
			isMovable = true;
		}
	}

	/**
	 * Handles the event of a mouse button being dragged.
	 * 
	 * @param e
	 */
	public void handleMouseDragged(int[] mousePressed, int[] mouseDelta,
			MouseEvent e) {
		if (isMovable) {
			// Get our delta for the camera.
			int[] cameraDeltaLocation = new int[] {
					cameraPressed[0] + mouseDelta[0],
					cameraPressed[1] + mouseDelta[1] };

			// Apply the delta.
			setLocation(cameraDeltaLocation);
		}
	}

	public boolean isMovable() {
		return this.isMovable;
	}

	/**
	 * Handles the event of a mouse button being released.
	 * 
	 * @param e
	 */
	public void handleMouseReleased(MouseEvent e) {
		// Sets movability to false to stop the camera from offseting.
		isMovable = false;
	}

	/**
	 * Handles the event of the mouse wheel moved.
	 * 
	 * @param e
	 */
	public boolean handleMouseWheelMoved(MouseWheelEvent e) {
		boolean zoomIn = false;
		// Movement of the wheel.

		/*
		 * Get the number of units the mouse wheel has moved since the last
		 * event trigger.
		 */
		int notches = e.getWheelRotation();

		// If no notches, return.
		if (notches == 0) {
			return false;
		}
		// If you have a negative number, zoom in. else, zoom out.
		if (notches < 0) {
			zoomIn = true;
		}

		// Iterate over how many notches have moved for accuracy.
		for (int notch = 0; notch < Math.abs(notches); notch++) {
			if (zoomIn) {
				accelerationZoom += acceleration;
				// Limit acceleration.
				if (accelerationZoom > accelerationLimit) {
					accelerationZoom = accelerationLimit;
				}

			} else {
				accelerationZoom -= acceleration;
				// Limit acceleration.
				if (accelerationZoom < -accelerationLimit) {
					accelerationZoom = -accelerationLimit;
				}
			}
		}
		return true;
	}

	/**
	 * Handles the event of as key being pressed down.
	 * 
	 * @param e
	 */
	public void handleKeyPressed(KeyEvent e) {
		// Zoom in.
		if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
			accelerationZoom += acceleration;
			// Limit zoom acceleration.
			if (accelerationZoom > accelerationLimit) {
				accelerationZoom = accelerationLimit;
			}
		}
		// Zoom out.
		else if (e.getKeyChar() == '-' || e.getKeyChar() == '_') {
			accelerationZoom -= acceleration;
			// Limit zoom acceleration.
			if (accelerationZoom < -accelerationLimit) {
				accelerationZoom = -accelerationLimit;
			}
		}

	}

	/**
	 * Handles a key being released.
	 * 
	 * @param e
	 */
	public void handleKeyReleased(KeyEvent e) {

	}

	/**
	 * Handles a key being typed.
	 * 
	 * @param e
	 */
	public void handleKeyTyped(KeyEvent e) {
		float acceleration = 15F / getScale();
		char key = e.getKeyChar();
		// Move up.
		if (key == 's') {
			accelerationY -= acceleration;
			// Limit zoom acceleration.
			if (accelerationY < -20F) {
				accelerationY = -20F;
			}
		}
		// Move down.
		if (key == 'w') {
			accelerationY += acceleration;
			// Limit zoom acceleration.
			if (accelerationY > 20F) {
				accelerationY = 20F;
			}
		}
		// Move left.
		if (key == 'd') {
			// Limit zoom acceleration.
			accelerationX -= acceleration;
			if (accelerationX < -20F) {
				accelerationX = -20F;
			}
		}
		// Move right.
		if (key == 'a') {
			// Limit zoom acceleration.
			accelerationX += acceleration;
			if (accelerationX > 20F) {
				accelerationX = 20F;
			}
		}
	}

	/**
	 * TimerTask designed to keep track of frames drawn per second.
	 */
	TimerTask updateFPS = new TimerTask() {
		public void run() {

			// Set the fps to the amount of frames counted.
			fps = totalFrameCount;

			// Reset our counter.
			totalFrameCount = 0;
		}
	};

	public void update() {
		cursorController.update();
		updateAccelerations();
	}

	public CursorController getCursorController() {
		return cursorController;
	}

	@Override
	public boolean changed() {
		NodeGraph graph = getNodeGraph();
		boolean nodeChanged = false;

		boolean locationChanged = locationChanged(getLocation());
		boolean scaleChanged = scaleChanged(getScale());

		for (Node node : graph.getProject().getNodes()) {
			if (node.changed()) {
				nodeChanged = true;
			}
		}

		return scaleChanged || locationChanged || nodeChanged;
	}

	@Override
	public void setChanged() {
		setLastScale(getScale());
		setLastLocation(getLocation());
	}

}