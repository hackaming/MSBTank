package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	int x,y;
	private boolean bL = false,bR = false,bU = false,bD = false;
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.STOP;
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	private TankClient tc = null;
	
	public Tank(int x,int y){
		this.x = x;
		this.y = y;
	}
	public Tank(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);
		switch(ptDir){
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		}
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
		case KeyEvent.VK_S:
			tc.missiles.add(fire());
		}
		locationDirection();
	}
	public Missile fire(){
		int x = this.x + Tank.WIDTH/2 - Missile.MISSLEWIDTH /2;
		int y = this.y + Tank.HEIGHT- Missile.MISSLEHEIGHT /2;
		Missile m = new Missile(x,y,ptDir);
		return m;
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
		if (dir != Direction.STOP){
			ptDir = dir;
		}
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
