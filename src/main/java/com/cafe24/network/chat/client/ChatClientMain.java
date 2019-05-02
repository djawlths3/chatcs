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

public class ChatClientMain {
	private static final String SERVER_IP = "192.168.1.23";
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
			
			//아이디랑 메세지 내용 구분하는구분자 체크
			if(",,//,;".equals(nickName)) {
				System.out.println("이문자는 사용하실 수 없습니다. 다시 입력하세요:");
				nickName = sanner.nextLine();
			}
			
			// cw.sendMessage();
			PrintWriter pw = new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true ); // true 값은 자동으로 flush 해주는 기능
			pw.println(nickName);
			// gui 실행
			ChatWindow cw = new ChatWindow(nickName, pw, nickName);
			cw.show();
			String nickNameCheck = br.readLine();
			// 3.닉네임 중복 Check
			while( !("ok".equals(nickNameCheck)) ) {
				System.out.println(nickNameCheck);
				nickName = sanner.nextLine();
				pw.println(nickName);
				nickNameCheck = br.readLine();
			}
//			// 4.쓰기만 실행
//			Thread send = new Thread(new ClientSend(pw,nickName));
//			send.start();
			while(true) {
				//5.읽기만 실행
				String data = br.readLine();
				// System.out.println(data);
				cw.printMessage(data);
			}
			
		} catch (IOException e) {
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
				e.printStackTrace();
			}
		}
	}

}
