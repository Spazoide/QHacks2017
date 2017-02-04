
/******************************************************************************\
* Copyright (C) 2012-2016 Leap Motion, Inc. All rights reserved.               *
* Leap Motion proprietary and confidential. Not for distribution.              *
* Use subject to the terms of the Leap Motion SDK Agreement available at       *
* https://developer.leapmotion.com/sdk_agreement, or another agreement         *
* between Leap Motion and you, your company or other organization.             *
\******************************************************************************/

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.lang.Math;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leapmotion.leap.*;

class MouseController extends Listener implements KeyListener {

	Robot robot;
	private int[] yRange = new int[2];
	private int[] xRange = new int[2];
	private boolean calibrationMode = true;
	private int caliState = 0;
	private Point[] corners = new Point[4];
	private String[] cornerNames = { "Top-Left", "Top-Right", "Bottom-Left", "Bottom-Right" };
	private Calibrator c;
	private boolean flag = false;
	
	private JFrame window;
	
	public MouseController(Calibrator c, JFrame window) {
		this.c = c;
		this.window = window;
	}

	public void onInit(Controller controller) {
		System.out.println("Initialized");
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
    	Finger finger = null;
    	Frame frame = controller.frame();
    	for(Hand hand: frame.hands()){
	    	for(Finger f: hand.fingers()){
	    		if(f.type()!=Finger.Type.TYPE_INDEX)
	    			continue;
	    		finger=f;
	    		break;
	    	}
    	}
    	
    	if(calibrationMode){
    		
    		if(flag){
    			corners[caliState] = new Point((int)finger.tipPosition().getX(), (int)finger.tipPosition().getY());
    			flag = false;
    			caliState++;
    			if(caliState > 3){
    				calibrationMode = false;
    			}
    		}
    		return;
    	}
        // Get the most recent frame and report some basic information
       robot.mouseMove(map(-165,175,0,1920,(int) finger.tipPosition().getX()), 1080-map(120,305,0,1080,(int) finger.tipPosition().getY()));
        	System.out.println(finger.tipPosition().toString());
            if(finger.tipPosition().getZ()<-50){
            	robot.mousePress(InputEvent.BUTTON1_MASK);
            	robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

        //Get hands
        
           
            
  
    }

	private int map(int rmin, int rmax, int vmin, int vmax, int value) {
		return (int) ((float) (value - rmin) / (rmax - rmin) * (vmax - vmin) + vmin);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}

class Sample {
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Cali");
		window.setSize(1920, 1080);
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);	
		//window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		
		
		Calibrator cali = new Calibrator();
		MouseController listener = new MouseController(cali , window);
		Controller controller = new Controller();

		
		

		window.addKeyListener(listener);
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
