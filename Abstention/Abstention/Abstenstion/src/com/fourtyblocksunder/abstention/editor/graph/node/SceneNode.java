package com.fourtyblocksunder.abstention.editor.graph.node;

import java.awt.Graphics2D;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.data.SceneData;
import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraph;

public class SceneNode extends BasicNode{

	public SceneNode(MainPanel mainPanel, String name, int width, int height,
			SceneData sceneData) {
		super(mainPanel, name, width, height, validateData(sceneData));
	}
	
	private static SceneData validateData(SceneData sceneData) {
		return sceneData == null ? new SceneData() : sceneData;
	}

	@Override
	public void drawActiveNode(Graphics2D g2d, GraphCamera camera, int pass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateActiveNode(NodeGraph graph, GraphCamera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDoubleClicked(int[] mousePressed, GraphCamera camera) {
		// TODO Auto-generated method stub
		
	}

}
