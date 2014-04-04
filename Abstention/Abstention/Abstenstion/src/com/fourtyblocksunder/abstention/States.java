package com.fourtyblocksunder.abstention;

import org.newdawn.slick.state.StateBasedGame;

public class States {

	private StateBasedGame game;
	
	public States(StateBasedGame game)
	{
		setGame(game);
	}

	void setGame(StateBasedGame game) {
		this.game = game;
	}
	
	StateBasedGame getGame()
	{
		return this.game;
	}
}
