package com.fourtyblocksunder.abstention.editor.graph;

public abstract class GraphComponent {

	private int[] lastLocation = new int[] { 0, 0 };
	private float lastScale = 1F;

	public abstract boolean changed();
	
	public abstract void setChanged();

	public int[] getLastLocation() {
		return this.lastLocation;
	}

	public float getLastScale() {
		return this.lastScale;
	}

	public void setLastLocation(int x, int y) {
		this.lastLocation = new int[] { x, y };
	}

	public void setLastLocation(int[] location) {
		this.lastLocation = new int[] { location[0], location[1] };
	}

	public void setLastScale(float scale) {
		this.lastScale = scale;
	}
	
	public boolean locationChanged(int[] location)
	{
		int[] lastLocation = getLastLocation();
		boolean locationChanged = (lastLocation[0] != location[0] || lastLocation[1] != location[1]);
		return locationChanged;
	}
	
	public boolean scaleChanged(float scale)
	{
		boolean changed = getLastScale() != scale;
		return changed;
	}
}
