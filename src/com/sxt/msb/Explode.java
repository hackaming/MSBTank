package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	private int x,y;
	private TankClient tc = null;
	private boolean bAlive = true;
	int[] diameter = {4,7,12,18,26,32,49,30,14,6};
	int step = 0;
	public Explode(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	public void draw(Graphics g){
		if (!bAlive){
			return;
		}
		if (step == diameter.length){
			bAlive = false;
			step = 0;
			tc.explodes.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.orange);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step ++;
	}
}
