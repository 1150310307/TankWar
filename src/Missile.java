import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {

	int x, y;
	Direction dir;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	public static final int XSPEED = 10;
	public static final int YSPEED = 10;

	private boolean live = true;
	private boolean good;
	private TankClient tc;

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	public boolean isLive() {
		return live;
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}

	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case DR:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		if (x < 0 || y < 0 || x > TankClient.W_WIDTH || y > TankClient.W_HEIGHT) {
			live = false;
		}
	}

	public Rectangle getRact() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.live && this.getRact().intersects(t.getRact()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()){
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0){
					t.setLive(false);
				}
			}else{t.setLive(false);}
			this.live = false;
			Explode e = new Explode(x, y, this.tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size();i++) {
			if(hitTank(tanks.get(i)))
			return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
		if(this.live && this.getRact().intersects(w.getRact())){
			this.live = false;
			return true;
		}
		return false;
	}
}
