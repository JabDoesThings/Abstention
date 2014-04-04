package com.fourtyblocksunder.abstention.editor.graph.node.component;

import java.awt.Graphics2D;

import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;

/**
 * Class designed to define and execute functions required as components for
 * node-based operations.
 * 
 * @author Joshua Edwards
 * 
 */
public abstract class NodeComponent {

	/**
	 * Color object representing the color of the curves.
	 */
	//private Color colorCurve = new Color(120, 120, 120, 200);
	//private Color colorCurveSelected = new Color(colorCurve.getRed(),
	//		colorCurve.getGreen(), colorCurve.getBlue(), colorCurve.getAlpha());
	
	/**
	 * A set width to the component. If -1, assume inheritance of the parent Node's width.
	 */
	private int width;

	/**
	 * A set height to the component. If -1, assume inheritance of the parent Node's height.
	 */
	private int height;

	private Node node;
	
	public abstract void updateComponent();

	public abstract void drawComponent(Graphics2D g2d, GraphCamera camera, int pass);
	
	public NodeComponent(Node node)
	{
		setNode(node);
	}

	private void setNode(Node node) {
		this.node = node;
	}
	
	public Node getNode()
	{
		return this.node;
	}
	
	public int getWidth()
	{
		return width == 1? getNode().getWidth(): width;
	}
	
	public int getHeight()
	{
		return height;
	}
}
