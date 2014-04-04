package com.fourtyblocksunder.abstention.game.data;

import java.io.Serializable;

/**
 * Class storing data for a specific transform during a scene.
 * @author JabJabJab
 */
public class TransformData implements Serializable{

	private static final long serialVersionUID = -5988163143580053755L;

	/**
	 * Frame being referenced where the transform occurs.
	 */
	public int frame;
	
	/**
	 * Cluster ID being transformed.
	 */
	public int clusterID;
	
	/**
	 * int for the x Position in the frame.
	 */
	public int xPosition;

	/**
	 * int for the y Position in the frame.
	 */
	public int yPosition;
	
	/**
	 * int for the z Position in the frame.
	 */
	public int zPosition;
	
	/**
	 * int for the x Rotation in the frame.
	 */
	public int xRotation;
	
	/**
	 * int for the y Rotation in the frame.
	 */
	public int yRotation;
	
	/**
	 * int for the z Rotation in the frame.
	 */
	public int zRotation;
	
	/**
	 * int for the x Scale in the frame.
	 */
	public int xScale;
	
	/**
	 * int for the y Scale in the frame.
	 */
	public int yScale;
	
	/**
	 * int for the z Scale in the frame.
	 */
	public int zScale;
}
