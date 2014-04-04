import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class LWJGLTest {

	public static void main(String[] args) {
		try {
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		glViewport(0, 0, 800, 600);
		glMatrixMode(GL_PROJECTION);
		glOrtho(-2.0f, 2.0f, -2.0, 2.0, 0.0f, 60.0f);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		while (true) {
			if (Display.isCloseRequested()) {
				Display.destroy();
				System.exit(0);
			}
			Display.update();
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
