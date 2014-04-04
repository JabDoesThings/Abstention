package com.fourtyblocksunder.abstention.editor.graph.node;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.data.NodeData;
import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.GraphKeys;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraph;
import com.fourtyblocksunder.abstention.editor.graph.button.NodeButton;
import com.fourtyblocksunder.abstention.util.GraphicsUtil;
import com.fourtyblocksunder.abstention.util.Cache;
import com.fourtyblocksunder.abstention.util.CursorController;
import com.fourtyblocksunder.abstention.util.RenderNodeUtil;

/**
 * Class designed to handle basic functions for nodes.
 * 
 * @author Main
 * 
 */
public abstract class BasicNode extends Node {

	private int phase = 101;

	@SuppressWarnings("unused")
	private long lastClicked = 0L;

	/**
	 * Our render cache for the 2nd pass drawing. Stores our NodeBoxFrame.
	 */
	private volatile Cache<BufferedImage> renderCache;

	/**
	 * Button used for minimizing / maximizing the node window.
	 */
	private NodeButton buttonMaximize;

	/**
	 * int[x,y] that is used for movement when dragged.
	 */
	private int[] originalPosition = new int[] { 0, 0 };

	/**
	 * Color
	 */
	//private Color curve = new Color(220, 220, 220, 100);

	private int[] cameraSpace = new int[] { 0, 0, 0, 0 };

	private int[] phaseSpace = new int[] { 0, 0, 0, 0 };

	private Rectangle rectangle;

	public abstract void drawActiveNode(Graphics2D g2d, GraphCamera camera, int pass);
	
	public abstract void updateActiveNode(NodeGraph graph, GraphCamera camera);
	
	/**
	 * Main constructor.
	 * 
	 * @param mainPanel
	 * @param name
	 * @param width
	 * @param height
	 */
	public BasicNode(MainPanel mainPanel, String name, int width, int height,
			NodeData nodeData) {
		super(mainPanel, name, width, height, validateData(nodeData));
		initialize();
	}

	private static NodeData validateData(NodeData nodeData) {
		return nodeData == null ? new NodeData() : nodeData;
	}

	/**
	 * Initializes the BasicNode.
	 */
	@Override
	public void initialize() {
		initializeButtons();
		setButtonDimensions();
		updateIOPositions(new int[] { 0, 0, 0, 0 });
		setColorInterior(new Color(20, 20, 20, 127));
		setColorSelected(new Color(240, 200, 140));
		renderCache = new Cache<>();
		rectangle = new Rectangle();

	}

	/**
	 * Initializes button objects.
	 */
	private void initializeButtons() {
		// Create our button object.
		buttonMaximize = new NodeButton(this);
	}

	/**
	 * Setup our button's position / dimensions.
	 */
	public void setButtonDimensions() {
		// Set our button's width and height.
		int buttonMaximizeWidth = 15;
		int buttonMaximizeHeight = 15;
		// Set our button's location.
		buttonMaximize.setLocation(getWidth() - 20, -16);
		// Set our defined dimensions.
		buttonMaximize.setWidth(buttonMaximizeWidth);
		buttonMaximize.setHeight(buttonMaximizeHeight);
	}

	@Override
	public void updateNode(GraphCamera camera, int pass) {
		if (pass == 0) {
			setCameraSpace(getNodeSpace(this, camera));
			updateIOPositions(cameraSpace);
			
			rectangle.setBounds(cameraSpace[0], cameraSpace[1], cameraSpace[2],
					cameraSpace[3]);
		} else if (pass == 1) {
			ArrayList<NodeConnection> listNodeConnections = getConnectedNodes();
			for(int index = 0; index < listNodeConnections.size(); index++)
			{				
				listNodeConnections.get(index).update(camera);
			}
		}
	}
	
	public void updateTransition(GraphCamera camera)
	{
		if (phase <= 100) {
			updateScaled(camera);
			phase += 1;
		} else {
			acceleration = 1.0F;
		}
	}

