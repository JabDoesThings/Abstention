package com.fourtyblocksunder.abstention.scene;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.fourtyblocksunder.abstention.Abstention;
import com.fourtyblocksunder.abstention.game.object.Cluster;

/**
 * @author JabJabJab
 * 
 * This class is for handling the Scene data & execution of the scene.
 */
public abstract class Scene extends BasicGameState {
	
	/**
	 * HashMap to store the clusters used in the scene.
	 */
	private HashMap<String,Cluster> mapClusters;
	
	/**
	 * Name of the representing scene.
	 */
	private String sceneName;
	
	/**
	 * Static Integer to store the count of Scenes loaded.
	 * Used for sceneID assignment of a scene.
	 */
	public static int sceneCount;
	
	/**
	 * Integer storing the ID of a Scene. Used to look up a scene.
	 */
	private int sceneID;
	
	/**
	 * Instance of the main engine.
	 */
	private Abstention instance;
	
	/**
	 * Default cluster for the scene. 
	 */
	private Cluster clusterDefault = new Cluster("Default",this);

	/**
	 * Main constructor for Scene.
	 * @param sceneID
	 * @param sceneName
	 */
	public Scene(int sceneID, String sceneName)
	{
		//Initialize method for multi-constructor efficieny.
		initialize(sceneName);	
	}

	/**
	 * Initializes the Scene.
	 */
	void initialize(String sceneName) 
	{
		//Set the sceneName.
		setSceneName(sceneName);
		
		/*Set the sceneID as the 
		sceneCount and then increment the count. */
		setSceneId(sceneCount++);
		
		//Initialize our cluster map.
		mapClusters = new HashMap<>();
	}

	/**
	 * Sets the scene ID.
	 * @param i
	 */
	void setSceneId(int i) {
		
	}
	
	/**
	 * Returns the scene ID that represents this Scene instance.
	 * @return
	 */
	public int getSceneId()
	{
		return this.sceneID;
	}
	
	/**
	 * Returns the name of the Scene instance.
	 * @return
	 */
	public String getSceneName()
	{
		return this.sceneName;
	}
	
	/**
	 * Returns the main engine instance.
	 * @return
	 */
	public Abstention getEngine()
	{
		return this.instance;
	}
	
	@Override
	public String toString()
	{
		return sceneName;
	}

	/**
	 * Sets the scene's name. Used when looking up scene in the scene map.
	 * @param sceneName2
	 */
	void setSceneName(String sceneName) 
	{
		this.sceneName = sceneName;
	}
	
	Cluster getCluster(String key)
	{
		return mapClusters.get(key);
	}
	
	HashMap<String,Cluster> getClusterMap()
	{
		return this.mapClusters;
	}
	
	void render()
	{
		for(Cluster cluster : mapClusters.values())
		{
			if(cluster.isVisible())
			cluster.render();
		}
		if(clusterDefault.isVisible())
		{
			clusterDefault.render();
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException
			{
				loadObjects();
			}

	private void loadObjects() {
		
	}

	public abstract void initializeScene(GameContainer container, StateBasedGame game);

	@Override
	public abstract void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException;

	@Override
	public abstract void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException;

	@Override
	public int getID() {
		return this.sceneID;
	}

	public Cluster getDefaultCluster() {
		return this.clusterDefault;
	}
}
