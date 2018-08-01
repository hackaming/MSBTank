package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ExplodeNewMsg implements Msg{
	TankClient tc;
	Explode e;
	public ExplodeNewMsg(TankClient tc){
		this.tc = tc;
	}
	public ExplodeNewMsg(Explode e){
		this.e = e;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.EXPLODE_NEW_MSG); //message type
			System.out.println("!!!EXPLODE_NEW_MSG is write into packet");
			dos.writeInt(e.x);
			dos.writeInt(e.y);
			dos.writeInt(e.tankid);
			dos.writeInt(e.explodeId);
			System.out.println("Explode generated!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,new InetSocketAddress(IP,udpPort));
			ds.send(dp);
			System.out.println("Explode sent out!");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int x = dis.readInt();
			int y = dis.readInt();
			int tankid = dis.readInt();
			int explodeId = dis.readInt();
			if (tankid == tc.myTank.id){
				System.out.println(explodeId+":the explosion is belongs to my tank. get rid of it."+tankid);
				return ; //don't draw as it's my explosion
			}
			boolean bExist = false;
			for(int i=0;i<tc.explodes.size();i++){
				Explode e = tc.explodes.get(i);
				if (e.explodeId == explodeId){
					bExist = true;
				}
			}
			if (!bExist){
				Explode e = new Explode(x,y,this.tc);
				tc.explodes.add(e);
				System.out.println("Explode received, added into explodes list.x i:"+x+" y is:"+y);				
			} else {
				System.out.println("Explode already drawed seems, get rid of it.i:"+x+" y is:"+y);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
