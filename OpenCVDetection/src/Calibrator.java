import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class Calibrator extends JComponent{

	private int x = 0;
	private int y = 0;
	public boolean text = false;
	
	public void setCirclePos(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().width);
		g.setColor(Color.red);
		g.fillOval(x-50, y-50, 100, 100);
		
		if(text){
			g.setColor(Color.WHITE);
			g.drawString("Hover finger over screen at comfortable distance and press any key.", x, y-100);
		}
		
	}
	
	

}
