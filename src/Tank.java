import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;

public class Tank {

	int x, y;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	public static final int PTLONG = 5;
	TankClient tc;
	private BloodStrand bs = new BloodStrand();

	private boolean good;
	private boolean live = true;

	public boolean bL = false, bU = false, bR = false, bD = false;

	private static Random r = new Random();
	private int step = r.nextInt(15) + 5;
	private int life = 100;
	

	public Direction dir = Direction.STOP;
	public Direction ptdir = Direction.R;
	public int oldx, oldy;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.good = good;
	}

	public Tank(int x, int y, Direction dir, boolean good, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	/*
	 * 绘制坦克的方法
	 */
	public void draw(Graphics g) {
		if (!live) {
			tc.tanks.remove(this);
			return;
		}
		// 获得背景色 默认是黑色
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.red);
			bs.draw(g);
		} else {
			g.setColor(Color.LIGHT_GRAY);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);

		switch (ptdir) {
		case L:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x - PTLONG, y + HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y - PTLONG);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH + PTLONG, y + Tank.HEIGHT / 2);
			break;
		case DR:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT + PTLONG);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
			break;
		default:
			break;
		}
		move();
	}

	/*
	 * 控制移动
	 */
	void move() {
		this.oldx = x;
		this.oldy = y;
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
		if (this.dir != Direction.STOP) {
			ptdir = this.dir;
		}
		if (x < 10)
			x = 10;
		if (y < 30)
			y = 30;
		if (x > TankClient.W_WIDTH - 10 - Tank.WIDTH)
			x = TankClient.W_WIDTH - 10 - Tank.WIDTH;
		if (y > TankClient.W_HEIGHT - 10 - Tank.HEIGHT)
			y = TankClient.W_HEIGHT - 10 - Tank.HEIGHT;
		if (!good) {
			Direction[] dirs = Direction.values();
			if (step == 0) {
				step = r.nextInt(15) + 5;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if (r.nextInt(100) > 98)
				this.fire();
		}
	}

	public void stay() {
		x = this.oldx;
		y = this.oldy;
	}

	public boolean hitWall(Wall w) {
		if (this.live && this.getRact().intersects(w.getRact())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean eatBlood(Blood b){
		if(this.live && b.isLive() && this.getRact().intersects(b.getRact())){
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
	public boolean crashTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.live && this.getRact().intersects(t.getRact())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}

	public boolean crashmyTank(List<Tank> tanks,Tank tank) {
		for(int i=0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			if (tank.live && t.live && tank.getRact().intersects(t.getRact())) {
//				tank.live = false;
//				tank.life = 0;
//				Explode e = new Explode(tank.x, tank.y, tc);
//				tc.explodes.add(e);
				tank.stay();
				t.stay();
				return true;
			}
		}
		return false;
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		switch (k) {
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}

	/*
	 * 给Direction赋值
	 */
	void locateDirection() {
		if (bL && !bU && !bR && !bD) {
			dir = Direction.L;
		} else if (bL && bU && !bR && !bD) {
			dir = Direction.LU;
		} else if (!bL && bU && !bR && !bD) {
			dir = Direction.U;
		} else if (!bL && bU && bR && !bD) {
			dir = Direction.RU;
		} else if (!bL && !bU && bR && !bD) {
			dir = Direction.R;
		} else if (!bL && !bU && bR && bD) {
			dir = Direction.DR;
		} else if (!bL && !bU && !bR && bD) {
			dir = Direction.D;
		} else if (bL && !bU && !bR && bD) {
			dir = Direction.LD;
		} else if (!bL && !bU && !bR && !bD) {
			dir = Direction.STOP;
		}
	}

	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		switch (k) {
		case KeyEvent.VK_F2:
			if(!this.live){
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_CONTROL:
			superfire();
			break;
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locateDirection();
	}

	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, ptdir, this.tc);
		tc.missiles.add(m);
		return m;
	}

	public void superfire() {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length - 1; i++) {
			fire(dirs[i]);
		}
	}

	private Missile fire(Direction dir) {
		if (!live)
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}

	public Rectangle getRact() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	private class BloodStrand{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			g.fillRect(x, y-10, WIDTH*life/100, 10);
			g.setColor(c);
		}
	}
}
