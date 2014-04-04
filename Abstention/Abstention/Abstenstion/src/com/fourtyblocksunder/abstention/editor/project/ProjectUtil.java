package com.fourtyblocksunder.abstention.editor.project;

import java.awt.Color;

import com.fourtyblocksunder.abstention.editor.MainFrame;
import com.fourtyblocksunder.abstention.editor.graph.node.DialogNode;

public class ProjectUtil {

/*	public static Project loadProject(File folderLocation) {
		
		if(folderLocation.exists())
		{
			if(folderLocation.isDirectory())
			{
				if(folderLocation.list().length > 0)
				{
					
				}
			}
		}
		
		Project project = new Project();

		project.setFolderLocation(folderLocation);

		
		
		return project;
	} */
	
	public static Project newProject(String projectName, MainFrame mainFrame)
	{
		Project project = new Project(projectName);
		DialogNode nodeEnter = new DialogNode(mainFrame.getMainPanel(), "enter", null);
		DialogNode nodeExit = new DialogNode(mainFrame.getMainPanel(), "exit", null);

		nodeEnter.setColor(new Color(150, 175, 150));
		nodeEnter.setColorSelected(new Color(150, 200, 150));
		nodeEnter.setLocation(0, 24);
		
		nodeExit.setColor(new Color(175, 150, 150));
		nodeExit.setColorSelected(new Color(200, 150, 150));
		nodeExit.setLocation(600, 24);

		//nodeEnter.addConnectedNode(nodeExit);

		project.addNode(nodeEnter);
		project.addNode(nodeExit);

		DialogNode node5 = new DialogNode(mainFrame.getMainPanel(), "Basic Node 1", null);
		node5.setLocation(400, 400);

		project.addNode(node5);

		return project;
	}
}
