import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class Calibrator extends JComponent{

	private int x = 0;
	private int y = 0;
	
	public void setCirclePos(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.fillOval(x, y, 100, 100);
		
	}


	
	

}
