package com.sxt.msb;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public class NetClientDeadMsg implements Msg{
	int msgType = Msg.NETCLIENT_DEAD_MSG;
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		
	}

}
