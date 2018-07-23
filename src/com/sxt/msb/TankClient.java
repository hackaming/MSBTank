package com.sxt.msb;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {
	int x=50,y=50;
	public static void main(String[] argv) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	public void launchFrame() {
		this.setLocation(400, 300);
		this.setSize(800, 600);
		this.setTitle("TankWar");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
		new Thread(new PaintThread()).start();
	}
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);
		y += 5;
	}
	private class PaintThread implements Runnable{
		@Override
		public void run() {
			while(true){
				repaint();
				try{
					Thread.sleep(100);
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}
