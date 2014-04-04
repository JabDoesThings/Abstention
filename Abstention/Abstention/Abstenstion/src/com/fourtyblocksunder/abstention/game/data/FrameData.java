package com.fourtyblocksunder.abstention.game.data;

import java.io.Serializable;

/**
 * Class Containing all data for a single frame.
 * @author JabJabJab
 *
 */
public class FrameData implements Serializable{

	private static final long serialVersionUID = -5101781634107201527L;

	/**
	 * Array of Transform data for this frame.
	 */
	public TransformData[] transformData;
	
}
