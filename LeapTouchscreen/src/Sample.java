import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.leapmotion.leap.Controller;

class Sample {
	public static void main(String[] args) {

		JFrame window = new JFrame("Cali");
		window.setSize(1920, 1080);
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setFocusable(true);
		window.requestFocus();

		Calibrator cali = new Calibrator();
		MouseController listener = new MouseController(cali, window);
		Controller controller = new Controller();

		window.addKeyListener(listener);
		window.addMouseListener(listener);
		window.add(cali);
		window.setVisible(true);

		// Create a sample listener and controller

		// Have the sample listener receive events from the controller
		controller.addListener(listener);

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(listener);
	}
}
