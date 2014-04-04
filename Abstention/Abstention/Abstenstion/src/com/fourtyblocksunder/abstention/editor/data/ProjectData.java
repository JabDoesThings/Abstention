package com.fourtyblocksunder.abstention.editor.data;

import java.util.ArrayList;

public class ProjectData {

	private ArrayList<GraphData> listGraphData;

	private SettingsData settingsData;

	public ProjectData() {
	}

	public void addGraphData(GraphData graphData) {
		if (listGraphData == null) {
			listGraphData = new ArrayList<GraphData>();
		}
		listGraphData.add(graphData);
	}

	public void removeGraphData(GraphData graphData) {
		if (listGraphData != null) {
			listGraphData.remove(graphData);
		}
	}

	public ArrayList<GraphData> getGraphDataList() {
		return this.listGraphData;
	}

	public SettingsData getSettingsData() {
		return this.settingsData;
	}

	public void setSettingsData(SettingsData settingsData) {
		this.settingsData = settingsData;
	}
}
