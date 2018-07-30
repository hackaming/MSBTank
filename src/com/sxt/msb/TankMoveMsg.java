package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg  implements Msg{
	private int msgType;
	private int id;
	private int x;
	private int y;
	private Direction dir;
	private Direction ptDir;
	TankClient tc;
	public TankMoveMsg(int id, int x, int y, Direction dir,TankClient tc,Direction ptDir) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.tc = tc;
		this.ptDir = ptDir;
		this.msgType = Msg.TANK_MOVE_MSG;
	}
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
		this.msgType = Msg.TANK_NEW_MSG;
	}
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			System.out.println("Construct the TankMoveMSG, Type is:"+msgType);
			dos.writeInt(msgType);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
			ds.send(dp);
			System.out.println("Send a Tank move message out!"+msgType);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			System.out.println("Tank Move Msg was called to parse msesage!");
			int x = dis.readInt();
			int y = dis.readInt();
			int id = dis.readInt();
			Direction dir =Direction.values()[ dis.readInt()];
			Direction ptDir =Direction.values()[ dis.readInt()];
			if (id == tc.myTank.id){
				return;
			}
			boolean bExist = false;
			for (int i=0;i<tc.tanks.size();i++){
				Tank t = tc.tanks.get(i);
				if (t.id == id){
					System.out.println("x and y of the tank before change:"+t.x+"    "+t.y);
					t.x = x;
					t.y = y;
					t.dir = dir;
					t.ptDir = ptDir; //note that many of the local variable needs to be set to private and setup the getters and setters.
					bExist = true;
					System.out.println("x and y of the tank after change:"+t.x+"    "+t.y);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
}
