
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
	private Vector[][] corners = new Vector[5][2];
	private boolean clicked = false;
	private Calibrator c;
	private boolean flag = false;
	private Plane screenPlane;
	private Plane exitPlane;
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

	private void calcBounds() {
		zClick = (int) ((corners[0][0].getZ() + corners[1][0].getZ() + corners[2][0].getZ() + corners[3][0].getZ())
				/ 4);
		screenPlane = new Plane(corners);

	}

	private Finger[] getFingers(Frame frame) {
		Finger[] fingers;
		for (Finger f : frame.fingers()) {
			for (Finger t : frame.fingers()) {
				for (Finger p : frame.fingers()) {
					for (Finger m : frame.fingers()) {
						for (Finger r : frame.fingers()) {
							if (f.type() == Finger.Type.TYPE_INDEX) {

								if (t.type() == Finger.Type.TYPE_THUMB) {
									if (p.type() == Finger.Type.TYPE_PINKY) {
										if (m.type() == Finger.Type.TYPE_MIDDLE) {
											if (r.type() == Finger.Type.TYPE_RING) {
													
												fingers = new Finger[]{t,f,m,r,p};
												return fingers;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {

		Finger finger = null;
		// controller.enableGesture(arg0);
		Frame frame = controller.frame();
//		for (Hand hand : frame.hands()) {
//			for (Finger f : hand.fingers()) {
//				if (f.type() != Finger.Type.TYPE_INDEX)
//					continue;
//				finger = f;
//				break;
//			}
//		}
		
		if((finger = getFingers(frame)[1]) == null){
			return;
		}

		if (calibrationMode) {
			calibrationMode(finger);
			return;
		}

		// if current offset is bigger then calibrated offset exit the method to
		// halt finger tracking.
		Vector fingerPos = finger.stabilizedTipPosition();
		Vector fingerDir = finger.direction();
		if (screenPlane.getOffsetValue(fingerPos) > screenPlane.getOffsetValue(corners[4][0])) {
			return;
		}

		// The drag zone 420 blaze it

		// Get the most recent frame and report some basic information
		int[] pos = screenPlane.getPOIScaled(fingerPos, fingerDir, window.getWidth(), window.getHeight());
		robot.mouseMove(pos[0], pos[1]);

		float currentZ = screenPlane.getPOI(fingerPos, fingerDir).getZ();
		// if (fingerPos.getZ() <= currentZ) {
		// if(!clicked){
		// robot.mousePress(InputEvent.BUTTON1_MASK);
		// robot.mouseRelease(InputEvent.BUTTON1_MASK);
		// clicked = true;
		// }
		// }else{
		// clicked=false;
		// }
		if (fingerPos.getZ() - 5 <= currentZ && !clicked) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			clicked = true;

		}
		if (clicked && fingerPos.getZ() > currentZ) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			clicked = false;
		}

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
