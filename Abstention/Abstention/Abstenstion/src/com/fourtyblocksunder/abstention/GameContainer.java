package com.fourtyblocksunder.abstention;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

/**
 * Sub-class of Slick's AppGameContainer to load project data alongside
 * Slick!'s engine.
 * @author JabJabJab
 *
 */
public class GameContainer extends AppGameContainer {

	
	//private Project project; 
	/**
	 * Main constructor.
	 * @param game
	 * @throws SlickException
	 */
	public GameContainer(Game game) throws SlickException {
		super(game);
		//Initialize Abstention's game engine along side Slick!'s engine.
		initialize();
	}

	/**
	 * Initializes handling of loading project files and initializing the engine.
	 */
	private void initialize() {
		
	}

}
