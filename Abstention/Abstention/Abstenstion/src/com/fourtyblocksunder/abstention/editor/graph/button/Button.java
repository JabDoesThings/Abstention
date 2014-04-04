package com.fourtyblocksunder.abstention.editor.graph.button;

public class Button {

	private int[] location = new int[] { 0, 0 };
	private int width = 0;
	private int height = 0;

	public Button() {
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[] getLocation() {
		return this.location;
	}

	public void setLocation(int x, int y) {
		this.location = new int[] { x, y };
	}
	
	public int[] getLocalData()
	{
		return new int[] { location[0], location[1], width, height };
	}

	public static boolean containsPoint(int[] buttonData,
			int[] mouseLocation) {
		//Mouse data.
		int mouseX = mouseLocation[0];
		int mouseY = mouseLocation[1];
		//Button data.
		int x = buttonData[0];
		int y = buttonData[1];
		int width = buttonData[2];
		int height = buttonData[3];
		
		if (mouseX >= x && mouseX <= (x + width)) {
			if (mouseY >= y && mouseY <= (y + height)) {
				return true;
			}
		}
		return false;
	}
}
