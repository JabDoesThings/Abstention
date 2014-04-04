package com.fourtyblocksunder.abstention.util;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author Joshua Edwards
 * 
 *         Class designed to control the cursor based upon the needs of the
 *         component assigned to it.
 * 
 */
public class CursorController {

	/**
	 * Enumeration designed to flag specific needs of the component.
	 * 
	 * @author JabJabJab
	 * 
	 */
	// public static enum CursorType {
	// DEFAULT, WAITING, UNAVALIABLE, BOX_SELECT, TEXT, HAND, NULL
	// }

	/**
	 * The current offset added to the position of the cursor to properly point.
	 */
	private int[] currentOffset = new int[] { 0, 0 };

	/**
	 * The type of cursor currently being requested by the component.
	 */
	// private CursorType cursorType = CursorType.DEFAULT;

	/**
	 * HashMap containing the offsets for specific cursors.
	 */
	private HashMap<String, int[]> mapOffsets;

	/**
	 * The current cursor being used by the component.
	 */
	private ArrayList<BufferedImage> currentImages;

	/**
	 * The component using this instance.
	 */
	private JPanel panel;

	/**
	 * Cursor object used to hide the cursor when active on our panel.
	 */
	private Cursor cursorNull;

	/**
	 * Map storing the frames of a cursor based on type.
	 */
	private HashMap<String, ArrayList<BufferedImage>> mapCursorImages;

	/**
	 * Current image used to render.
	 */
	private BufferedImage currentCursor;

	/**
	 * The directory the cursor data is in.
	 */
	private String cursorDirectory = "assets/Cursor/";

	/**
	 * The current frame in the animation of the cursor.
	 */
	private int cursorFrame = 0;

	/**
	 * The maximum amount of frames.
	 */
	private int cursorFrames = 0;

	/**
	 * Time kept to delay moving frames in the sprite.
	 */
	private long previousTickTime = 0L;

	/**
	 * The name of the current cursor being used.
	 */
	private String currentCursorName;

	/**
	 * The current task, used as a flag for cursor operation control purposes.
	 */
	private String currentTask = "default";

	private boolean changed;

	/**
	 * Main constructor.
	 * 
	 * @param component
	 */
	public CursorController(JPanel panel) {
		// Sets the component using this controller.
		setPanel(panel);
		// Initializes our cursors.
		initializeCursors();
	}

	/**
	 * Initializes & loads our cursor data.
	 */
	private void initializeCursors() {
		// Declares our hashmaps.
		mapCursorImages = new HashMap<>();
		mapOffsets = new HashMap<>();

		// Installs our default cursor.
		File[] directoryEntries = new File(cursorDirectory).listFiles();

		// Go through the directories in the main Cursor directory.
		for (File file : directoryEntries) {
			// If the entry is a directory.
			if (file.isDirectory()) {

				// Initialize the set of cursors.
				initializeCursorSet(file);
			}
		}

		/*
		 * Initializes the empty cursor used to cloak the real one, using our
		 * cursors drawn as a graphical entity.
		 */
		initializeNullCursor();
		this.setCurrentTask("default");
		this.setCursorType("blue.default");

	}

	private void initializeCursorSet(File file) {

		// Grab the name of the directory.
		String directoryName = file.getName().toLowerCase();
		// Grab all sub-directories. Any directory is considered one cursor.
		File[] subEntries = file.listFiles();

		// Iterate through the list to process cursors.
		for (File subEntry : subEntries) {

			// Any non-directory entries are ignored.
			if (subEntry.isDirectory()) {

				// Grab the name of the directory for the cursor's name.
				String cursorName = subEntry.getName().toLowerCase();

				// Install the cursor.
				installCursor(directoryName, cursorName);
			}
		}
	}

