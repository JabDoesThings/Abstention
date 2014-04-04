package com.fourtyblocksunder.abstention;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	
	private Game game;
	
	private static AppGameContainer appGameContainer;
	
	
	public Main()
	{
	      try{
	         appGameContainer = new AppGameContainer(new Game("Abstention"));
	         
	         boolean fullscreen = true;
	         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	         int width  = (int)screenSize.getWidth();
	         int height = (int)screenSize.getHeight();
	         appGameContainer.setTargetFrameRate(60);
	         
	         if(fullscreen)
	         {	        	 	 
	        	 appGameContainer.setDisplayMode(800, 600, true);
	         }
	         else
	         {
	        	 appGameContainer.setDisplayMode(width / 2, height / 2, false);	        	 
	         }
	         appGameContainer.start();
	      }catch(SlickException e){
	         e.printStackTrace();
	      }
	}
	
	public Game getGame()
	{
		return this.game;
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}
