package com.cafe24.network.chat.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ChatClient {
	static HashMap<String, PrintWriter> clientManage = new HashMap<String, PrintWriter>();
	

	
	//서버에 사용자 추가, 전체 메세지 보냄
	public synchronized void AddClient(String name,PrintWriter pw)            
    {                                                                        
        try {
        	clientManage.put(name,pw);
            sendMsg(name+"님 입장하셨습니다. 총 접속 인원 : "+clientManage.size()+"\n",name,null);
        }catch(Exception e){
        	e.printStackTrace();
        }
 
    }
	//서버에 사용자 제거, 전체 메세지 보냄
	public synchronized void RemoveClient(String name)  
    {
        try {
        	sendMsg(name+"님 퇴장하셨습니다. 총 접속 인원 : "+(clientManage.size() -1) +"\n",name,null);
        	clientManage.remove(name);            
        }catch(Exception e) {}
    }
	
	// 메세지 보냄
	static synchronized void sendMsg(String msg, String nickName, String tgNickName) {
		//clientManage.get(nickName).println(msg.getBytes());
		if(tgNickName == null) {
			for(String key :clientManage.keySet()) {
				clientManage.get(key).println(nickName+" : "+msg);
			}			
		} else {
			//귓속말 기능
			clientManage.get(tgNickName).println(nickName+": "+msg.getBytes());
		}
	}	
	
}
