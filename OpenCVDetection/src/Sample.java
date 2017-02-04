/******************************************************************************\
* Copyright (C) 2012-2016 Leap Motion, Inc. All rights reserved.               *
* Leap Motion proprietary and confidential. Not for distribution.              *
* Use subject to the terms of the Leap Motion SDK Agreement available at       *
* https://developer.leapmotion.com/sdk_agreement, or another agreement         *
* between Leap Motion and you, your company or other organization.             *
\******************************************************************************/

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;

class SampleListener extends Listener {

	Robot a;
	
    public void onInit(Controller controller) {
        System.out.println("Initialized");
        try {
			a=new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(map(0, 50, 50, 100, 25));
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

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

        //Get hands
        for(Hand hand : frame.hands()) {
//            String handType = hand.isLeft() ? "Left hand" : "Right hand";
//            System.out.println("  " + handType + ", id: " + hand.id()
//                             + ", palm position: " + hand.palmPosition());
//
//            // Get the hand's normal vector and direction
//            Vector normal = hand.palmNormal();
//            Vector direction = hand.direction();
//
//            // Calculate the hand's pitch, roll, and yaw angles
//            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
//                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
//                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
//
//            // Get arm bone
//            Arm arm = hand.arm();
//            System.out.println("  Arm direction: " + arm.direction()
//                             + ", wrist position: " + arm.wristPosition()
//                             + ", elbow position: " + arm.elbowPosition());

            // Get fingers
        	
        	for(Finger finger: hand.fingers()){
        		if(finger.type()!=Finger.Type.TYPE_INDEX)
        			continue;
        		a.mouseMove(map(-175,175,0,1920,(int) finger.tipPosition().getX()), 1080-map(120,305,0,1080,(int) finger.tipPosition().getY()));
	        	System.out.println(finger.tipPosition().toString());
	            System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

        	}
           
            }
        

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
    }
    
    private int map(int rmin, int rmax,int vmin, int vmax, int value){
    	return (int)((float)(value-rmin)/(rmax-rmin)*(vmax-vmin)+vmin);
    }
}

class Sample {
    public static void main(String[] args) {
    	System.out.println(Finger.Type.TYPE_INDEX.ordinal());
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

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
