package com.fourtyblocksunder.abstention.editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.fourtyblocksunder.abstention.editor.graph.GraphCamera;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraph;
import com.fourtyblocksunder.abstention.editor.graph.NodeGraphMain;
import com.fourtyblocksunder.abstention.util.CursorController;
import com.fourtyblocksunder.abstention.util.RenderNodeUtil;

public class MainPanel extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener {

	private CursorController cursorController;

	private float graphTransition = .5F;

	private int[] mousePressed;

	private int[] mouseDelta = new int[] { 0, 0 };

	private int[] mouseReleased = new int[] { 0, 0 };

	private int[] mousePosition = new int[] { 0, 0 };

	private int[] locationNewNode = null;

	private long previousClick = 0L;

	private MainFrame mainFrame = null;

	private NodeGraph nodeGraphActive = null;

	private NodeGraph nodeGraphLast = null;
	
	boolean imageProcessed = true;

	private Object lock = new Object();

	private BufferedImage imageTransition;

	private static final long serialVersionUID = 20141037099563327L;

	public MainPanel(MainFrame mainFrame) {
		setMainFrame(mainFrame);
		initialize();
		setBackground(Color.black);
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	private void initialize() {
		mousePressed = new int[] { 0, 0 };
		mouseDelta = new int[] { 0, 0 };
		mouseReleased = new int[] { 0, 0 };
		cursorController = new CursorController(this);
		cursorController.setCursorType("blue.default");
		setNodeGraph(new NodeGraphMain(this));
	}

	public void updatePanel() {
		synchronized (lock) {

			float transition = getModeTransition();
			if (transition < 0.5F) {
				transition += .01F;
				if (transition >= 0.5F) {
					transition = 0.5F;
				}
				setModeTransition(transition);
			}
			getNodeGraph().update();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		if (g == null) {
			return;
		}
		synchronized (lock) {
			Graphics2D g2d = (Graphics2D) g;
			renderMainPanel(g2d);
			g.dispose();
		}

	}

	private void renderMainPanel(Graphics2D graphics2D) {
		Graphics2D g2d = null;

		float transition = getModeTransition();
		if (!imageProcessed && getNodeGraphLast() != null) {
			imageTransition = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			g2d = (Graphics2D) imageTransition.getGraphics();
			getNodeGraphLast().render(g2d);
			g2d.dispose();
		//	try {
		//		ImageIO.write(imageTransition, "PNG", new File("graph.png"));
		//	} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
			imageProcessed = true;
		}
		
		if (transition < 0.5F) {
			float opacity = 0.5f;
			graphics2D.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, (opacity - transition) * 2));

			graphics2D.drawImage(imageTransition, 0, 0, null);

			graphics2D.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, transition * 2));

			getNodeGraph().render(graphics2D);
		}
		else
		{	
			graphics2D.setComposite(AlphaComposite
					.getInstance(AlphaComposite.SRC_OVER, 1F));
			
			getNodeGraph().render(graphics2D);
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();
			graph.handleKeyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();
			graph.handleKeyReleased(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		synchronized (lock) {

		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();
			graph.handleMouseWheelMoved(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		synchronized (lock) {
			mousePosition = getMousePositionOnScreen(e, this);
			NodeGraph graph = getNodeGraph();

			GraphCamera camera = graph.getGraphCamera();

			float scale = camera.getScale();

			int[] screenPosition = RenderNodeUtil.pointToInts(e
					.getLocationOnScreen());

			int[] screenLocation = RenderNodeUtil
					.pointToInts(getLocationOnScreen());

			int[] mousePositionLocal = new int[] {
					screenPosition[0] - screenLocation[0],
					screenPosition[1] - screenLocation[1] };

			// TODO divide scale inside the movement operation of a node.
			// Took it out here. mouseDelta.x / scale; mouseDelta.y / scale;
			mouseDelta = new int[] {
					(int) ((mousePositionLocal[0] - mousePressed[0]) / scale),
					(int) ((mousePositionLocal[1] - mousePressed[1]) / scale) };

			graph.handleMouseDragged(mousePressed, mouseDelta, e);

		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();
			mousePosition = getMousePositionOnScreen(e, this);
			graph.handleMouseMoved(mousePosition, e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		synchronized (lock) {

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		synchronized (lock) {
			cursorController.setPanelCursorNull(true);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		synchronized (lock) {
			cursorController.setPanelCursorNull(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();

			long currentClick = System.currentTimeMillis();

			mouseDelta = new int[] { 0, 0 };
			mouseReleased = new int[] { 0, 0 };
			mousePressed = getMousePositionOnScreen(e, this);

			if (currentClick - previousClick < 300L) {
				graph.handleDoubleClick(mousePressed, e);
				previousClick = 0L;
			} else {
				previousClick = currentClick;
			}
			getNodeGraph().handleMousePressed(mousePressed, e);
			// pressedInMenu = menu.handleMousePressed(mousePressed, e);
			// if (!pressedInMenu) {
			// camera.handleMousePressed(mousePressed, e);
			// for (Node node : getNodes()) {
			// node.handleMousePressed(mousePressed, e, getCamera(), getKeys());
			// }
			// }
		}
	}

	public static int[] getMousePositionOnScreen(MouseEvent e, JPanel panel) {
		int[] mousePositionOnScreen = RenderNodeUtil.pointToInts(e
				.getLocationOnScreen());
		int[] panelLocation = RenderNodeUtil.pointToInts(panel
				.getLocationOnScreen());

		return new int[] { (int) (mousePositionOnScreen[0] - panelLocation[0]),
				(int) (mousePositionOnScreen[1] - panelLocation[1]) };
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		synchronized (lock) {
			NodeGraph graph = getNodeGraph();
			mouseReleased = getMousePositionOnScreen(e, this);
			graph.handleMouseReleased(mousePressed, mouseDelta, mouseReleased,
					e);
		}
	}

	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	public float getModeTransition() {
		return this.graphTransition;
	}

	public void setModeTransition(float graphTransition) {
		this.graphTransition = graphTransition;
	}

	public NodeGraph getNodeGraph() {
		return this.nodeGraphActive;
	}

	public void setNodeGraph(NodeGraph nodeGraph) {
		imageProcessed = false;
		setModeTransition(0F);
		this.nodeGraphLast = this.nodeGraphActive;
		this.nodeGraphActive = nodeGraph;
	}

	public NodeGraph getNodeGraphLast() {
		return this.nodeGraphLast;
	}

	private void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public MainFrame getMainFrame() {
		return this.mainFrame;
	}

	private void setImageTransition(BufferedImage imageTransition) {
		this.imageTransition = imageTransition;
	}

	public BufferedImage getImageTransition() {
		return this.imageTransition;
	}

	public int[] getMouseLocation() {
		// TODO Auto-generated method stub
		return this.mousePosition;
	}

}