	/**
	 * Initializes the cursor used to hide the real cursor to draw a cursor in
	 * it's place.
	 */
	private void initializeNullCursor() {
		// Our default point for a cursor.
		Point hotSpot = new Point(0, 0);

		// Our BufferedImage object carrying the empty image.
		BufferedImage image = null;

		try {
			// Read the empty image.
			image = ImageIO.read(new File(cursorDirectory + "null.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Install the cursor and store it as a field to call to when needed.
		cursorNull = Toolkit.getDefaultToolkit().createCustomCursor(image,
				hotSpot, "null");
	}

	/**
	 * Installs a cursor.
	 * 
	 * @param hotSpot
	 * @param folderName
	 * @param type
	 * @param point
	 */
	private void installCursor(String folderName, String cursorName) {

		// Our storage list of each image used for the cursor.
		ArrayList<BufferedImage> listImages;

		// Our point for the offset of the cursor to properly point the cursor.
		int[] point = new int[] { 0, 0, 0, 0 };

		// File holder.
		File[] cursorFiles = null;

		// The identity of the cursor in the result hashmap.
		String storeName = folderName + "." + cursorName;

		// The folder of the cursor.
		File cursorFolder = new File(cursorDirectory + folderName + "/"
				+ cursorName + "/");
		System.out.println("cursorFolder: " + cursorFolder);
		cursorFiles = cursorFolder.listFiles();

		listImages = new ArrayList<BufferedImage>();

		for (File file : cursorFiles) {
			if (file.getName().toLowerCase().equals("config.txt")) {
				try {
					point = loadConfig(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
				if (image != null) {
					listImages.add(image);
				}
			} catch (IOException e) {
				continue;
			}
		}
		System.out.println(storeName);
		mapCursorImages.put(storeName, listImages);
		mapOffsets.put(storeName, point);
	}

	public int[] loadConfig(File file) throws IOException {
		int x = 0;
		int y = 0;
		int ix = 0;
		int iy = 0;

		BufferedReader reader;
		reader = new BufferedReader(new FileReader(file));

		String line;
		while ((line = reader.readLine()) != null) {
			String[] splitSetting = line.split(":");
			if (splitSetting[0].equals("offset")) {
				String[] splitSetting2 = splitSetting[1].split(",");
				x = Integer.parseInt(splitSetting2[0]);
				y = Integer.parseInt(splitSetting2[1]);
			} else if (splitSetting[0].equals("icon_offset")) {
				String[] splitSetting2 = splitSetting[1].split(",");
				ix = Integer.parseInt(splitSetting2[0]);
				iy = Integer.parseInt(splitSetting2[1]);
			}
		}

		reader.close();

		return new int[] { x, y, ix, iy };
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Returns the component that is set.
	 * 
	 * @return
	 */
	public JPanel getPanel() {
		return this.panel;
	}

	/**
	 * Updates and transitions the cursor.
	 */
	public void update() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - previousTickTime > 100L) {
			if (cursorFrames > 1) {
				if (cursorFrame < cursorFrames - 1) {
					cursorFrame++;
				} else {
					cursorFrame = 0;
				}
			} else {
				cursorFrame = 0;
			}
			ArrayList<BufferedImage> images;
			if (currentImages == null) {
				System.out.println("currentCursor is null.");
			}
			images = currentImages;
			currentCursor = images.get(cursorFrame);

			previousTickTime = currentTime;
		}
	}

	public void draw(Graphics2D g2d, int x, int y) {
		int cursorX = x + currentOffset[0];
		int cursorY = y + currentOffset[1];
		g2d.drawImage(currentCursor, cursorX, cursorY, null);
	}

	public String getCursorType() {
		return currentCursorName;
	}

	public BufferedImage getCursor(String key) {
		return mapCursorImages.get(key).get(0);
	}

	public void setCursorType(String cursorName) {
		this.currentCursorName = cursorName;
		ArrayList<BufferedImage> images = mapCursorImages.get(cursorName);
		currentOffset = mapOffsets.get(cursorName);
		currentImages = images;
		setFrameCount(images.size());
		setFrame(0);
	}

	public void setPanelCursorNull(boolean b) {
		if (b) {
			getPanel().setCursor(cursorNull);
		} else {
			getPanel().setCursor(null);
		}
	}

	private void setFrame(int cursorFrame) {
		this.cursorFrame = cursorFrame;
	}

	private void setFrameCount(int cursorFrames) {
		this.cursorFrames = cursorFrames;
	}

	public int[] getCursorOffset(String key) {
		return mapOffsets.get(key);
	}

	public String getCurrentTask() {
		return this.currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public void setChanged(boolean b) {
		this.changed = b;
	}
}
