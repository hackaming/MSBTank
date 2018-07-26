package com.sxt.msb;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.io.*;
public class TankServer {
	public static final int TCPPORT = 8888;
	public static final int UDPPORT = 6666;
	public static int tankID = 100;
	List<Client> clients = new ArrayList<Client>();
	public   void start(){
		ServerSocket ss = null;
		//List <>
		new Thread(new UDPThread()).start();
		try {
			ss = new ServerSocket(TCPPORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("TankServer's started, waiting to connect!");
		while(true){
			Socket s = null;
			try{
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); //send out the tank id;
				dos.writeInt(tankID ++);
				String ip = s.getInetAddress().getHostAddress();
				Client c = new Client(ip,udpPort);
				clients.add(c);
				System.out.println("A client's connected!"+s.getInetAddress()+":"+s.getPort()+" udp port is:" + udpPort+" IP is:" + ip);
				dis.close();
				s.close();
			} catch (IOException e){
			} finally {
				if (s != null){
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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
	private class UDPThread implements Runnable{
		byte[] buf = new byte[1024];
		@Override
		public void run() {
			DatagramSocket ds = null;
				try {
					ds = new DatagramSocket(UDPPORT);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				System.out.println("UDP thread started at port:" + UDPPORT);
				while (ds != null){
					DatagramPacket dp = new DatagramPacket(buf,buf.length);
					try {
						ds.receive(dp);
						for (int i=0;i<clients.size();i++){
							Client c = clients.get(i);
							dp.setSocketAddress(new InetSocketAddress(c.sIP,c.iUDPPort));
							ds.send(dp);
						}
						System.out.println("a packet received!");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
	}
}
