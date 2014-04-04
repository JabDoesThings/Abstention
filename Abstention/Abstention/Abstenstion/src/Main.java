import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JWindow;

public class Main {
  public static void main(String[] argv) throws Exception {
    JWindow window = new JWindow();

    JButton component = new JButton("asdf");
    // Add component to the window
    window.getContentPane().add(component, BorderLayout.CENTER);

    // Set initial size
    window.setSize(300, 300);

    // Show the window
    window.setVisible(true);

  }
}