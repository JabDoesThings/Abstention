package com.fourtyblocksunder.abstention.editor.data;

/**
 * Class designed to store generic data for nodes.
 * 
 * @author Joshua Edwards
 * 
 */
public class NodeData {
	/**
	 * The X coordinate on graph-space.
	 */
	private int x = 0;

	/**
	 * The Y coordinate on graph-space.
	 */
	private int y = 0;

	/**
	 * Main constructor for GSON handling.
	 */
	public NodeData() {
	}

	/**
	 * Returns the X coordinate of a node in graph-space.
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Returns the Y coordinate of a node in graph-space.
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Sets the X coordinate of a node in graph-space.
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the Y coordinate of a node in graph-space.
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Sets the location of a node in graph-space.
	 * 
	 * @param location
	 */
	public void setLocation(int[] location) {
		this.x = location[0];
		this.y = location[1];
	}

	/**
	 * Sets the location of a node in graph-space.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns a integer array of the location of a node in graph-space.
	 * 
	 * @return
	 */
	public int[] getLocation() {
		return new int[] { x, y };
	}
}
