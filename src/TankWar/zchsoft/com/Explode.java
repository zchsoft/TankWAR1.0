package TankWar.zchsoft.com;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	int x, y;
	private boolean live = true;
	int[] diameter = { 4, 7, 12, 18, 26, 32, 49, 30, 14, 6 };
	int step;
	TankClient tc;

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void darw(Graphics g) {
		if (!live)
			return;
		if (step == diameter.length) {
			live = false;
			step = 0;
			tc.explodes.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step  ++  ;
	}

}
