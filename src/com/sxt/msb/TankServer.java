package com.sxt.msb;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.io.*;
public class TankServer {
	public static final int TCPPORT = 8888;
	List<Client> clients = new ArrayList<Client>();
	public   void start(){
		ServerSocket ss = null;
		//List <>
		try {
			ss = new ServerSocket(TCPPORT);
			while (true){
				Socket s = 	ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				String ip = s.getInetAddress().getHostAddress();
				Client c = new Client(ip,udpPort);
				clients.add(c);
				System.out.println("A client's connected!"+s.getInetAddress()+":"+s.getPort()+" udp port is:" + udpPort+" IP is:" + ip);
				dis.close();
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] argv){
		new TankServer().start();
	}
	private class Client{
		private String sIP;
		private int iUDPPort;
		public Client(String ip, int udpPort){
			this.sIP = ip;
			this.iUDPPort = udpPort;
		}
	}
}
