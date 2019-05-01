package com.cafe24.network.chat.client;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientSend implements Runnable{

	public static PrintWriter pw = null;
	public static String nickName = null;
	
	public ClientSend(PrintWriter pw, String nickName) {
		this.pw = pw;
		this.nickName = nickName;
	}
	
	@Override
	public void run() {
		Scanner sanner = null;
		sanner = new Scanner(System.in);
		while(true) {
			String line = sanner.nextLine();
			pw.println(nickName +",,//,/" +line);
			if("quit".equals(line)) {
				sanner.close();
				break;
			}
		}
		
	}

}
