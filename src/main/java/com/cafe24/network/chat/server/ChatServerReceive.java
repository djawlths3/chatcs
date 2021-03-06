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
	private static final String DEVICE_KEY = ",,//,;";
	
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
			pw.println("ok"); //서버접속 확인
			client.AddClient(nickName, pw); //사용자 추가
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
				String[] msg = data.split(DEVICE_KEY);
				String message = arrToString(msg);
				String nickName = msg[0];
				// 채팅창 특수 기능 구현
				if("/".equals(message.substring(0, 1))) {
					//종료
					if("/quit".equals(message)) {
						client.RemoveClient(nickName);
						break;
					} else if("/W".equals(message.split(" ")[0]) || "/w".equals(message.split(" ")[0])) { 
						//귓속말 기능
						String tgNickName = message.split(" ")[1];
						if( !(client.clientManage.containsKey(tgNickName)) ) {
							client.sendMsg("/w remove 해당 사용자가 없습니다", nickName, nickName);
						} else {
							client.sendMsg(message, nickName, tgNickName);							
						}
					}
					
				}else {
					//사용자들에게 메세지 전송
					client.sendMsg(message, nickName, null);					
				}
			}
			
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
