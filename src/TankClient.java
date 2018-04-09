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
	// ���ô��ڳ�ʼ��λ�õ�x����
	public static final int LOCATION_WIDTH = 100;
	// ���ô��ڳ�ʼ��λ�õ�y����
	public static final int LOCATION_HEIGHT = 100;
	// ���ô��ڵĿ�
	public static final int W_WIDTH = 800;
	// ���ô��ڵĸ�
	public static final int W_HEIGHT = 600;

	Tank myTank = new Tank(600, 600, Direction.STOP, true, this);
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	Wall w1 = new Wall(100, 100, 500, 20, this);
	Wall w2 = new Wall(100, 200, 20, 300, this);
	Blood b = new Blood();

	// ����һ������Image Ϊupdate��������Ķ���
	Image offScreenImage = null;

	/*
	 * ��������
	 */
	public void launchFrame() {
		// ���صз�̹��
		for (int i = 0; i < 10; i++) {
			Tank t = new Tank(50 * (i + 1) + 100, 300, Direction.D, false, this);
			tanks.add(t);
		}
		// ���ô��ڵĳ�ʼ��λ��
		this.setLocation(LOCATION_WIDTH, LOCATION_HEIGHT);
		// ���ô��ڵĴ�С
		this.setSize(W_WIDTH, W_HEIGHT);
		// ���ô��ڵı���
		this.setTitle("TankWar");
		// ����һ�������࣬���ڹرմ���
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// �Ƿ������û��޸Ľ����С
		this.setResizable(false);
		// ���ñ�����ɫ
		this.setBackground(Color.YELLOW);
		// �󶨼��̼�����
		this.addKeyListener(new KeyMonitor());
		// ��������ɼ�
		this.setVisible(true);

		// �����̻߳���̹��
		new Thread(new PointThread()).start();
	}

	/*
	 * ����̹��
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
		g.drawString("�ӵ�������" + missiles.size(), 10, 40);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		g.drawString("��ը������" + explodes.size(), 10, 80);
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		g.drawString("�з�̹��������" + tanks.size(), 10, 60);
		g.drawString("��ǰ����ֵ��" + myTank.getLife(), 10, 100);
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
	 * ��дupdate������������Ļ��˸
	 */

	public void update(Graphics g) {
		if (offScreenImage == null) {
			// ����һ��ͼƬ
			offScreenImage = this.createImage(W_WIDTH, W_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.YELLOW);
		gOffScreen.fillRect(0, 0, W_WIDTH, W_HEIGHT);
		gOffScreen.setColor(c);
		// ���û���̹�˵ķ���
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/*
	 * �ڲ��� ����һ���̣߳����ڻ���̹��
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
	 * ����һ���ڲ��� ���̼���
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
