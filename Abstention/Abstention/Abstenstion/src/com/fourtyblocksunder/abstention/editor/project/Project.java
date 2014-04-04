package com.fourtyblocksunder.abstention.editor.project;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import com.fourtyblocksunder.abstention.editor.data.ProjectData;
import com.fourtyblocksunder.abstention.editor.graph.node.Node;

/**
 * 
 * @author Joshua Edwards
 * 
 */
public class Project {

	/**
	 * Folder location of the project's files.
	 */
	private File folderLocation;

	private ProjectData data;

	private String name = "Untitled Project";

	private HashMap<String, Node> listNodes;

	/**
	 * Main constructor.
	 * 
	 * @param projectName
	 */
	public Project(String projectName) {
		setName(projectName);
		initialize();
	}

	private void setName(String projectName) {
		this.name = projectName;
	}

	public void initialize() {
		listNodes = new HashMap<>();
	}

	public ProjectData getData() {
		return this.data;
	}

	public void setData(ProjectData data) {
		this.data = data;
	}

	/**
	 * Sets the folder location responsible for project information.
	 * 
	 * @param folderLocation
	 */
	public void setFolderLocation(File folderLocation) {
		this.folderLocation = folderLocation;
	}

	/**
	 * Returns the folder location responsible for project information.
	 * 
	 * @return
	 */
	public File getFolderLocation() {
		return this.folderLocation;
	}

	public Collection<Node> getNodes() {
		return this.listNodes.values();
	}

	public void addNode(Node node) {
		boolean foundName = false;
		String uniqueName = node.getName();
		int nameAdd = 0;
		while(!foundName)
		{
			boolean exists = false;
			for(String nodeName : listNodes.keySet())
			{
				if(nodeName.equals(uniqueName))
				{
					exists = true;
					nameAdd++;
					uniqueName = node.getName() + "_" + nameAdd;
					break;
				}
			}
			if(!exists)
			{
				node.setName(uniqueName);
				foundName = true;
			}
		}
		this.listNodes.put(uniqueName, node);
	}

	public Node getNode(String nodeName) {
		return this.listNodes.get(nodeName.toLowerCase());
	}

	public void removeNode(Node node) {
		this.listNodes.remove(node);
	}
	
	public String getName()
	{
		return this.name;
	}
}
