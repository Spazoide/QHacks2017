
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.Math;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leapmotion.leap.*;

class MouseController extends Listener implements KeyListener, MouseListener {

	Robot robot;
	private int[] yRange = new int[2];
	private int[] xRange = new int[2];
	private int zClick;
	private boolean calibrationMode = true;
	private int caliState = 0;
	private Vector[] corners = new Vector[4];
	private boolean clicked = false;
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
    			corners[caliState] = finger.tipPosition();
    			flag = false;
    			caliState++;
    			if(caliState > 3){
    				calcBounds();
    				calibrationMode = false;
    				window.dispose();
    			}else{
    				int[][] cornerCoords = {{0,0},{window.getWidth(), 0}, {0, window.getHeight()}, {window.getWidth(), window.getHeight()}};
    				c.setCirclePos(cornerCoords[caliState][0], cornerCoords[caliState][1]);
    				c.repaint();
    			}
    		}
    		return;
    	}
        // Get the most recent frame and report some basic information
       robot.mouseMove(map(xRange[0],xRange[1],0,window.getWidth(),(int) finger.tipPosition().getX()), map(yRange[0],yRange[1],0,window.getHeight(),(int) finger.tipPosition().getY()));
            if(finger.tipPosition().getZ()<zClick && !clicked){
            	robot.mousePress(InputEvent.BUTTON1_MASK);
            	System.out.println("Click");
            }
            if(clicked && finger.tipPosition().getZ()>zClick+5){
            	robot.mouseRelease(InputEvent.BUTTON1_MASK);
            	System.out.println("unclick");
            	clicked = false;
            }
     
    }

	private int map(int rmin, int rmax, int vmin, int vmax, int value) {
		return (int) ((float) (value - rmin) / (rmax - rmin) * (vmax - vmin) + vmin);
	}
	
	private void calcBounds(){
		xRange[0] = (int) (corners[0].getX() + corners[2].getX()) /2;
		xRange[1] = (int) ((corners[1].getX()  + corners[3].getX() )/2);
		
		yRange[0] = (int) ((corners[0].getY() + corners[1].getY())/2);
		yRange[1] = (int) ((corners[2].getY()  + corners[3].getY())/2);
		
		zClick = (int) ((corners[0].getZ()+corners[1].getZ()+corners[2].getZ()+corners[3].getZ())/4);
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		flag = true;
		System.out.println("key");

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		flag=true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

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
		MouseController listener = new MouseController(cali , window);
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
