package com.fourtyblocksunder.abstention.editor.graph;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;
import com.fourtyblocksunder.abstention.editor.project.Project;
import com.fourtyblocksunder.abstention.util.Cache;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;

public abstract class NodeGraph {

	/**
	 * The Camera being used by this Graph instance.
	 */
	private GraphCamera camera;

	/**
	 * Float used for transitioning between this graph and another.
	 */
	private float graphTransition = .5F;

	private Cache<BufferedImage> cacheTransition;

	private GraphMenu menu;

	private GraphKeys keys;

	private MainPanel mainPanel;

	private Project project;

	private NodeGraph nodeGraphLast = null;

	public abstract void initialize();

	public abstract void updateGraph();

	public abstract void addNode(Node node);

	public abstract void createNode();

	public abstract Collection<Node> getNodes();

	public abstract void renderGraph(Graphics2D g2d);

	public abstract void handleMousePressed(int[] mousePressed, MouseEvent e);

	public abstract void handleMouseDragged(int[] mousePressed,
			int[] mouseDelta, MouseEvent e);

	public abstract void handleMouseMoved(int[] mouseMoved, MouseEvent e);

	public abstract void handleMouseWheelMoved(MouseWheelEvent e);

	public abstract void handleMouseReleased(int[] mousePressed,
			int[] mouseDelta, int[] mouseReleased, MouseEvent e);

	public abstract void handleDoubleClick(int[] mousePressed, MouseEvent e);

	public abstract void handleKeyPressed(KeyEvent e);

	public abstract void handleKeyReleased(KeyEvent e);

	public abstract void handleKeyTyped(KeyEvent e);

	/**
	 * Main Constructor.
	 * 
	 * @param mainPanel
	 */
	public NodeGraph(MainPanel mainPanel, NodeGraph nodeGraphLast) {
		setMainPanel(mainPanel);
		setNodeGraphLast(nodeGraphLast);
		System.out.println(mainPanel);
		setProject(mainPanel.getMainFrame().getProject());
		initialize();
	}

	private void setNodeGraphLast(NodeGraph nodeGraphLast) {
		this.nodeGraphLast = nodeGraphLast;
	}

	public NodeGraph getNodeGraphLast() {
		return this.nodeGraphLast;
	}

	private void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return this.project;
	}

	public void setGraphMenu(GraphMenu menu) {
		this.menu = menu;
	}

	public GraphMenu getGraphMenu() {
		return this.menu;
	}

	private void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public MainPanel getMainPanel() {
		return this.mainPanel;
	}

	public GraphCamera getGraphCamera() {
		return camera;
	}

	public void setGraphCamera(GraphCamera camera) {
		this.camera = camera;
	}

	public void update() {
		GraphCamera camera = getGraphCamera();
		GraphMenu menu = getGraphMenu();
		camera.update();
		updateGraph();
		menu.update();
	}

	public void render(Graphics2D g2d) {
		GraphCamera camera = getGraphCamera();
		camera.renderPanel(g2d);
	}

	public int getWidth() {
		return getMainPanel().getWidth();
	}

	public int getHeight() {
		return getMainPanel().getHeight();
	}

	public GraphKeys getGraphKeys() {
		return this.keys;
	}

	public void setGraphKeys(GraphKeys keys) {
		this.keys = keys;
	}

}
