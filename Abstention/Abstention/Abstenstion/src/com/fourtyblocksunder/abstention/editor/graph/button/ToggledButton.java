package com.fourtyblocksunder.abstention.editor.graph.button;

/**
 * Class designed to contain the toggle-abilities for on-off buttons.
 * 
 * @author Joshua Edwards
 * 
 */
public class ToggledButton extends Button {

	/**
	 * On | Off storage boolean.
	 */
	private boolean on;

	/**
	 * Returns whether or not the button is on.
	 * 
	 * @return
	 */
	public boolean isOn() {
		return this.on;
	}

	/**
	 * Sets whether the botton is on or not.
	 * 
	 * @param on
	 */
	public void setOn(boolean on) {
		this.on = on;
	}

	/**
	 * Inverts the button's current state.
	 */
	public void toggle() {
		this.on = !this.on;
	}

}
