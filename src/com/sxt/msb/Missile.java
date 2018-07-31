package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int MISSLEWIDTH = 10;
	public static final int MISSLEHEIGHT = 10;
	public static int missileID = 0;
	int tankid;
	Direction dir;
	TankClient tc = null;
	int x, y;
	boolean isLive = true;
	boolean isGood;

	public Missile(int x, int y, Direction dir,boolean isGood) {
		missileID ++;
		this.x = x;
		this.y = y;
		this.isGood = isGood;
		if (dir != Direction.STOP) {
			this.dir = dir;
		} else {
			this.dir = Direction.U;
		}
	}
	public Missile(int x, int y, Direction dir,int tankid,boolean isGood) {
		missileID++;
		this.x = x;
		this.y = y;
		this.tankid = tankid;
		this.isGood = isGood;
		if (dir != Direction.STOP) {
			this.dir = dir;
		} else {
			this.dir = Direction.U;
		}
	}
	public Missile(int x, int y, Direction dir, TankClient tc,int tankid,boolean isGood) {
		missileID++;
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.tankid = tankid;
		this.isGood = isGood;
		if (dir != Direction.STOP) {
			this.dir = dir;
		} else {
			this.dir = Direction.U;
		}
		System.out.println("A new Missile's generated. x,y,tank id and dir is:" + x+"  " +y+ "  " +tankid+ "  " +dir+ " ");
		System.out.println("This dir is:"+this.dir);
	}

	public void draw(Graphics g) {
		if (!isLive) {
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, MISSLEWIDTH, MISSLEHEIGHT);
		g.setColor(c);
		move();
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, MISSLEWIDTH, MISSLEHEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.getRect().intersects(t.getRect()) && t.isLive() && this.isLive && (t.good != this.isGood)) {
			t.setLive(false);
			tc.tanks.remove(t);
			this.isLive = false; //will also needs to send out missiles neds to be removed.
			tc.missiles.remove(this); // will needs to sent out tanks dead message and explode message.otherwise the dead tank in different client will not be removed.
			TankDeadMsg tdm = new TankDeadMsg(t);
			tc.nc.send(tdm);
			System.out.println("The TankDeadMSG's newed in Missile and sent out!");
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return false;
		}
		return false;
	}

	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case RU:
			y -= YSPEED;
			x += XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case LD:
			y += YSPEED;
			x -= XSPEED;
			break;
		}
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			tc.missiles.remove(this);
		}
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
}
