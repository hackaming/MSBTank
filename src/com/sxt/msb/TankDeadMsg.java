package com.sxt.msb;

import java.io.DataInputStream;
import java.net.DatagramSocket;

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
		
	}

	@Override
	public void parse(DataInputStream dis) {
		
	}

}
