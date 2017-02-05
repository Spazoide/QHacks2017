import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class Calibrator extends JComponent{

	private int x = 0;
	private int y = 0;
	List<Pulse> pulses = new ArrayList<>();
	private int counter = 0;
	private String[] prompts = {"Top-Left","Top-Right","Bottom-Left","Bottom-Right"};
	public void setCirclePos(int x, int y){
//		pulses.add(new Pulse(x, y, 200, 5, Color.black, Color.WHITE));
		this.x=x;
		this.y=y;
		counter++;
	}
	
		
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 25));
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().width);
		g.setColor(Color.gray);
		g.fillOval(x-50, y-50, 100, 100);
//		for(Pulse p:pulses){
//			//System.out.println(p.isDead());
//			p.draw(g2);
//			p.grow();
//		}
		g.setColor(Color.WHITE);
		
		String str ="";
		if(counter<4){
			str=String.format("Please place your index finger in the %s corner of the screen and press any key.", prompts[counter]);
		}else{
			str="Hover finger over screen at comfortable distance and press any key.";
		}
		
		int textWidth = g.getFontMetrics(g.getFont()).stringWidth(str);
		g.drawString(str, (int)(g.getClipBounds().getWidth()/2-textWidth/2), (int)(g.getClipBounds().getHeight()/2-60));
		
		
	}
	
	

}
