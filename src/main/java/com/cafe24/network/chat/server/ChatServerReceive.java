package com.cafe24.network.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatServerReceive implements Runnable {
	public static Socket socket = null;
	public static Client client = new Client();

	
	public ChatServerReceive(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(),"utf-8") );
			PrintWriter pw = new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true ); 
			//닉네임 중복체크
			String nickName = br.readLine();
			while( (client.clientManage.containsKey(nickName)) ) {
				pw.println("중복되는 닉네임이 있습니다. 아이디를 다시 입력하세요 : ");
				nickName = br.readLine();				
			}
			pw.println("ok");
			client.AddClient(nickName, pw);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void run() {
		
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(),"utf-8") );
			PrintWriter pr = new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true ); 
			while(true) {
				String data = br.readLine();
				String[] msg = data.split(",,//,;");
				String message = arrToString(msg);
				client.sendMsg(message, msg[0], null);
				if("/quit".equals(message)) {
					client.RemoveClient(msg[0]);
				}
			}
			
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 아이디랑 메시지 분리
	public String arrToString(String[] arr) {
		int arrLength = arr.length;
		String message = "";
		for(int i=1; i<arrLength; i++) {
			message += arr[i];
			if( i != (arrLength -1) ) {
				message += " ";
			}
		}
		return message;
	}

}
