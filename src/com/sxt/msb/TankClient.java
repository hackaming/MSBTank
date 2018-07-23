package com.sxt.msb;
import java.awt.*;
public class TankClient extends Frame{
	public static void main(String[] argv){
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	public void launchFrame(){
		this.setLocation(400, 300);
		this.setSize(800, 600);
		this.setVisible(true);
	}
}
