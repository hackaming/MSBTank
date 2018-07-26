package com.sxt.msb;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
public class NetClient {
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	TankClient tc = null;
	public NetClient(TankClient tc){
		udpPort = UDP_PORT_START ++;
		this.tc = tc;
	}
	public void connect(String sIP,int port){
		Socket s = null;
		try {
			s = new Socket(sIP,port);
			DataOutputStream dos= new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis =  new DataInputStream(s.getInputStream()); // get the tank id and bind it.
			tc.myTank.id=dis.readInt();
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
	}
}