	public void setCameraSpace(int[] cameraSpace) {
		this.cameraSpace = cameraSpace;
	}

	public int[] getCameraSpace() {
		return this.cameraSpace;
	}

	/**
	 * Draws our node.
	 */
	@Override
	public void drawNode(Graphics2D g2d, GraphCamera camera, int pass) {
		int[] nodeData = getCameraSpace();
		camera.setRenderPasses(3);
		Color colorNode = isSelected() ? getColorSelected() : getColor();
		// Pass 1
		if (pass == 0) {
			for (NodeConnection nodeConnection : getConnectedNodes()) {
				nodeConnection.drawLine(g2d, camera);
			}
		} else if (pass == 1) {
			// Pass 2
			if (!camera.isWithinScreen(rectangle)) {
				return;
			}
			int[] localLocation = getLocation();
			String newKey = localLocation[0] + " " + localLocation[1] + " "
					+ camera.getScale() + " " + buttonMaximize.isOn()
					+ isMovable() + " " + isSelected();
			if (!renderCache.keysMatch(newKey)) {
				BufferedImage image = new BufferedImage(nodeData[2] + 32,
						nodeData[3] + 32, BufferedImage.TYPE_4BYTE_ABGR);

				Graphics2D g2 = (Graphics2D) image.getGraphics();
				GraphicsUtil.setAntiAlias(g2, true);
				RenderNodeUtil.drawNodeBox(g2, 16, 16, nodeData[2],
						nodeData[3], colorNode, getColorShadow(), true,
						getColorInterior(), true, isSelected(), 32, camera);
				if (camera.getScale() > 0.65F) {
					RenderNodeUtil.drawTitle(g2, getName(), 16, 16, 16,
							colorNode, getColorShadow(), true, camera);
				}
				g2.dispose();
				renderCache.setCache(image, newKey);
			}
			BufferedImage image = renderCache.getCache();
			g2d.drawImage(image, nodeData[0] - 16, nodeData[1] - 16, null);
		} else if (pass == 2) {
			drawMaximizeButton(g2d, colorNode, camera);
		}
	}

	private float acceleration = 1.00F;

	public void updateScaled(GraphCamera camera) {
		int[] nodeData = getCameraSpace();
		float scale = camera.getScale();

		int widthHalved = (nodeData[2] / 2);
		int heightHalved = (nodeData[3] / 2);

		int xMiddle = nodeData[0] + widthHalved;
		int yMiddle = nodeData[1] + heightHalved;

		int dist = (int) ((widthHalved / heightHalved) * phase * acceleration);

		int xDistance = widthHalved + dist;
		int yDistance = heightHalved + dist;

		int scaledOffset = (int) (scale);

		int x1 = (xMiddle - xDistance) - scaledOffset;
		int x2 = (xMiddle + xDistance) + scaledOffset;
		int y1 = (yMiddle - yDistance) - scaledOffset;
		int y2 = (yMiddle + yDistance) + scaledOffset;

		MainPanel panel = getMainPanel();
		
		int width = panel.getWidth();
		int height = panel.getHeight();
		
		if(x1 < -32)
		{
			x1 = -32;
		}
		if(x2 > width + 32)
		{
			x2 = width + 32;
		}
		
		if(y1 < -32)
		{
			y1 = -32;
		}
		
		if(y2 > height + 32)
		{
			y2 = height + 32;
		}
		phaseSpace = new int[] { x1, y1, x2 - x1, y2 - y1 };

		acceleration += 0.125F / (scale / 2);
		if(acceleration > 8F)
		{
			acceleration = 8F;
		}
	}

