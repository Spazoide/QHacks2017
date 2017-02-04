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
	private int caliState = 3;
	private Point[] corners = new Point[4];
	private String[] cornerNames = {"Top-Left","Top-Right","Bottom-Left","Bottom-Right"};
	private Calibrator c;
	
	public MouseController(Calibrator c){
		this.c=c;
	}
	
    public void onInit(Controller controller) {
        System.out.println("Initialized");
        try {
			robot=new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    private void calibration(){
    	
    }
    
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

        //Get hands
        Hand hand = frame.hand(0);
    	for(Finger finger: hand.fingers()){
    		if(finger.type()!=Finger.Type.TYPE_INDEX)
    			continue;
    		robot.mouseMove(map(-165,175,0,1920,(int) finger.tipPosition().getX()), 1080-map(120,305,0,1080,(int) finger.tipPosition().getY()));
        	System.out.println(finger.tipPosition().toString());
            if(finger.tipPosition().getZ()<-50){
            	robot.mousePress(InputEvent.BUTTON1_MASK);
            	robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }

    	}
           
            
  
    }
    
    private int map(int rmin, int rmax,int vmin, int vmax, int value){
    	return (int)((float)(value-rmin)/(rmax-rmin)*(vmax-vmin)+vmin);
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
    	
    	Calibrator cali = new Calibrator();
    	MouseController listener = new MouseController(cali);
        Controller controller = new Controller();
    	
    	JFrame window = new JFrame("Cali");
    	window.setSize(1920, 1080);
    	window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	window.setVisible(true);
    	
    	window.addKeyListener(listener);
    	
    	
    	window.add(cali);
    	
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
