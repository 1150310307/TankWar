import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2991376889565273423L;
	// 设置窗口初始化位置的x坐标
	public static final int LOCATION_WIDTH = 100;
	// 设置窗口初始化位置的y坐标
	public static final int LOCATION_HEIGHT = 100;
	// 设置窗口的宽
	public static final int W_WIDTH = 800;
	// 设置窗口的高
	public static final int W_HEIGHT = 600;

	Tank myTank = new Tank(600, 600, Direction.STOP, true, this);
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	Wall w1 = new Wall(100, 100, 500, 20, this);
	Wall w2 = new Wall(100, 200, 20, 300, this);
	Blood b = new Blood();

	// 定义一个背景Image 为update方法定义的对象
	Image offScreenImage = null;

	/*
	 * 创建窗口
	 */
	public void launchFrame() {
		// 加载敌方坦克
		for (int i = 0; i < 10; i++) {
			Tank t = new Tank(50 * (i + 1) + 100, 300, Direction.D, false, this);
			tanks.add(t);
		}
		// 设置窗口的初始化位置
		this.setLocation(LOCATION_WIDTH, LOCATION_HEIGHT);
		// 设置窗口的大小
		this.setSize(W_WIDTH, W_HEIGHT);
		// 设置窗口的标题
		this.setTitle("TankWar");
		// 设置一个匿名类，用于关闭窗口
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// 是否允许用户修改界面大小
		this.setResizable(false);
		// 设置背景颜色
		this.setBackground(Color.YELLOW);
		// 绑定键盘监听器
		this.addKeyListener(new KeyMonitor());
		// 设置组件可见
		this.setVisible(true);

		// 调用线程绘制坦克
		new Thread(new PointThread()).start();
	}

	/*
	 * 绘制坦克
	 */
	public void paint(Graphics g) {
		if (tanks.size() <= 0) {
			for (int i = 0; i < 10; i++) {
				Tank t = new Tank(50 * (i + 1) + 100, 300, Direction.D, false, this);
				tanks.add(t);
			}
		}
		myTank.draw(g);
		myTank.hitWall(w1);
		myTank.hitWall(w2);
		myTank.crashTanks(tanks);
		myTank.eatBlood(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		g.drawString("子弹数量：" + missiles.size(), 10, 40);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		g.drawString("爆炸数量：" + explodes.size(), 10, 80);
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		g.drawString("敌方坦克数量：" + tanks.size(), 10, 60);
		g.drawString("当前生命值：" + myTank.getLife(), 10, 100);
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			t.crashmyTank(tanks, myTank);
			t.crashTanks(tanks);
			t.draw(g);
		}
	}

	/*
	 * 重写update方法，消除屏幕闪烁
	 */

	public void update(Graphics g) {
		if (offScreenImage == null) {
			// 创建一个图片
			offScreenImage = this.createImage(W_WIDTH, W_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.YELLOW);
		gOffScreen.fillRect(0, 0, W_WIDTH, W_HEIGHT);
		gOffScreen.setColor(c);
		// 调用绘制坦克的方法
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/*
	 * 内部类 定义一个线程，用于绘制坦克
	 */
	private class PointThread implements Runnable {
		public void run() {
			while (true) {
				try {
					repaint();
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 创建一个内部类 键盘监听
	 */
	private class KeyMonitor extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
}
