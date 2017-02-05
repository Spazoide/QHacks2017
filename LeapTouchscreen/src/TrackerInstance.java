import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.leapmotion.leap.Controller;

public class TrackerInstance implements Runnable, ActionListener {

	boolean stop = false;
	Controller controller = null;
	JFrame window = null;
	MouseController listener = null;
	
	@Override
	public void run() {

		window = new JFrame("Cali");
		window.setSize(1920, 1080);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setFocusable(true);
		window.requestFocus();
		
		final PopupMenu popup = new PopupMenu();
		TrayIcon trayIcon = null;
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File("icon.png")),"Leap Tracker");
		} catch (IOException e1) {

			e1.printStackTrace();
		}
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu component
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(this);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
		

		Calibrator cali = new Calibrator();
		listener = new MouseController(cali, window);
		controller = new Controller();
		controller.setPolicy(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
		window.addKeyListener(listener);
		window.addMouseListener(listener);
		window.add(cali);
		window.setVisible(true);

		// Create a sample listener and controller

		// Have the sample listener receive events from the controller
		controller.addListener(listener);
		
		// Keep this process running until Enter is pressed
		while(!stop){
			
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.removeListener(listener);
		window.dispose();
		window=null;
		System.exit(0);
		
	}

}
