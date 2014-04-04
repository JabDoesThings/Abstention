package com.fourtyblocksunder.abstention;

import java.io.File;

/**
 * Main class for the Abstention game engine.
 * @author JabJabJab
 *
 */
public class Abstention 
{
	public File fileProject; 
	
	/**
	 * Main constructor. 
	 * @param fileProject
	 */
	public Abstention(File fileProject)
	{
		setProjectFile(fileProject);
	}

	/**
	 * sets the project file in the engine. 
	 * @param fileProject
	 */
	private void setProjectFile(File fileProject) 
	{
		this.fileProject = fileProject;
	}
}
