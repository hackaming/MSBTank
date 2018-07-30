package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class MissileNewMsg implements Msg{
	DatagramSocket ds = null;
	private int x;
	private int y;
	TankClient tc ;
	int tankid;
	Tank t;
	Direction dir;
	public MissileNewMsg(TankClient tc,int tankid){
		this.tc = tc;
		this.tankid = tankid;
		System.out.println("New MissileNewMsg was created, tankid is:" + tankid);
	}
	public MissileNewMsg(int x,int y,TankClient tc,Tank t,Direction dir,int tankid){
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.t= t;
		this.tankid = tankid;
		this.dir = dir;
	}
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.MISSILE_NEW_MSG); //message type;
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeInt(this.tankid);
			dos.writeInt(t.dir.ordinal());
			System.out.println("New packet's write to dos, id is:"+this.tankid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[1024];
		buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length, new InetSocketAddress(IP,udpPort));
			ds.send(dp);
			System.out.println("Datagrampacket's sent out for Missile New Msg.");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int x = dis.readInt();
			int y = dis.readInt();
			int id = dis.readInt();
			this.dir = Direction.values()[dis.readInt()];
			if (id == tc.myTank.id){
				return;
			}
			System.out.println("Tank id of MissileNewMsg is:" + tankid + "   Need to check if tank id is equal to the id received in the packet!");
			System.out.println("Datagram packet's parsed: x,y,id is:"+x+"  "+y+"  "+"  "+id +"now, added it into tc.misiles array list");
			Missile m = new Missile(x,y,dir);
			tc.missiles.add(m);    // here sould be some gus, does it need to send out, how to disginguish if the missile's myself and re-send?
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
