package com.fourtyblocksunder.abstention.editor.data;

public class CameraData {
	private int x = 0;

	private int y = 0;

	private float zoom = 1F;

	public CameraData() {
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public float getZoom() {
		return this.zoom;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

}
