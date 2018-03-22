package com.cooksys.FileTransfer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ClientHandler implements Runnable {
	Socket sock;
	
	public ClientHandler(Socket sock) {
		this.sock = sock;
	}
	
	public void run() {
		BufferedReader incoming = null;
		
		try {
			incoming = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String PATH = "C:\\Users\\ftd-01\\code\\FileTransfer\\Students";
		
		File dir = new File(PATH);
		
		if(!dir.exists()) {
			dir.mkdir();
		} else {
			System.out.println(dir + " exists!");
		}

		File file = new File(dir + "\\" + "JasonBrown" + ".xml");
	
		if(!file.exists()) {
			try {
				file.createNewFile();
				
				String newLine;
				try {
					DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
					String nextLine = System.getProperty("line.separator");
					while((newLine = incoming.readLine()) !=  null) {
						writer.writeBytes(newLine);
						writer.writeBytes(nextLine);
						
						System.out.println(newLine);
					}
					
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File exists!");
		}
	}
}
