package com.cafe24.network.chat.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class ChatServerThread {
	private static final int PORT = 7000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket =null;
		ChatClient client = new ChatClient();
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
			System.out.println("server On");
			while(true) {
				Socket socket = serverSocket.accept();
				Thread serverThread = new Thread(new ChatServerReceive(client, socket));
				serverThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
