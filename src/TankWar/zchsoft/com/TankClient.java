package TankWar.zchsoft.com;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author zch
 *  这个类的作用是主入口
 */
public class TankClient extends Frame {
	private static final long serialVersionUID = 1L;
	/**
	 * 整个窗口的宽度
	 */
	public static final int  GAME_WIDTH=800;
	public static final int  GAME_HEIGHT=600;
	
	Tank myTank=new Tank(50, 50,this,true,Direction.STOP);
	Blood b=new Blood();
	List<Tank> tanks =new ArrayList<Tank>();
	List<Missile> missiles =new ArrayList<Missile>();
	List<Explode> explodes=new ArrayList<Explode>();
	Wall w1=new Wall(100, 200, 20, 150, this);
	Wall w2=new Wall(300, 200, 300, 20, this);
	
	
	Image offscreenImage=null;
	
	 @Override
	public void update(Graphics g){
		 
		 if(offscreenImage==null){
			 offscreenImage=this.createImage(GAME_WIDTH, GAME_HEIGHT);
		 }
         
		 Graphics gOffscreen=offscreenImage.getGraphics();
		 Color c=gOffscreen.getColor();
		 gOffscreen.setColor(Color.GREEN);
		 gOffscreen.fillRect(0,0, GAME_WIDTH, GAME_HEIGHT);
		 gOffscreen.setColor(c);
		 paint(gOffscreen);
		 g.drawImage(offscreenImage, 0, 0,null);
		 
	 }
	
	 
    @Override
    public void paint(Graphics g){
    	Color c=g.getColor();
    	g.setColor(Color.GRAY);
    	g.drawString("Missiles:"+missiles.size(), 20, 60);
    	g.drawString("Explodes:"+explodes.size(), 20, 90);
    	g.drawString("Tanks:"+tanks.size(), 20, 120);
    	g.drawString("Tanks:"+myTank.getLife(), 20, 150);
    	g.setColor(c);
    	
    	myTank.darw(g);
    	b.darw(g);
    	w1.darw(g);
    	w2.darw(g);
    	
    	
    	
    	for(int i=0;i<missiles.size();i++){
    		Missile m=missiles.get(i);
    		m.hitTanks(tanks);
    		m.hitTank(myTank);
    		m.hitWall(w1);
    		m.hitWall(w2);
    	    m.darw(g);
    	}
    	for(int i=0;i<explodes.size();i++){
    		Explode e=explodes.get(i);
    	    e.darw(g);
    	}
    	for(int i=0;i<tanks.size();i++){
    		Tank t=tanks.get(i);
    		t.collidesWithWall(w1);
    		t.collidesWithWall(w2);
    		t.collidesWithTanks(tanks);
    	    t.darw(g);
    	}
    	
    }
    
    
	public static void main(String[] args) {
		new TankClient().luanchfrome();
		
	}
	
/**
 * 本方法显示主窗口
 */
	public void luanchfrome(){
		for(int i=0;i<10;i++){
			tanks.add(new Tank(50+40*(i+1), 50, this, false,Direction.D));
		}
		this.setLocation(400,150);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		setVisible(true);
//		setResizable(false);
		new Thread(new paintThread()).start();
		this.setBackground(Color.GREEN);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
                 System.exit(-1);
			}
		});
		
		this.addKeyListener(new Keymonitor() );
	
		
	}
	
	private class paintThread implements Runnable{

		@Override
		public void run() {
           while(true){
        	   repaint();
        	   try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
           }
            
		}
		
	}
	
	
	private class Keymonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		@Override
		public void keyReleased(KeyEvent e){
			myTank.keyReleased(e);
		}
		
	}
	
	
}
