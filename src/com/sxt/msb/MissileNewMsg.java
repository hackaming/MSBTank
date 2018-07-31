package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class MissileNewMsg implements Msg{ //should add some id to the message so when the tankclient receive it, it will not re-write it when it added to the missileslist.
	DatagramSocket ds = null;
	Missile m;
	//private int x;
	//private int y;
	TankClient tc ;
	Tank t;
	//Direction dir;
	public MissileNewMsg(TankClient tc){
		this.tc = tc;
		//System.out.println("New MissileNewMsg was created, tankid is:" + tankid);
	}
	public MissileNewMsg(Missile m){
		this.m  = m;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.MISSILE_NEW_MSG); //message type;
			dos.writeInt(m.tankid);
			dos.writeInt(m.x);
			dos.writeInt(m.y);
			//dos.writeInt(this.tankid);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.isGood);
			//dos.writeInt(1); //debug only
			System.out.println("msg type is:"+Msg.MISSILE_NEW_MSG+"New packet's write to dos,x is:"+m.x+" y is:"+m.y+" tank id is:"+m.tankid+" dir is:"+m.dir.ordinal());
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
			int id = dis.readInt();
			if (id == tc.myTank.id){
				System.out.println("My tank's missile, get rid of it!");
				return;
			} else {
				System.out.println("The id received's different from mine"+id+", continue!:"+tc.myTank.id);
			}
			int x = dis.readInt();
			int y = dis.readInt();
			System.out.println("x is:"+x+" y is:"+y);
//			System.out.println("parse the it in missile new messag:x is:" + x + " y is:" + y + " id is:" + id+"dir is:"+i);
			if (null == dis){
				System.out.println("Why the hell is the dis's null???");
			} else {
				System.out.println("The dis's not null, but why dis.readint get null exceptin??");
			}
			if (null  == m){
				System.out.println("the m's null, can't set value, will show exception!!");
			}
			Direction dir = Direction.values()[dis.readInt()];
			boolean isGood = dis.readBoolean();
			Missile m1 = new Missile(x, y, dir, tc,id,isGood);
			tc.missiles.add(m1);    // here sould be some gus, does it need to send out, how to disginguish if the missile's myself and re-send?
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
