package com.sxt.msb;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	NetClient nc = new NetClient(this);
	ConnectDiag dialog = new ConnectDiag();
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
				TankDeadMsg tdm = new TankDeadMsg(myTank); //when exit the game, needs to send out tank dead message.
				nc.send(tdm);
				//also needs to send out the network client's removed message so that the server realized that no needs to send message to this client anymore!
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		this.setBackground(Color.GREEN);
		this.setVisible(true);
		new Thread(new PaintThread()).start();
		myTank = new Tank(50, 50, this);
		tanks.add(myTank);
		//nc.connect("127.0.0.1", TankServer.TCPPORT);
		dialog.setVisible(true);
	}
	public void generateBadTank(){
		Tank badTank = new Tank(50, 50, this,false);
		tanks.add(badTank);
	}

	public void paint(Graphics g) {
		g.drawString("Missiles count:"+missiles.size(), 10, 50);
		g.drawString("Tank count:"+tanks.size(), 10, 70);
		g.drawString("Explode count:"+explodes.size(), 10, 90);
		g.drawString("id:"+myTank.id, 10, 110);
		for (int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			m.draw(g);
			for (int j=0;j<tanks.size();j++){
				Tank t = tanks.get(j);
				if (t.id!=myTank.id) m.hitTank(t);
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
	private class ConnectDiag extends Dialog{
		Button b = new Button("OK");
		TextField tfIP = new TextField("127.0.0.1",12);
		TextField tfPort = new TextField(""+TankServer.TCPPORT);
		TextField tfMyUDPPort = new TextField("2223",4);
		public ConnectDiag() {
			super(TankClient.this,true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.setLocation(300,300);
			this.pack();
			this.addWindowFocusListener(new WindowAdapter(){

				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			b.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText().trim());
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, port);
					setVisible(false);
				}
				
			});
		}
		
	}
}
