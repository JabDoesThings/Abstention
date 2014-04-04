package com.fourtyblocksunder.abstention.editor.graph;

public class GraphKeys {
	private boolean ctrl;
	
	private boolean shift;
	
	private boolean alt;
	
	public void setCtrl(boolean key)
	{
		this.ctrl = key;
	}
	
	public void setShift(boolean key)
	{
		this.shift = key;
	}
	
	public void setAlt(boolean key)
	{
		this.alt = key;
	}
	
	public boolean isCtrlDown()
	{
		return ctrl;
	}
	
	public boolean isShiftDown()
	{
		return shift;
	}
	
	public boolean isAltDown()
	{
		return alt;
	}
}
