package org.wlf.java_websocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {

	public Server(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public Server(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

		sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ "偵測到Client");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ "偵測到Client");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

		sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ " 中斷Client連線");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " 中斷Client連線");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		sendToAll("["
				+ conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ "]" + message);

		System.out.println("["
				+ conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ "]" + message);	
		System.out.println("message: " +message);
		   //做收到的message判斷
	        switch(message){
	        case "Notepad":
	        
			Runtime rt = Runtime.getRuntime();
			try {
			rt.exec("notepad");//執行 notepad.exe(記事本)
			System.out.println("開啟記事本");
			}
			catch (IOException e) {
			System.err.println(e);	
	     	}
			break;
			default:
				System.out.println("Command Error");
				break;
	        }
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		e.printStackTrace();
		if (conn != null) {
			conn.close();
		}
	}
//傳送訊息
	private void sendToAll(String text) {
		
		Collection<WebSocket> conns = connections();
		synchronized (conns) {
			for (WebSocket client : conns) {
				client.send(text);
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
        //指定所使用的port
		int port = 8887;
        //啟用Server
		Server server = new Server(port);
		server.start();
       //顯示所有連進來的client
		System.out.println("目前server所採用的port" + server.getPort());
      
		BufferedReader webSocketIn = new BufferedReader(new InputStreamReader(
				System.in));
        //會像所有連上的client傳遞message
		while (true) {
			String stringIn = webSocketIn.readLine();
			server.sendToAll(stringIn);
		}
	}
}
