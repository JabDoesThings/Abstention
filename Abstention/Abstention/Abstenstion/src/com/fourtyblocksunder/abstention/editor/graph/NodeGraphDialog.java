package com.fourtyblocksunder.abstention.editor.graph;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.graph.node.DialogNode;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;
import com.fourtyblocksunder.abstention.util.CursorController;
import com.fourtyblocksunder.abstention.util.GraphicsUtil;

public class NodeGraphDialog extends NodeGraph {

	private DialogNode activeNode;
	private int[] locationNewNode = new int[] { 0, 0 };

	public NodeGraphDialog(MainPanel mainPanel, DialogNode activeNode,
			NodeGraph nodeGraphLast) {
		super(mainPanel, nodeGraphLast);
		setActiveNode(activeNode);
	}

	private void setActiveNode(DialogNode activeNode) {
		this.activeNode = activeNode;
	}

	public DialogNode getActiveNode() {
		return this.activeNode;
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
		DialogNode dialogNode = getActiveNode();
		camera.update();
		dialogNode.updateActiveNode(this, camera);
		for (int pass = 0; pass < 2; pass++) {
			for (Node node : getNodes()) {
				node.update(camera, pass);
			}
		}

	}

	@Override
	public void addNode(Node node) {
	}

	@Override
	public void createNode() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Node> getNodes() {
		return getActiveNode().getNodes();
	}

	@Override
	public void renderGraph(Graphics2D g2d) {
		GraphCamera camera = getGraphCamera();
		DialogNode dialogNode = getActiveNode();

		int passCount = camera.getRenderPasses();

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_SPEED);

		dialogNode.drawActiveNode(g2d, camera, 0);

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);

		GraphicsUtil.setRenderQuality(g2d, RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_SPEED);

		// Turn on anti-aliasing.
		GraphicsUtil.setAntiAlias(g2d, true);

		for (int pass = 0; pass < passCount; pass++) {
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			getMainPanel().setNodeGraph(getNodeGraphLast());
			return;
		}
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