	public void drawTransition(Graphics2D g2d, GraphCamera camera) {
		if (phase <= 100) {
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1F - 0.01F * (float) phase));
			Color colorNode = isSelected() ? getColorSelected() : getColor();
			RenderNodeUtil.drawNodeBox(g2d, phaseSpace[0], phaseSpace[1],
					phaseSpace[2], phaseSpace[3], colorNode, getColorShadow(),
					true, getColorInterior(), true, isSelected(), 32, camera);
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1F));
		}
		
	}

	private void drawMaximizeButton(Graphics2D g2d, Color color,
			GraphCamera camera) {

		Color lastColor = g2d.getColor();

		Font lastFont = g2d.getFont();

		g2d.setStroke(new BasicStroke(2));

		g2d.setColor(color);

		int[] buttonData = NodeButton.getCameraSpace(buttonMaximize, camera);

		int x = buttonData[0];

		int y = buttonData[1];

		int width = buttonData[2];

		int height = buttonData[3];

		int curveValue = (int) (16 * camera.getScale());

		g2d.fillRoundRect(x, y, width, height, curveValue, curveValue);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.black);
		g2d.fillRoundRect(buttonData[0] + 2, buttonData[1] + 2, width - 4,
				height - 4, curveValue, curveValue);

		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(1.5F));
		if (camera.getScale() > 0.75F) {
			if (!buttonMaximize.isOn()) {
				g2d.drawLine(x + (width / 2), y + 5, x + (width / 2), y
						+ height - 6);
			}
			g2d.drawLine(x + 5, y + (height / 2), x + width - 6, y
					+ (height / 2));
		}
		g2d.setFont(lastFont);
		g2d.setColor(lastColor);
	}

	@Override
	public void handleMousePressed(int[] mousePressed, MouseEvent e,
			GraphCamera camera, GraphKeys keys) {
		// Left-Click
		if (e.getButton() == MouseEvent.BUTTON1) {
			setMovable(true);
			if (this.containsMouse(mousePressed, camera)) {
				if (camera.getCursorController().getCurrentTask()
						.equals("default")) {
				}
				originalPosition = getLocation();
				setSelected(true);
			} else if (buttonMaximize.containsMouse(mousePressed, camera)) {
				setSelected(true);
				originalPosition = getLocation();
				buttonMaximize.toggle();
			} else {
				if (keys.isShiftDown()) {
					originalPosition = getLocation();
				} else {
					setSelected(false);
				}

			}
		} else {
			setMovable(false);
		}
	}
	
	public void resetTransition(int value)
	{
		phase = value;
	}

	@Override
	public void handleMouseDragged(int[] mousePressed, int[] mouseDelta,
			MouseEvent e, GraphCamera camera, GraphKeys keys,
			CursorController cursorController) {
		if (isSelected() && isMovable()) {
			setLocation((int) (originalPosition[0] + mouseDelta[0]),
					(int) (originalPosition[1] + mouseDelta[1]));

		}
	}

	@Override
	public void handleMouseReleased(int[] mousePressed, int[] mouseDelta,
			int[] mouseReleased, MouseEvent e, GraphCamera camera,
			GraphKeys keys, CursorController cursorController) {
	}

	@Override
	public void handleKeyPressed(KeyEvent e, GraphCamera camera) {

	}

	@Override
	public void handleKeyReleased(KeyEvent e, GraphCamera camera) {

	}

	@Override
	public void handleKeyTyped(KeyEvent e, GraphCamera camera) {

	}

	@Override
	public boolean handleMouseMoved(int[] mouseMoved, MouseEvent e,
			GraphCamera camera, GraphKeys keys,
			CursorController cursorController) {
		if (containsMouse(mouseMoved, camera)
				|| this.buttonMaximize.containsMouse(mouseMoved, camera)) {
			if (cursorController.getCurrentTask().equals("default")) {
				cursorController.setCursorType("blue.hand");
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean changed() {
		int[] location = getLocation();
		int[] lastLocation = getLastLocation();
		boolean locationChanged = (location[0] != lastLocation[0] || location[1] != lastLocation[1]);
		return locationChanged;
	}

	@Override
	public void setChanged() {
		setLastLocation(getLocation());
	}

}
