package com.cooksys.FileTransfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) throws IOException {
		try(ServerSocket server = new ServerSocket(10101)){
			while(true) {
				System.out.println("Waiting for a connection!");
				Socket sock = server.accept();
				System.out.println("Connetion received: " + sock);
				if(sock.isConnected()) {
					Thread thread = new Thread(new ClientHandler(sock));
					thread.start();
				} else if (!sock.isConnected()){
					System.out.println("Connection lost! Please reconnect!");
				}
			}
		}
	}
}
