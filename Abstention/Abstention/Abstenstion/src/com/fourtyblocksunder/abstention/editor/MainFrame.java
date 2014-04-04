package com.fourtyblocksunder.abstention.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.fourtyblocksunder.abstention.editor.graph.node.Node;
import com.fourtyblocksunder.abstention.editor.project.Project;
import com.fourtyblocksunder.abstention.editor.project.ProjectUtil;
import com.fourtyblocksunder.abstention.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class designed to hold the main window's functions, as well as data &
 * settings.
 * 
 * @author Joshua Edwards
 * 
 */
public class MainFrame extends JFrame implements WindowListener {

	/**
	 * Serial ID to go with JFrame.
	 */
	private static final long serialVersionUID = 5917860052864201930L;

	/**
	 * Editor instance using this frame.
	 */
	private Editor editor;

	private Project project;

	private MainPanel mainPanel;

	/**
	 * Main menu for the node editor.
	 */
	private JMenuBar menuBar;

	private Updater updater;

	private boolean minimized = false;

	private MainFrame mainFrame;

	/**
	 * Main constructor.
	 * 
	 * @param editor
	 */
	public MainFrame(Editor editor) {
		// Sets the editor instance.
		setEditor(editor);
		// Initializes objects & settings.
		initialize();
		mainFrame = this;
	}

	/**
	 * Initializes the Frame's objects & settings.
	 */
	private void initialize() {

		project = ProjectUtil.newProject("Untitled", this);
		// Sets the size of the window.
		setSize(800, 600);
		
		setBackground(Color.black);
		// Sets the title of the window.
		setTitle("Abstention Editor " + editor.getVersion());
		// When this window is closed, the program terminates.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Initializes the components used by this frame.
		initializeComponents();

		addWindowListener(this);
		
		for(Node node : project.getNodes())
		{
			node.setMainPanel(mainPanel);
		}

		updater = new Updater();
		updater.start();
		// Shows the frame.
		setVisible(true);
	}

	/**
	 * Initializes the components used for this frame.
	 */
	private void initializeComponents() {
		initializeGson();
		// Declares our GraphPanel instance, and adds it to the frame.
		mainPanel = new MainPanel(this);
		add(mainPanel);

		// Initializes our menu options.
		initializeMenuBar();
	}

	private void initializeGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		Gson gson = gsonBuilder.create();
		GsonUtil.setGson(gson);
	}

	/**
	 * Initializes the MenuBar of the JFrame.
	 */
	private void initializeMenuBar() {
		menuBar = new JMenuBar();

		menuBar.setBorderPainted(true);

		JMenu menuFile = new JMenu("File");

		JMenuItem menuItemNew = new JMenuItem("New Project");
		menuItemNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("new");
				String projectName = "";

				boolean attempt = false;
				while (projectName.isEmpty()) {
					String message = "Project Name? ";
					if (attempt) {
						message += "(Please enter a valid name.)";
					}
					projectName = JOptionPane.showInputDialog(message);

					attempt = true;
				}
				setProject(ProjectUtil.newProject(projectName, mainFrame));
			}

		});
		menuFile.add(menuItemNew);

		JMenuItem menuItemOpen = new JMenuItem("Open Project");
		menuFile.add(menuItemOpen);

		JMenuItem menuItemSave = new JMenuItem("Save Project");
		menuItemOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
				  //File file = fileChooser.getSelectedFile();
				  // save to file
				}
			}
		}
		);
		menuFile.add(menuItemSave);

		JMenuItem menuItemSaveAs = new JMenuItem("Save Project As...");
		menuFile.add(menuItemSaveAs);

		menuFile.addSeparator();

		JMenu menuImport = new JMenu("Import");
		JMenuItem menuImportFromProject = new JMenuItem("From Project");
		menuImport.add(menuImportFromProject);
		JMenuItem menuImportResource = new JMenuItem("Resource");
		menuImport.add(menuImportResource);
		JMenuItem menuImportMultipleResources = new JMenuItem("Recource Folder");
		menuImport.add(menuImportMultipleResources);
		menuFile.add(menuImport);

		JMenu menuExport = new JMenu("Export");
		JMenuItem menuExportGame = new JMenuItem("Compile to Game");
		menuExport.add(menuExportGame);
		menuFile.add(menuExport);

		menuFile.addSeparator();

		JMenuItem menuSettings = new JMenuItem("Settings");
		menuFile.add(menuSettings);

		menuFile.addSeparator();
		JMenuItem menuExit = new JMenuItem("Exit");
		menuFile.add(menuExit);
		menuBar.add(menuFile);

		JMenu menuEdit = new JMenu("Edit");
		// TODO: Edit options.
		menuBar.add(menuEdit);

		JMenu menuWindow = new JMenu("Window");
		JCheckBox menuWindowNodeEditor = new JCheckBox("NodeEditor");
		menuWindow.add(menuWindowNodeEditor);
		// TODO: Window options.
		menuBar.add(menuWindow);

		JMenu menuHelp = new JMenu("Help");
		JMenuItem menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
		// TODO: Help options.
		menuBar.add(menuHelp);

		setJMenuBar(menuBar);
	}

	/**
	 * Sets the editor using this frame.
	 * 
	 * @param editor
	 */
	private void setEditor(Editor editor) {
		this.editor = editor;
	}

	/**
	 * Draws the entire frame.
	 */
	private void draw() {
		repaint();
	}

	public void setProject(Project newProject) {
		this.project = newProject;
	}

	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		getMainPanel().repaint();
	}

	/**
	 * Updates all elements in the frame.
	 */
	private void update() {
		getMainPanel().updatePanel();
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public Editor getEditor() {
		return this.editor;
	}

	public Project getProject() {
		return this.project;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		setMinimized(false);
	}

	/**
	 * Sets the window's flag for whether or not it is optimized.
	 * 
	 * @param minimized
	 */
	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	/**
	 * Returns whether or not the window is minimized.
	 * 
	 * @return
	 */
	public boolean isMinimized() {
		return this.minimized;
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		setMinimized(true);
	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}

	/**
	 * Updating thread class designed to update the JFrame.
	 * 
	 * @author Joshua Edwards
	 * 
	 */
	private class Updater extends Thread {

		@Override
		public void run() {

			long gameTime;
			long lastLoopTime = System.nanoTime();
			final int TARGET_FPS = 60;
			final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
			long lastFpsTime = 0;
			while (true) {
				long now = System.nanoTime();
				long updateLength = now - lastLoopTime;
				lastLoopTime = now;
				double delta = updateLength / ((double) OPTIMAL_TIME);

				if (delta >= 0) {
					lastFpsTime += updateLength;
					if (lastFpsTime >= 1000000000) {
						lastFpsTime = 0;
					}
					if (!isMinimized()) {
						update();

						draw();
					}

					try {
						gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
						Thread.sleep(gameTime);
					} catch (Exception e) {
					}
				}
			}
		}
	}

}
