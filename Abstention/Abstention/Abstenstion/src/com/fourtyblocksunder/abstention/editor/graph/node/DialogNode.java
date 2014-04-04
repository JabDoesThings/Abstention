package com.fourtyblocksunder.abstention.editor.graph.node;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.fourtyblocksunder.abstention.editor.MainPanel;
import com.fourtyblocksunder.abstention.editor.data.DialogData;
import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraph;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraphDialog;
import com.fourtyblocksunder.abstention.util.GraphicsUtil;

/**
 * 
 * @author Joshua Edwards
 * 
 */
public class DialogNode extends BasicNode {

	private NodeGraphDialog nodeGraphDialog;

	private ArrayList<Node> listNodes;

	public DialogNode(MainPanel mainPanel, String name, DialogData dialogData) {
		super(mainPanel, name, 128, 64, validateData(dialogData));
		initializeGraph(mainPanel);
	}

	private void initializeGraph(MainPanel mainPanel) {
		listNodes = new ArrayList<Node>();

	}

	public void addNode(Node node) {
		listNodes.add(node);
	}

	public void removeNode(Node node) {
		listNodes.remove(node);
	}

	@Override
	public void drawActiveNode(Graphics2D g2d, GraphCamera camera, int pass) {
		drawTransition(g2d, camera);
	}

	@Override
	public void updateActiveNode(NodeGraph graph, GraphCamera camera) {
		updateTransition(camera);
	}

	public ArrayList<Node> getNodes() {
		return this.listNodes;
	}

	private static DialogData validateData(DialogData dialogData) {
		return dialogData == null ? new DialogData() : dialogData;
	}

	@Override
	public void handleDoubleClicked(int[] mousePressed, GraphCamera camera) {
		MainPanel panel = getMainPanel();
		NodeGraph graph = panel.getNodeGraph();
		NodeGraph graphLast = panel.getNodeGraphLast();
		if (graphLast != null) {
			if (graphLast instanceof NodeGraphDialog) {
				((NodeGraphDialog) graphLast).getActiveNode().resetTransition(101);
			}
		}
		if (nodeGraphDialog == null) {
			nodeGraphDialog = new NodeGraphDialog(panel, this, graph);
			DialogNode node = new DialogNode(panel, "test", null);
			DialogData data = (DialogData) getData();
			data.addNode(node.getData());
			addNode(node);
		}
		getMainPanel().setNodeGraph(nodeGraphDialog);
		resetTransition(0);
	}
}
