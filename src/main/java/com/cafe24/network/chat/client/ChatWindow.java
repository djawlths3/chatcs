package com.cafe24.network.chat.client;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private static PrintWriter pw = null;
	private static String nickName = null;
	private static final String DEVICE_KEY = ",,//,;";
	
	// 서버에서 온 메시지를 내 창에 표시한다
	public void printMessage(String msg) {
		this.textArea.append(msg+"\r\n");
	}
	
	//내가 쓴 문자를 전달한다.
	public void writeMessage() {
		String message = "";
		message = this.textField.getText();
		if(message == null || "".equals(message)) {
			return;
		}
		pw.println(nickName +",,//,;" +message);
		textField.setText("");
		textField.requestFocus();
		if("/quit".equals(message)) {
			finish();
		}
	}
	
	//생성자
	public ChatWindow(String name, PrintWriter pw, String nickName) {
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		this.pw = pw;
		this.nickName = nickName;
	}
	
	//종료
	private void finish() {
			pw.println(nickName +DEVICE_KEY +"/quit");
			System.out.println("..........");
			System.exit(0);
	}
	//GUI 창을 띄움
	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				writeMessage();
			}
		});

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					writeMessage();
				}
			}
		});
		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);
		
		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
		frame.setVisible(true);
		frame.pack();
	}

}
