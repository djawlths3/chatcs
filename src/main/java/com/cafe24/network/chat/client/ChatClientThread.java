package com.cafe24.network.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientThread {
	//private static final String SERVER_IP = "192.168.1.23";
	private static final String SERVER_IP = "192.168.219.100";
	private static final int SERVER_PORT = 7000;
	
	public static void main(String[] args) {
		Scanner sanner = null;
		Socket socket = null;
		try {
			sanner = new Scanner(System.in);
			
			// 1. socket create
			socket = new Socket();
			
			
			// 2. server connect			
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(),"utf-8") );
			System.out.print("닉네임을 입력하세요 : ");
			String nickName = sanner.nextLine();
			PrintWriter pw = new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true ); // true 값은 자동으로 flush 해주는 기능
			pw.println(nickName);
			String idCheck = br.readLine();
			// 닉네임 중복 Check
			while( !("ok".equals(idCheck)) ) {
				System.out.println(idCheck);
				pw.println( sanner.nextLine());
				idCheck = br.readLine();
			}
			Thread send = new Thread(new ClientSend(pw,nickName));
			send.start();
			while(true) {
				String data = br.readLine();
				System.out.println(data);
				//5. 키보드 입력 받기
				//System.out.print(">>");
//				String line = sanner.nextLine();
//				if("quit".contentEquals(line)) {
//					pw.println("quit");
//					break;
//				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(sanner != null) {
					sanner.close();					
				}
				if(socket != null && socket.isClosed() == false) {
					socket.close();					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
