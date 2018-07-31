package com.sxt.msb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankDeadMsg implements Msg{
	TankClient tc;
	Tank t;
	public TankDeadMsg(TankClient tc){
		this.tc = tc;
	}
	public TankDeadMsg(Tank t){
		this.t= t;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			if (null == t){
				System.out.println("!!!!!!!!!!The tank in tank dead msg is not initialized, can't continue!!");
			}
			dos.writeInt(Msg.TANK_DEAD_MSG); //message type
			dos.writeInt(t.id); //if a tank's dead, id is enough?just removed it from the tank list.
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
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
			int id = dis.readInt();
			for (int i=0;i<tc.tanks.size();i++){
				if (tc.tanks.get(i).id == id) {
					tc.tanks.remove(tc.tanks.get(i));
					System.out.println(id+" tank 's removed from the tank list!");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
