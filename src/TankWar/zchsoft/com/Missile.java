package TankWar.zchsoft.com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {

	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	int x, y;
	private boolean live = true;
	private boolean good;
	TankClient tc = null;
	Direction dir;

	public Missile(int x, int y, Direction dir, TankClient tc,boolean good) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tc = tc;
		this.good=good;
	}

	public void darw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
			}
		Color c = g.getColor();
		g.setColor(Color.BLACK);
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
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH
				|| y > TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
	}

	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean hitTank(Tank t){
  		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good!=t.isGood()){
	     if(t.isGood()){
	    	 t.setLife(t.getLife()-20);
	    	 if (t.getLife()<=0)
            	t.setLive(false);
	     }else{
	    	 t.setLive(false);
	     }
	     this.live=false;
	     tc.explodes.add(new Explode(this.x, this.y, this.tc));
		return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
  		if(this.live && this.getRect().intersects(w.getRect()) ){
	     this.live=false;
		 return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))) return true;
		}
		return false;
	}

}
