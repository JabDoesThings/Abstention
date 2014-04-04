package com.fourtyblocksunder.abstention;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Class for Slick 'StateBasedGame' utilization entry and control.
 * @author JabJabJab
 *
 */
public class Game extends StateBasedGame{

	/**
	 * HashMap for states of the game.
	 */
	HashMap<Integer,BasicGameState> mapStates;
	
	/**
	 * Main constructor. 
	 * @param name - slick! uses this string for the name on the window.
	 */
	public Game(String name) {
		super(name);
		initialize();
	}

	/**
	 * Initializes on the main class-level. Used to load game project instructions.
	 */
	private void initialize() 
	{
		addState(new MainState(0));
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
	}

}
