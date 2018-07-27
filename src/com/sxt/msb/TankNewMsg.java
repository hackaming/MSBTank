package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg implements Msg{
	int msgType;
	Tank tank;
	TankClient tc;
	public TankNewMsg(Tank tank){
		this.tank = tank;
		this.msgType = Msg.TANK_NEW_MSG;
	}
	public TankNewMsg(TankClient tc){
		this.tc = tc;
		this.msgType = Msg.TANK_NEW_MSG;
	}
	public void parse(DataInputStream dis){
		try {
			int tankid = dis.readInt();
			//System.out.println(tc.myTank.id+"---->This is my tank id, is it null?debug use only!");
			//System.out.println(tankid+"---->This is the tank id get from server, is it null?debug use only!");
			System.out.println("Tank New Msg was called to parse msesage!");
			if ((tc.myTank.id == tankid)){
				return;
			}
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			System.out.println("id:" + tankid + "-x:" + x + "-y:" + y + "-dir:" + dir + "-good:" + good);
			// confirm if the tc's exist
			boolean bExist = false;
			for (int i=0;i<tc.tanks.size();i++){
				Tank t = tc.tanks.get(i);
				if (t.id == tankid){
					bExist = true;
					break;
				}
			}
			if (!bExist){
				TankNewMsg tnMsg = new TankNewMsg(tc.myTank);
				tc.nc.send(tnMsg); //announce?
				
				Tank t = new Tank(x,y,tc,good,dir);
				t.id = tankid;
				tc.tanks.add(t);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void send(DatagramSocket ds,String sIP,int udpPort){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			System.out.println("Construct the TankNewMsg, Type is:"+msgType);
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
			System.out.println("A Tank new Msg was sent out!");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
