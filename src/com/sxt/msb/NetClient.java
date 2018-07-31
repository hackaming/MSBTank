package com.sxt.msb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
public class NetClient {
	private static int UDP_PORT_START = 2246;
	private int udpPort;
	TankClient tc = null;
	DatagramSocket ds = null;
	public NetClient(TankClient tc){
		this.tc = tc;
	}
	public void connect(String sIP,int port){
		Socket s = null;
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		try {
			s = new Socket(sIP,port);
			DataOutputStream dos= new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis =  new DataInputStream(s.getInputStream()); // get the tank id and bind it.
			tc.myTank.id=dis.readInt();
			if (tc.myTank.id %2 == 0){
				tc.myTank.good = true;
			} else {
				tc.myTank.good = false;
			}
			System.out.println("Connected to Server, get tank id is:" + tc.myTank.id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				s = null;
			}
		}
		// send some message
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UDPThread()).start();
	}
	public void send(Msg msg){
		msg.send(ds, "127.0.0.1", TankServer.UDPPORT);
	}
	private class UDPThread implements Runnable{
		byte[] buf = new byte[1024];
		public void run() {
			while (ds!=null){
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				try {
					ds.receive(dp);
					System.out.println("Received a packet from server!");
					parse(dp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		private void parse(DatagramPacket dp){
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength()); //bug will show, because the buf's used to receive!!but don't change it for now as MSB havn't yet change the code.
			DataInputStream dis = new DataInputStream (bais);
			try {
				int msgType = dis.readInt();
				System.out.println("Received a message in NetClient.Message type is:" + msgType);
				Msg msg = null;
				switch (msgType){
				case Msg.TANK_NEW_MSG:
					msg = new TankNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.TANK_MOVE_MSG:
					msg = new TankMoveMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.MISSILE_NEW_MSG:
					msg = new MissileNewMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				case Msg.TANK_DEAD_MSG:
					msg = new TankDeadMsg(NetClient.this.tc);
					msg.parse(dis);
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
}
