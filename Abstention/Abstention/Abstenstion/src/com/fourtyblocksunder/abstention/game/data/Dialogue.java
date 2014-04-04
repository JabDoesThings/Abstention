package com.fourtyblocksunder.abstention.game.data;

/**
 * Class designed to store the Dialogue setup of a certain event.
 * @author JabJabJab
 *
 */
public class Dialogue {

	/**
	 * 0 = Yes / No.
	 * 1 = Ok / Continue;
	 * 2 = Custom.
	 */
	public int dialogueType;
	
	/**
	 * Identification of the dialogue. Used for locating and processing the dialogues.
	 */
	public int dialogueID;
	
	/**
	 * Array of String objects to represent the messages of each response choice within this dialogue.
	 */
	public String[] dialogueChoices;
	
	/**
	 * Same size as the dialogueChoices array. Gives a response dialogue ID to move to when chosen.
	 * 
	 * The way the data is stored in each long is as follows:
	 * 
	 *  Scene|Dialogue|
	 *  |----|--------|
	 *  |0000|0000    |
	 *  \-------------/
	 *  
	 *  You need to split the data of a long into two integers to properly use / store data.
	 */
	public long[] dialogueChoiceRoutes;
}
