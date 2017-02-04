import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class Calibrator extends JComponent{

	private int x,y;
	
	public void setCirclePos(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.fillOval(0, 0, 100, 100);
		
	}


	
	

}
