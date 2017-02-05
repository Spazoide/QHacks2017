
/******************************************************************************\
* Copyright (C) 2012-2016 Leap Motion, Inc. All rights reserved.               *
* Leap Motion proprietary and confidential. Not for distribution.              *
* Use subject to the terms of the Leap Motion SDK Agreement available at       *
* https://developer.leapmotion.com/sdk_agreement, or another agreement         *
* between Leap Motion and you, your company or other organization.             *
\******************************************************************************/

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

class MouseController extends Listener implements KeyListener, MouseListener {

	Robot robot;
	
	private boolean calibrationMode = true;
	private int caliState = 0;
	private Vector[][] corners = new Vector[5][2];
	private boolean clicked = false;
	private boolean dragged = false;
	private boolean rightClicked = false;
	private Calibrator c;
	private boolean flag = false;
	private Plane screenPlane;
	
	private int offsetConst;
	
	private JFrame window;
	
	private float zOffset;

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
	
	public void onServiceConnect(Controller controller) {
		System.out.println("Service Connected");
	}
	
	public void onServiceDisconnect(Controller controller) {
		System.out.println("Service Disconnected");
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	private void calcBounds() {
		
		screenPlane = new Plane(corners);
		zOffset = screenPlane.getOffsetValue(corners[4][0]);
		//offsetConst = (int)((screenPlane.getOffsetValue(corners[0][0]) + screenPlane.getOffsetValue(corners[1][0]) + screenPlane.getOffsetValue(corners[2][0]) + screenPlane.getOffsetValue(corners[3][0]))/4);

	}

	private Finger[] getFingers(Frame frame) {
		if(frame.fingers().isEmpty()){
			return null;
		}
		Finger[] fingers = new Finger[5];
		for (Finger f : frame.fingers()) {
			fingers[f.type().ordinal()] = f;
		}
		return fingers;
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}
	
	
	public void onFrame(Controller controller) {

		Finger finger = null;
	
		Frame frame = controller.frame();

		Finger[] allFingers = getFingers(frame);
		if(allFingers == null){
			return;
		}
		finger = allFingers[1];

		if (calibrationMode) {
			calibrationMode(finger);
			return;
		}

		// if current offset is bigger then calibrated offset exit the method to
		// halt finger tracking.
		Vector fingerPos = finger.stabilizedTipPosition();
		Vector fingerDir = finger.direction();
		
		float screenOffset = screenPlane.getOffsetValue(fingerPos);
		if (screenOffset > zOffset) {
			return;
		}

		// The drag zone 420 blaze it

		// Get the most recent frame and report some basic information
		int[] pos = screenPlane.getPOIScaled(fingerPos, fingerDir, window.getWidth(), window.getHeight());
		System.out.printf("x:%d \t y:%d \n", pos[0], pos[1]);
		robot.mouseMove(pos[0], pos[1]);

		//System.out.printf("%s %s %s %s %s\n",Boolean.toString(allFingers[0].isExtended()), Boolean.toString(allFingers[1].isExtended()), Boolean.toString(allFingers[2].isExtended()), Boolean.toString(allFingers[3].isExtended()), Boolean.toString(allFingers[4].isExtended()));
		//Recognizing gestures
		if(allFingers[1].isExtended() && allFingers[2].isExtended() && allFingers[3].isExtended()){ //Scrolling
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			actionScroll(allFingers[2], screenOffset);
			//System.out.println("scrolling");
		}else if(allFingers[1].isExtended() && allFingers[2].isExtended()){
			actionDrag(allFingers[1], screenOffset);
			//System.out.println("dragging");
		}else if(allFingers[1].isExtended()){
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			actionClick(finger, screenOffset);
			//System.out.println("clicking");
		}else if(allFingers[0].isExtended()){
			actionRightClick(allFingers[0], screenOffset);
		}
		
		
	}
	
	private boolean isClickRegistered(float offset){
		System.out.println(offset);		
		return offset <= 8;
		
	
	}

	private void calibrationMode(Finger finger) {

		if (flag) {
			corners[caliState][0] = finger.tipPosition();
			corners[caliState][1] = finger.direction();
			flag = false;
			caliState++;
			if (caliState > 4) {
				calcBounds();
				calibrationMode = false;
				window.dispose();
			} else if (caliState > 3) {
				int[][] cornerCoords = { { 0, 0 }, { window.getWidth(), 0 }, { 0, window.getHeight() },
						{ window.getWidth(), window.getHeight() }, { window.getWidth() / 2, window.getHeight() / 2 } };
				c.setCirclePos(cornerCoords[caliState][0], cornerCoords[caliState][1]);
				c.text = true;
				c.repaint();

			} else {
				int[][] cornerCoords = { { 0, 0 }, { window.getWidth(), 0 }, { 0, window.getHeight() },
						{ window.getWidth(), window.getHeight() }, { window.getWidth() / 2, window.getHeight() / 2 } };
				c.setCirclePos(cornerCoords[caliState][0], cornerCoords[caliState][1]);
				c.repaint();
			}

		}

	}
	
	public void actionDrag(Finger f, float offset){
		boolean isRegistered = isClickRegistered(offset);
		
		if (isRegistered && !dragged) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			dragged = true;

		}
		if (dragged && !isRegistered) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			dragged = false;
		}
				
	}
	
	public void actionClick(Finger f, float offset) {
		
		if (isClickRegistered(offset)) {
			if (!clicked) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				clicked = true;
			}
		} else {
			clicked = false;
		}
	}
	
public void actionRightClick(Finger f, float offset) {
		
		if (isClickRegistered(offset)) {
			if (!rightClicked) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
				rightClicked = true;
			}
		} else {
			rightClicked = false;
		}
	}
	
	public void actionScroll(Finger f , float offset){
		
		float fingerVely = f.tipVelocity().getY()/200;
				
		if(isClickRegistered(offset)){
			robot.mouseWheel((int)fingerVely);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		flag = true;

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
		flag = true;
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
