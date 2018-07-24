package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	int x,y;
	private boolean bL = false,bR = false,bU = false,bD = false;
	private Direction dir = Direction.STOP;
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	
	public Tank(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);
		move();
	}
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		switch (key){
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locationDirection();
	}
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch (key){
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locationDirection();
	}
	public void locationDirection(){
		if (bL && !bU && !bR && !bD) dir = Direction.L;
		else if (!bL && bU && !bR && !bD) dir = Direction.U;
		else if (!bL && !bU && bR && !bD) dir = Direction.R;
		else if (!bL && !bU && !bR && bD) dir = Direction.D;
		else if (bL && bU && !bR && !bD) dir = Direction.LU;
		else if (!bL && bU && bR && !bD) dir = Direction.RU;
		else if (!bL && !bU && bR && bD) dir = Direction.RD;
		else if (bL && !bU && !bR && bD) dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	public void move(){
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
		case STOP:
			break;
		}
	}
}
