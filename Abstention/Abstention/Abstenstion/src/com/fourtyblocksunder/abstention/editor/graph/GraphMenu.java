package com.fourtyblocksunder.abstention.editor.graph;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.fourtyblocksunder.abstention.editor.graph.button.Button;
import com.fourtyblocksunder.abstention.util.CursorController;

public class GraphMenu {

	private Button buttonAdd;

	private BufferedImage imageButtonAdd;

	private Button buttonMouse;

	private BufferedImage imageButtonMouse;

	private float nodeOpacity = 0F;

	private Button buttonSelect;

	private Button buttonSlice;

	private BufferedImage imageButtonSelect;

	private BufferedImage imageButtonSlice;

	private Rectangle menuBottom;

	private int[] imageButtonAddOffset;

	private int[] imageButtonMouseOffset;

	private int[] imageButtonSelectOffset;

	private int[] imageButtonSliceOffset;

	private NodeGraph graph;

	private boolean clickedOn;

	public GraphMenu(NodeGraph graph) {
		setNodeGraph(graph);
		initialize();
	}

	private void setNodeGraph(NodeGraph nodeGraph) {
		this.graph = nodeGraph;
	}

	public NodeGraph getNodeGraph() {
		return this.graph;
	}

	private void initialize() {
		initializeButtons();
	}

	private void initializeButtons() {
		NodeGraph graph = getNodeGraph();
		GraphCamera camera = graph.getGraphCamera();

		menuBottom = new Rectangle();
		String keyButtonAdd = "blue.default";
		String keyButtonMouse = "blue.default";
		String keyButtonSelect = "blue.box_select";
		String keyButtonSlice = "blue.pen";

		buttonSlice = new Button();
		imageButtonSlice = camera.getCursorController().getCursor(
				keyButtonSlice);
		imageButtonSliceOffset = camera.getCursorController().getCursorOffset(
				keyButtonSlice);

		buttonAdd = new Button();
		imageButtonAdd = camera.getCursorController().getCursor(keyButtonAdd);
		imageButtonAddOffset = camera.getCursorController().getCursorOffset(
				keyButtonAdd);

		buttonMouse = new Button();
		imageButtonMouse = camera.getCursorController().getCursor(
				keyButtonMouse);
		imageButtonMouseOffset = camera.getCursorController().getCursorOffset(
				keyButtonMouse);

		buttonSelect = new Button();
		imageButtonSelect = camera.getCursorController().getCursor(
				keyButtonSelect);
		imageButtonSelectOffset = camera.getCursorController().getCursorOffset(
				keyButtonSelect);

		updateButtons();
	}

	public void update() {
		updateButtons();
	}

	public void updateButtons() {
		NodeGraph graph = getNodeGraph();
		int width = graph.getWidth();
		int height = graph.getHeight();
		
		buttonAdd.setLocation(8, height - 40);
		buttonAdd.setWidth(32);
		buttonAdd.setHeight(32);

		buttonSlice.setLocation(width - (36 * 3) - 6, getNodeGraph()
				.getHeight() - 40);
		buttonSlice.setWidth(32);
		buttonSlice.setHeight(32);

		buttonMouse.setLocation(width - (36 * 2) - 6, getNodeGraph()
				.getHeight() - 40);
		buttonMouse.setWidth(32);
		buttonMouse.setHeight(32);

		buttonSelect.setLocation(width - (36 * 1) - 6, height - 40);
		buttonSelect.setWidth(32);
		buttonSelect.setHeight(32);
	}

	public void renderMenu(Graphics2D g2d, GraphCamera camera) {
		// Store our original settings.
		Color lastColor = g2d.getColor();
		Paint lastPaint = g2d.getPaint();
		Composite lastComposite = g2d.getComposite();
		Stroke lastStroke = g2d.getStroke();

		if (nodeOpacity < 1F) {
			updateOpacity();
		}
		Composite composite = null;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				Math.min(nodeOpacity, 1));
		g2d.setComposite(composite);

