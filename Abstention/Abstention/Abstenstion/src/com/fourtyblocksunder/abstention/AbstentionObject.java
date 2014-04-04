package com.fourtyblocksunder.abstention;

/**
 * Class containing any useful information for the object.
 * @author JabJabJab
 *
 */
public class AbstentionObject {

	/**
	 * Type of object to send data to.
	 * 
	 * 0 = data object
	 * 1 = image object
	 * 2 = animation object
	 * 3 = sound object
	 * 4 = script object
	 */
	public int objectType;
	
	/**
	 * Raw data to be interpreted by the object when initializing.
	 */
	public String[] data;
}
