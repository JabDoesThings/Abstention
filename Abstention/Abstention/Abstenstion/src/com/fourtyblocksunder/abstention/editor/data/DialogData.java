package com.fourtyblocksunder.abstention.editor.data;

import java.util.ArrayList;

public class DialogData extends NodeData {

	ArrayList<NodeData> listNodes;

	public DialogData() {
		listNodes = new ArrayList<NodeData>();
	}
	
	public ArrayList<NodeData> getNodes()
	{
		return this.listNodes;
	}
	
	public void addNode(NodeData node)
	{
		this.listNodes.add(node);
	}
	
	public void removeNode(NodeData node)
	{
		this.listNodes.remove(node);
	}

}
