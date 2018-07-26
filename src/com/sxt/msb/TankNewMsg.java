package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg {
	int msgType = Msg.TANK_NEW_MSG;
	Tank tank;
	TankClient tc;
	public TankNewMsg(Tank tank){
		this.tank = tank;
	}
	public TankNewMsg(){
		
	}
	public TankNewMsg(TankClient tc){
		this.tc = tc;
	}
	public void parse(DataInputStream dis){
		try {
			int tankid = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			System.out.println("id:" + tankid + "-x:" + x + "-y:" + y + "-dir:" + dir + "-good:" + good);
			Tank t = new Tank(x,y,tc,good,dir);
			t.id = tankid;
			tc.tanks.add(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void send(DatagramSocket ds,String sIP,int udpPort){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
			dos.writeBoolean(tank.good);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(sIP,udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
