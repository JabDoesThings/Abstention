package com.fourtyblocksunder.abstention.game.data;

import java.io.Serializable;

/**
 * Class containing all Cluster-Specific data.
 * @author JabJabJab
 *
 */
public class ClusterData implements Serializable{

	private static final long serialVersionUID = 7931283295725817022L;
	
	/**
	 * Object data array to store the objects used inside of a cluster.
	 */
	public SceneObjectData[] objectData;
	
	/**
	 * Animation data to store frames and things used for animations within
	 * a cluster.
	 */
	public AnimationData[] animationData;
	
}