		// Get panel dimensions.
		int width = getNodeGraph().getWidth();
		int height = getNodeGraph().getHeight();

		menuBottom.setBounds(0, height - 48, width, 48);

		g2d.setColor(new Color(20, 20, 25, 200));
		g2d.fill(menuBottom);
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(80, 80, 85, 200));
		g2d.drawRect(menuBottom.x + 1, menuBottom.y + 1, menuBottom.width - 2,
				menuBottom.height - 2);

		renderButtons(g2d, camera);
		// Reset to our original settings.
		g2d.setColor(lastColor);
		g2d.setPaint(lastPaint);
		g2d.setStroke(lastStroke);
		g2d.setComposite(lastComposite);

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
	 * Renders buttons for the menu.
	 * 
	 * @param g2d
	 * @param camera
	 */
	private void renderButtons(Graphics2D g2d, GraphCamera camera) {
		g2d.setColor(new Color(100, 100, 100));

		renderButton(buttonAdd, imageButtonAdd, imageButtonAddOffset, g2d,
				camera);

		renderButton(buttonSlice, imageButtonSlice, imageButtonSliceOffset,
				g2d, camera);

		renderButton(buttonMouse, imageButtonMouse, imageButtonMouseOffset,
				g2d, camera);

		renderButton(buttonSelect, imageButtonSelect, imageButtonSelectOffset,
				g2d, camera);

	}

	private void renderButton(Button button, BufferedImage icon, int[] offset,
			Graphics2D g2d, GraphCamera camera) {
		int[] buttonData = button.getLocalData();
		int x = buttonData[0];
		int y = buttonData[1];
		int width = buttonData[2];
		int height = buttonData[3];

		Color lastColor = g2d.getColor();
		Stroke lastStroke = g2d.getStroke();
		g2d.fillRect(x, y, width, height);
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(50, 50, 50));
		g2d.drawRect(x, y, width, height);
		g2d.drawImage(icon, x - offset[2], y - offset[3], null);
		g2d.setColor(lastColor);
		g2d.setStroke(lastStroke);
	}

	public boolean handleMousePressed(int[] mousePressed, MouseEvent e) {

		NodeGraph graph = getNodeGraph();
		GraphCamera camera = graph.getGraphCamera();

		if (mouseIsInMenu(menuBottom, mousePressed)) {
			CursorController cursorController = camera.getCursorController();
			if (Button.containsPoint(buttonMouse.getLocalData(), mousePressed)) {
				cursorController.setCurrentTask("default");
				cursorController.setCursorType("blue.default");
			} else if (Button.containsPoint(buttonSelect.getLocalData(),
					mousePressed)) {
				cursorController.setCurrentTask("box_select");
				cursorController.setCursorType("blue.box_select");
			} else if (Button.containsPoint(buttonSlice.getLocalData(),
					mousePressed)) {
				cursorController.setCurrentTask("slice");
				cursorController.setCursorType("blue.pen");
			} else if (Button.containsPoint(buttonAdd.getLocalData(),
					mousePressed)) {
				getNodeGraph().createNode();
			}

			return this.clickedOn = true;
		} else {

		}
		return this.clickedOn = false;
	}

	private boolean mouseIsInMenu(Rectangle menu, int[] mousePressed) {
		Rectangle mouse = new Rectangle(mousePressed[0] - 1,
				mousePressed[1] - 1, 3, 3);
		return menu.contains(mouse) || menu.intersects(mouse);
	}

	public void handleMouseReleased(int[] mousePressed, int[] mouseDelta,
			int[] mouseReleased, MouseEvent e) {
		setHandledClick(false);

	}

	public void handleMouseDragged(int[] mousePressed, int[] mouseDelta,
			MouseEvent e) {

	}

	public void handleKeyPressed(KeyEvent e) {

	}

	public void handleKeyTyped(KeyEvent e) {

	}

	public boolean handledClick() {
		// TODO Auto-generated method stub
		return this.clickedOn;
	}

	private void setHandledClick(boolean b) {
		this.clickedOn = b;
	}

}
