package com.fourtyblocksunder.abstention.editor.graph;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.graph.node.DialogNode;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;
import com.fourtyblocksunder.abstention.editor.project.Project;
import com.fourtyblocksunder.abstention.util.CursorController;
import com.fourtyblocksunder.abstention.util.GraphicsUtil;

public class NodeGraphMain extends NodeGraph {

	private int[] locationNewNode = null;

	public NodeGraphMain(MainPanel mainPanel) {
		super(mainPanel,null);
	}

	@Override
	public void initialize() {
		setGraphCamera(new GraphCamera(this));
		
		setGraphMenu(new GraphMenu(this));

		setGraphKeys(new GraphKeys());
	}

	@Override
	public void updateGraph() {
		GraphCamera camera = getGraphCamera();
		camera.update();
		
		for (int pass = 0; pass < 2; pass++)
			for (Node node : getNodes()) {
				node.update(camera, pass);
			}
	}

	@Override
	public void addNode(Node node) {
		
	}

	@Override
	public void createNode() {
		Project project = getProject();
		Node nodeEnter = project.getNode("enter");
		Node nodeExit = project.getNode("exit");
		GraphCamera camera = getGraphCamera();

		DialogNode node = new DialogNode(getMainPanel(), "A Dialog", null);

		int[] location;

		if (locationNewNode == null) {
			location = locationNewNode = camera.getLocation();
		} else {
			int x = locationNewNode[0];
			int y = locationNewNode[1] - node.getHeight() - 16;

			location = locationNewNode = new int[] { x, y };
		}
		int[] windowSize = new int[] { getWidth(), getHeight() };

		int x = (int) (((-location[0] * camera.getScale())
				+ (windowSize[0] / 2) - (node.getWidth() / 2)) / camera
				.getScale());
		int y = (int) (((-location[1] * camera.getScale())
				+ (windowSize[1] / 2) - (node.getHeight() / 2)) / camera
				.getScale());

		node.setLocation(x, y);
		if (nodeEnter != null) {
			nodeEnter.addConnectedNode(node);
		}
		if (nodeExit != null) {
			node.addConnectedNode(nodeExit);
		}

		project.addNode(node);
	}

	@Override
	public Collection<Node> getNodes() {
		return getProject().getNodes();
	}

	@Override
	public void renderGraph(Graphics2D g2d) {
		
		GraphCamera camera = getGraphCamera();

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_SPEED);

		// Turn on anti-aliasing.
		GraphicsUtil.setAntiAlias(g2d, true);

		int passes = camera.getRenderPasses();

		for (int pass = 0; pass < passes; pass++) {
			for (Node node : getNodes()) {
				node.draw(g2d, camera, pass);
			}
		}
	}

	@Override
	public void handleMousePressed(int[] mousePressed, MouseEvent e) {
		GraphMenu menu = getGraphMenu();
		GraphCamera camera = getGraphCamera();
		GraphKeys keys = getGraphKeys();

		menu.handleMousePressed(mousePressed, e);
		if (!menu.handledClick()) {
			camera.handleMousePressed(mousePressed, e);
			for (Node node : getNodes()) {
				node.handleMousePressed(mousePressed, e, camera, keys);
			}
		}

	}

	@Override
	public void handleMouseDragged(int[] mousePressed, int[] mouseDelta,
			MouseEvent e) {
		GraphCamera camera = getGraphCamera();
		GraphKeys keys = getGraphKeys();
		GraphMenu menu = getGraphMenu();

		if (camera.isMovable()) {
			locationNewNode = null;
		}
		if (!menu.handledClick()) {
			for (Node node : getNodes()) {
				node.handleMouseDragged(mousePressed, mouseDelta, e, camera,
						keys, camera.getCursorController());
			}
		}
		camera.handleMouseDragged(mousePressed, mouseDelta, e);
	}

	@Override
	public void handleMouseMoved(int[] mouseMoved, MouseEvent e) {
		GraphCamera camera = getGraphCamera();
		GraphKeys keys = getGraphKeys();
		CursorController cursorController = camera.getCursorController();
		boolean mouseOverNode = false;

		for (Node node : getNodes()) {
			if (node.handleMouseMoved(mouseMoved, e, camera, keys,
					cursorController)) {
				mouseOverNode = true;
				break;
			}
		}
		if (!mouseOverNode) {
			if (cursorController.getCurrentTask().equals("default")) {
				cursorController.setCursorType("blue.default");
			} else if (cursorController.getCurrentTask().equals("box_select")) {
				cursorController.setCursorType("blue.box_select");
			} else if (cursorController.getCurrentTask().equals("slice")) {
				cursorController.setCursorType("blue.pen");
			}

		}
	}

	@Override
	public void handleKeyPressed(KeyEvent e) {
		GraphCamera camera = getGraphCamera();
		GraphKeys keys = getGraphKeys();
		
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			keys.setAlt(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			keys.setCtrl(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			keys.setShift(true);
		}

		for (Node node : getNodes()) {
			node.handleKeyPressed(e, camera);
		}

		camera.handleKeyPressed(e);
	}

	@Override
	public void handleKeyReleased(KeyEvent e) {
		GraphCamera camera = getGraphCamera();
		GraphKeys keys = getGraphKeys();

		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			keys.setAlt(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			keys.setCtrl(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			keys.setShift(false);
		}
		
		camera.handleKeyReleased(e);
	}

	@Override
	public void handleKeyTyped(KeyEvent e) {
		GraphCamera camera = getGraphCamera();
		for (Node node : getNodes()) {
			node.handleKeyTyped(e, camera);
		}
		camera.handleKeyTyped(e);
	}

	@Override
	public void handleMouseWheelMoved(MouseWheelEvent e) {
		GraphCamera camera = getGraphCamera();
		if (!camera.handleMouseWheelMoved(e)) {

		}
	}

	@Override
	public void handleDoubleClick(int[] mousePressed, MouseEvent e) {
		System.out.println("double click");
		GraphCamera camera = getGraphCamera();
		for (Node node : getNodes()) {
			if(node.containsMouse(mousePressed, camera))
			{
				node.handleDoubleClicked(mousePressed, camera);
			}
		}
	}

	@Override
	public void handleMouseReleased(int[] mousePressed, int[] mouseDelta, int[] mouseReleased, MouseEvent e) {
		GraphCamera camera = getGraphCamera();
		GraphMenu menu = getGraphMenu();
		GraphKeys keys = getGraphKeys();
		boolean menuClicked = menu.handledClick();
		
		camera.handleMouseReleased(e);
		
		if (!menu.handledClick()) {
			for (Node node : getNodes()) {
				node.handleMouseReleased(mousePressed, mouseDelta,
						mouseReleased, e, camera, keys,
						camera.getCursorController());
			}
		}
		if (!menuClicked) {
			menu.handleMouseReleased(mousePressed, mouseDelta,
					mouseReleased, e);
		}		
	}
	
}
