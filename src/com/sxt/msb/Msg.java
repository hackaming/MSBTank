package com.sxt.msb;

import java.net.DatagramSocket;
import java.io.*;
public interface Msg {
	public static final int TANK_NEW_MSG = 1;
	public static final int TANK_MOVE_MSG = 2;
	public void send (DatagramSocket ds,String IP,int udpPort);
	public void parse (DataInputStream dis);
}
