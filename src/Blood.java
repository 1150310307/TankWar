import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {
	int x,y,w,h;
	
	private int step = 0;
	private int[] position = {300,340,380,420,460,420,380,340};
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public Blood(){
		x = position[0];
		y = 300;
		w = 15;
		h = 15;
	}
	public void draw(Graphics g){
		if(!live)return;
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}
	private void move(){
		step++;
		if(step==position.length){
			step = 0;
		}
		x = position[step];
	}
	public Rectangle getRact() {
		return new Rectangle(x, y, w, h);
	}
}
