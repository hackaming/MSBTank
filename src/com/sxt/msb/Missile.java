package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int MISSLEWIDTH = 10;
	public static final int MISSLEHEIGHT = 10;
	Tank.Direction dir;
	int x,y;
	public Missile(int x,int y,Tank.Direction dir){
		this.x = x;
		this.y = y;
		if (dir != Tank.Direction.STOP){
			this.dir = dir;
		} else {
			this.dir = Tank.Direction.U;
		}
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, MISSLEWIDTH, MISSLEHEIGHT);
		g.setColor(c);
		move();
	}
	private void move(){
		switch (dir){
		case L:
			x-=XSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case D:
			y+=YSPEED;
			break;
		case RU:
			y-=YSPEED;
			x+=XSPEED;
			break;
		case LU:
			x-=XSPEED;
			y-=YSPEED;
			break;
		case RD:
			x+=XSPEED;
			y+=YSPEED;
			break;
		case LD:
			y+=YSPEED;
			x-=XSPEED;
			break;
		}
	}
}
