import java.awt.Color;
import java.awt.Graphics;

public class Explode {

	int x, y;
	int[] diameter = { 5, 10, 16, 23, 31, 40, 27, 13, 4 };
	private TankClient tc;
	private boolean live = true;
	int step = 0;

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.explodes.remove(this);
			return;
		}
		if (step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.cyan);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}

}
