package com.sxt.msb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	int id;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	int x, y;
	private boolean bL = false, bR = false, bU = false, bD = false;
	private boolean isLive = true;
	Direction dir = Direction.STOP;
	Direction ptDir = Direction.D;

	private TankClient tc = null;
	boolean good = true;
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tank(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public Tank(int x, int y, TankClient tc, boolean good) {
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.good = good;
	}

	public Tank(int x, int y, TankClient tc, boolean good,Direction dir) {
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.good = good;
		this.dir = dir;

	}

	public void draw(Graphics g) {
		if (!isLive) {
			return;
		}
		Color c = g.getColor();
		if (good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.YELLOW);
		g.fillOval(x, y, 30, 30);
		g.setColor(c);

		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
			break;
		}
		move();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
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

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
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
			fire();
		}
		locationDirection();
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public Missile fire() {
		int x = this.x + Tank.WIDTH / 2 - Missile.MISSLEWIDTH / 2;
		int y = this.y + Tank.HEIGHT - Missile.MISSLEHEIGHT / 2;
		Missile m = new Missile(x, y, ptDir, tc,id);
		tc.missiles.add(m);
		MissileNewMsg mns = new MissileNewMsg(m);
		System.out.println("New missile generated, id is:" + id);
		tc.nc.send(mns);
		return m;
	}

	public void locationDirection() {
		Direction oldDir = this.dir;
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
		if (dir != Direction.STOP) {
			ptDir = dir;
		}
		if (oldDir != this.dir){
			TankMoveMsg msg = new TankMoveMsg(id,x,y,dir,tc,ptDir);
			System.out.println("From tank, the new generated TankMoveMsg's type is:"+msg.getMsgType()+"Now call th tc.nc.send(msg) to send out to server.");
			tc.nc.send(msg);
		}
	}

	public void move() {
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
		case STOP:
			break;
		}
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x + Tank.WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		if (!good) {
			Direction[] dirs = Direction.values();
			if (step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if (r.nextInt(40) > 38)
				this.fire();
		}
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
}
