package com.sxt.msb;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int GAME_STARTX = 400;
	public static final int GAME_STARTY = 100;
	Image offScreenImage = null;
	Tank myTank = null;
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles= new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	//public Missile m = null;

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	int x = 50, y = 50;

	public static void main(String[] argv) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

	public void launchFrame() {
		this.setLocation(GAME_STARTX, GAME_STARTY);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		this.setBackground(Color.GREEN);
		this.setVisible(true);
		new Thread(new PaintThread()).start();
		myTank = new Tank(50, 50, this);
		
		tanks.add(myTank);
		
	}
	public void generateBadTank(){
		Tank badTank = new Tank(50, 50, this,false);
		tanks.add(badTank);
	}

	public void paint(Graphics g) {
		g.drawString("Missiles count:"+missiles.size(), 10, 50);
		g.drawString("Tank count:"+tanks.size(), 10, 70);
		
		for (int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			m.draw(g);
			for (int j=0;j<tanks.size();j++){
				Tank t = tanks.get(j);
				if (!t.good) {
					m.hitTank(t);
				}
			}
		}
		for (int i=0;i<tanks.size();i++){
			tanks.get(i).draw(g);
		}
		for ( int i=0;i<explodes.size();i++){
			explodes.get(i).draw(g);
		}
	}

	private class PaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1){
				generateBadTank();
			}
			myTank.keyPressed(e);
		}
	}
}
