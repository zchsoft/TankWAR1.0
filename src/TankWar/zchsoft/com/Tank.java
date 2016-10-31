package TankWar.zchsoft.com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
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

	public void setGood(boolean good) {
		this.good = good;
	}

	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	private int x, y;
	private Direction ptDir = Direction.D;
	private TankClient tc = null;
	private boolean good;
	private boolean live = true;
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	private int oldX, oldY;
	private int life = 100;
	private BloodBar bb = new BloodBar();
	Direction dir = Direction.STOP;

	private boolean bL = false, bU = false, bR = false, bD = false;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Tank(int x, int y, TankClient tc, boolean good, Direction dir) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.tc = tc;
		this.good = good;
		this.dir = dir;
	}

	/**
	 * 
	 * @param g
	 *            画笔
	 */
	public void darw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if (good)
			bb.darw(g);
		if (good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		switch (ptDir) {
		case L:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y);
			break;
		case U:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y);
			break;
		case RU:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y);
			break;
		case R:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT);
			break;
		case D:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT);
			break;
		case LD:
			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT);
			break;

		}

		move();
	}

	void move() {
		Direction[] dirs = Direction.values();
		int rn = r.nextInt(dirs.length);
		this.oldX = x;
		this.oldY = y;
		eat();
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
		case RD:
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

		}
		if (this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}

		if (x < 0) {
			x = 0;
			dir = dirs[rn];
		}
		if (y < 30) {
			y = 30;
			dir = dirs[rn];
		}
		if (x + WIDTH > TankClient.GAME_WIDTH) {
			x = TankClient.GAME_WIDTH - WIDTH;
			dir = dirs[rn];
		}
		if (y + HEIGHT > TankClient.GAME_HEIGHT) {
			y = TankClient.GAME_HEIGHT - HEIGHT;
			dir = dirs[rn];
		}

		if (!good) {
			if (step == 0) {
				step = r.nextInt(12) + 3;
				dir = dirs[rn];
			}
			step--;
			if (r.nextInt(40) > 20)
				this.fire();
		}
		
		if(good){
			this.superFire();
		}
		
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if (tc.tanks.size() == 0) {
				for (int i = 0; i < 10; i++) {
					tc.tanks.add(new Tank(50 + 40 * (i + 1), 50, tc, false,
							Direction.D));
				}
			}
			if (!tc.myTank.isLive()) {
				tc.myTank.setLife(100);
				tc.myTank.setLive(true);
			}
			break;
		case KeyEvent.VK_F1:
			superFire();
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;

		}
		locateDirect();
	}

	void locateDirect() {
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;

	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;

		}
		locateDirect();

	}

	public Missile fire() {
		if (!live)
			return null;
		Missile m = new Missile(x + WIDTH / 2 - Missile.WIDTH / 2, y + HEIGHT
				/ 2 - Missile.HEIGHT / 2, ptDir, tc, this.good);
		tc.missiles.add(m);

		return m;
	}

	public void eat() {
		if (good) {
			System.out.println(this.getRect().intersects(tc.b.getRect()));
			if (this.getRect().intersects(tc.b.getRect())) {
				life = 100;
			}
		}
	}

	/**
	 * 
	 * @param dir
	 *            子弹的方向
	 * @return 返回新产生的子弹
	 */
	public Missile fire(Direction dir) {
		if (!live)
			return null;
		Missile m = new Missile(x + WIDTH / 2 - Missile.WIDTH / 2, y + HEIGHT
				/ 2 - Missile.HEIGHT / 2, dir, tc, this.good);
		tc.missiles.add(m);

		return m;
	}

	public void superFire() {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length - 1; i++) {
			fire(dirs[i]);
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void stay() {
		x = oldX;
		y = oldY;
	}

	public boolean collidesWithWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (t != this) {
				if (this.live && this.getRect().intersects(t.getRect())) {
					stay();
					return true;
				}
			}
		}
		return false;
	}

	private class BloodBar {
		public void darw(Graphics g) {
			Color c = g.getColor();
			int w = WIDTH * life / 100;
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, WIDTH, 10);
			g.fillRect(x, y - 10, w, 10);
			g.setColor(c);

		}
	}

}
