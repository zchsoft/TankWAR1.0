package TankWar.zchsoft.com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Blood {
   int x=230;
   int y=460;
   int w=10;
   int h=10;
   
   Random r=new Random();
   private TankClient tc;
   public void darw(Graphics g){
	   Color c=g.getColor();
	   g.setColor(Color.PINK);
	   g.fillRect(x, y, w, h);
	   g.setColor(c);
   }
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
   
}
