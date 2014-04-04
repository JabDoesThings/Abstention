package com.fourtyblocksunder.abstention.game.object;

import java.util.HashMap;

import com.fourtyblocksunder.abstention.scene.Scene;

/**
 * @author JabJabJab
 * 
 *         This class is designed to store & modulate Scene Objects into groups.
 *         This can be useful for transforms, rotations, and diversity in your
 *         Scene.
 */
public class Cluster {

	/**
	  * Boolean controlling whether or not to render this cluster.
	  */
	private boolean visible = false;

	/**
	 * Name of the cluster.
	 */
	private String clusterName;

	/**
	 * ID of the cluster stored as an Integer.
	 */
	private int clusterID;

	/**
	 * Integer used to store count of clusters instanced. Useful to assigning
	 * IDs to new cluster instances.
	 */
	private static int clusterCount;

	private HashMap<String, SceneObject> mapSceneObjects;

	private Scene scene;

	/**
	 * Main constructor.
	 * 
	 * @param clusterName
	 */

	public Cluster(String clusterName, Scene scene) {
		initialize(true, clusterName);
		setScene(scene);
	}

	private void setScene(Scene scene) 
	{
		this.scene = scene;
	}

	/**
	 * Initializes the cluster.
	 * 
	 * @param clusterName
	 */
	void initialize(boolean loadObjects, String clusterName) {

		// Sets the Cluster's name.
		setClusterName(clusterName);

		// Sets the Cluster's ID, incrementing the count of clusters globally.
		setClusterID(clusterCount++);

		if (loadObjects) {
			initializeObjectMap();
		}
	}

	private void initializeObjectMap() {
		mapSceneObjects = new HashMap<>();
	}

	/**
	 * Resets data for the cluster.
	 */
	public void reset() {
		// Re-Initializes the cluster, wiping any data given to the object.
		initialize(false, getName());

		for (SceneObject object : mapSceneObjects.values()) {
			object.reset();
		}
	}

	/**
	 * Returns the name of the cluster.
	 * @return
	 */
	private String getName() {
		return this.clusterName;
	}

	/**
	 * Sets the cluster's ID.
	 * 
	 * @param clusterID
	 */
	void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	/**
	 * Sets the cluster's name.
	 * 
	 * @param clusterName
	 */
	void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * Sets the visible flag in the cluster to the boolean given.
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void render() {
		for (SceneObject object : mapSceneObjects.values()) {
			object.render();
		}
	}

	/**
	 * Returns default cluster.
	 * 
	 * @return
	 */
	public Cluster getDefaultCluster() {
		return getScene().getDefaultCluster();
	}

	public Scene getScene() 
	{
		return this.scene;
	}
	
	public int getID()
	{
		return this.clusterID;
	}
}
