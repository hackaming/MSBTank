package com.sxt.msb;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class MissileDeadMsg implements Msg{
	int msgType = Msg.MISSILE_DEAD_MSG;
	Tank t;
	Missile m;
	TankClient tc;
	
	public MissileDeadMsg(TankClient tc){
		this.tc = tc;
	}
	public MissileDeadMsg(Missile m){
		this.m = m;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.MISSILE_DEAD_MSG); //message type
			dos.writeInt(m.missileID); // missile id,right now, i think missile id is enough when it dead.
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray(); //generate a byte array
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort)); //make a data packet and sent out
			ds.send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();  //get the missile id
			if (null!= tc){
				for (int i=0;i<tc.missiles.size();i++){
					if (tc.missiles.get(i).missileID == id){
						tc.missiles.remove(tc.missiles.get(i));
						System.out.println("Missile's dead, now removed it from missile list." + id);
					}
				}
			} else {
				System.out.println("!!!Exception:TC's null in MissileDead Mesage. Can't remove the Missiles.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
