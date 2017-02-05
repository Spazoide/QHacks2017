import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.leapmotion.leap.Controller;

public class TrackerInstance implements Runnable, ActionListener {

	boolean stop = false;
	@Override
	public void run() {

		JFrame window = new JFrame("Cali");
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
		MouseController listener = new MouseController(cali, window);
		Controller controller = new Controller();
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
	
		// Remove the sample listener when done
		controller.removeListener(listener);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("F");
		stop=true;
		
	}

}