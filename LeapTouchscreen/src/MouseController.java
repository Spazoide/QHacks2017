
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
	private Vector[] corners = new Vector[5];
	private boolean clicked = false;
	private Calibrator c;
	private boolean flag = false;
	private Plane screenPlane;
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
		for (Hand hand : frame.hands()) {
			for (Finger f : hand.fingers()) {
				if (f.type() != Finger.Type.TYPE_INDEX)
					continue;
				finger = f;
				break;
			}
		}

		if (calibrationMode) {

			if (flag) {
				corners[caliState] = finger.tipPosition();
				flag = false;
				caliState++;
				if (caliState > 4) {
					calcBounds();
					calibrationMode = false;
					window.dispose();
				}else if(caliState > 3){
					int[][] cornerCoords = { { 0, 0 }, { window.getWidth(), 0 }, { 0, window.getHeight() },
							{ window.getWidth(), window.getHeight() },
							{ window.getWidth() / 2, window.getHeight() / 2 } };
					c.setCirclePos(cornerCoords[caliState][0], cornerCoords[caliState][1]);
					c.text = true;
					c.repaint();

				} else {
					int[][] cornerCoords = { { 0, 0 }, { window.getWidth(), 0 }, { 0, window.getHeight() },
							{ window.getWidth(), window.getHeight() },
							{ window.getWidth() / 2, window.getHeight() / 2 } };
					c.setCirclePos(cornerCoords[caliState][0], cornerCoords[caliState][1]);
					c.repaint();
				}
				
			}
			return;
		}
		// Get the most recent frame and report some basic information
//		 robot.mouseMove(map(xRange[0],xRange[1],0,window.getWidth(),(int)
//		 finger.tipPosition().getX()),
//		 map(yRange[0],yRange[1],0,window.getHeight(),(int)
//		 finger.tipPosition().getY()));
		int[] pos = screenPlane.getPOIScaled(finger.tipPosition(), finger.direction(), window.getWidth(), window.getHeight());
		robot.mouseMove(pos[0], pos[1]);
		
//		if (finger.tipPosition().getZ() < zClick && !clicked) {
//			robot.mousePress(InputEvent.BUTTON1_MASK);
//			System.out.println("Click");
//		}
//		if (clicked && finger.tipPosition().getZ() > zClick + 5) {
//			robot.mouseRelease(InputEvent.BUTTON1_MASK);
//			System.out.println("unclick");
//			clicked = false;
//		}

	}

	

	private void calcBounds() {
		xRange[0] = (int) (corners[0].getX() + corners[2].getX()) / 2;
		xRange[1] = (int) ((corners[1].getX() + corners[3].getX()) / 2);

		yRange[0] = (int) ((corners[0].getY() + corners[1].getY()) / 2);
		yRange[1] = (int) ((corners[2].getY() + corners[3].getY()) / 2);

		zClick = (int) ((corners[0].getZ() + corners[1].getZ() + corners[2].getZ() + corners[3].getZ()) / 4);

		screenPlane = new Plane(corners[0], corners[1], corners[2]);

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
