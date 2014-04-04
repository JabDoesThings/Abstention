package com.fourtyblocksunder.abstention;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainState extends BasicGameState{

	
	Rectangle rect;
	public MainState(int i) {
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		rect = new Rectangle(20,20,20,20);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawString("Test",255,255);
		g.draw(rect);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
	}

	@Override
	public int getID() {
		return 0;
	}

}
