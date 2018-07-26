package com.sxt.msb;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
public class NetClient {
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	public NetClient(){
		udpPort = UDP_PORT_START ++;
	}
	public void connect(String sIP,int port){
		Socket s = null;
		try {
			s = new Socket(sIP,port);
			System.out.println("Connected to Server");
			DataOutputStream dis= new DataOutputStream(s.getOutputStream());
			dis.writeInt(udpPort);
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
