package com.fourtyblocksunder.abstention.game.object;

import com.fourtyblocksunder.abstention.scene.Scene;

/**
 * @author JabJabJab
 * 
 */
public abstract class SceneObject {

	/**
	 * The name of the scene object.
	 */
	private String objectName;

	/**
	 * The cluster this object is bound to.
	 */
	private Cluster cluster;

	/**
	 * Scene the object belongs to.
	 */
	private Scene scene;

	/**
	 * MainObject
	 * @param objectName
	 */
	public SceneObject(String objectName, Scene scene) {
		setCluster(scene.getDefaultCluster());
	}

	/**
	 * 
	 * @param name
	 * @param cluster
	 */
	public SceneObject(String sceneName, Cluster cluster) {
		initialize(sceneName);
		setCluster(cluster);
		setScene(cluster.getScene());
	}

	/**
	 * Sets the Scene Object to the scene using this object.
	 * @param scene
	 */
	private void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public Scene getScene()
	{
		return this.scene;
	}

	/**
	 * Sets the object to a represented Cluster group.
	 * 
	 * @param cluster
	 */
	private void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * Initializes the Object.
	 */
	void initialize(String sceneName) {
		setObjectName(sceneName);
	}

	void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public abstract void reset();

	public abstract void render();

	/**
	 * Returns the default cluster from the scene.
	 * @return
	 */
	public Cluster getCluster()
	{
		return this.cluster;
	}
	
	public String getName()
	{
		return this.objectName;
	}
}
