package com.sxt.msb;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public class ExplodeNewMsg implements Msg{
	TankClient tc;
	public ExplodeNewMsg(TankClient tc){
		this.tc = tc;
	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		
	}

	@Override
	public void parse(DataInputStream dis) {
		
	}

}
