package com.fourtyblocksunder.abstention.game.data;

import java.io.Serializable;

/**
 * Main Data container Object carrying both Cluster data and Dialogue data.
 * @author JabJabJab
 *
 */
public class SceneData implements Serializable {

	private static final long serialVersionUID = -4264989638621345495L;

	/**
	 * Container storing Dialogue data for the scene.
	 */
	public DialogueData dialogueData;
	

	/**
	 * Container storing Cluster data 
	 */
	public ClusterData[] clusterData;
	
}
