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
				+ "������Client");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ "������Client");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

		sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ " ���_Client�s�u");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " ���_Client�s�u");
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
		   //�����쪺message�P�_
	        switch(message){
	        case "Notepad":
	        
			Runtime rt = Runtime.getRuntime();
			try {
			rt.exec("notepad");//���� notepad.exe(�O�ƥ�)
			System.out.println("�}�ҰO�ƥ�");
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
//�ǰe�T��
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
        //���w�ҨϥΪ�port
		int port = 8887;
        //�ҥ�Server
		Server server = new Server(port);
		server.start();
       //��ܩҦ��s�i�Ӫ�client
		System.out.println("�ثeserver�ұĥΪ�port" + server.getPort());
      
		BufferedReader webSocketIn = new BufferedReader(new InputStreamReader(
				System.in));
        //�|���Ҧ��s�W��client�ǻ�message
		while (true) {
			String stringIn = webSocketIn.readLine();
			server.sendToAll(stringIn);
		}
	}
}
