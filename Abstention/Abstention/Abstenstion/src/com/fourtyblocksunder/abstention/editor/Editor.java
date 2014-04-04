package com.fourtyblocksunder.abstention.editor;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Main class for the editor program.
 * 
 * @author Joshua Edwards
 * 
 */
public class Editor {

	/**
	 * Flags whether or not the program is in debug mode, for debug writes.
	 */
	private static boolean debug = true;

	/**
	 * Object that manages the main JFrame window.
	 */
	public static MainFrame mainFrame;

	/**
	 * Main constructor.
	 */
	public Editor() {
		//Initialize the program.
		initialize();
	}

	public void initialize() {
		//Initialize the main JFrame.
		mainFrame = new MainFrame(this);
	}

	/**
	 * Returns the version of the program.
	 * @return
	 */
	public String getVersion() {
		return "0.01";
	}

	/**
	 * Returns if the program is set in debug mode.
	 * 
	 * @return
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the mode of the program for debug.
	 * 
	 * @param b
	 */
	public void setDebug(boolean b) {
		debug = b;
	}

	/**
	 * Sets the look and feel of the program.
	 */
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName()) || "Macintosh".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println(info.getName());
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Main entry point of the editor program. Simply calling 'new Editor()'
	 * exits the static area of the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		new Editor();
	}
}
