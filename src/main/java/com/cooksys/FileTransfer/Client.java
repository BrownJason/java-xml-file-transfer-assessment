package com.cooksys.FileTransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

public class Client {
	BufferedReader reading;
	StreamResult output;
	
	TransformerHandler th;
	
	public void readFile() throws UnknownHostException, IOException {
		try(Socket sock = new Socket("localhost", 10101)){
			try {
				
				try {
					Files.list(Paths.get(".")).forEach(System.out::println);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String filename = null;
				String Path = "C:\\Users\\ftd-01\\code\\FileTransfer";
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				if(reader.readLine() != null) {
					filename = reader.readLine().toString();
					File file = new File(Path + filename);
					
					if(!file.exists()) {
						System.out.println("File does not exist!");
					} else {
						System.out.println("Retrieving file and Converting to XML");
						reading = new BufferedReader(new FileReader(file));
						output = new StreamResult(file + ".xml");
						fileToXML();
						String line;
						
						while((line = reading.readLine()) != null) {
							convertToXml(line);
						}
						reading.close();
						closeXML();
						
						BufferedReader reading = new BufferedReader(new FileReader(file));
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
						
						if(!file.exists()) {
							System.out.println("File does not exist!");
						}
				
						String newLine;
						while((newLine = reading.readLine()) != null) {
							writer.write(newLine + '\n');
							writer.flush();
						}	
						reading.close();
					}
				}  else {
					System.out.println("Couldn't do it!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void fileToXML() throws TransformerConfigurationException, SAXException {
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		
		th = tf.newTransformerHandler();
		Transformer serial = th.getTransformer();

		serial.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		serial.setOutputProperty(OutputKeys.INDENT, "yes");
		
		th.setResult(output);
		th.startDocument();
		th.startElement(null, null, "Students", null);
	}
	
	public void convertToXml(String s) throws SAXException {
		String [] elements = s.split("\\|");
		th.startCDATA();
		th.startElement(null, null, "New_Student", null);
		
		th.startElement(null, null, "Username", null);
		th.characters(elements[0].toCharArray(), 0, elements[0].length());
		th.endElement(null, null, "Username");
		
		th.startElement(null, null, "Date", null);
		th.characters(elements[1].toCharArray(), 0, elements[1].length());
		th.endElement(null, null, "Date");
		
		th.startElement(null, null, "FileName", null);
		th.characters(elements[2].toCharArray(), 0, elements[2].length());
		th.endElement(null, null, "FileName");
		
		th.startElement(null, null, "Content_Of_File", null);
		th.characters(elements[3].toCharArray(), 0, elements[3].length());
		th.endElement(null, null, "Content_Of_File");
		
		th.endElement(null, null, "New_Student");
		th.endCDATA();
	}
	
	public void closeXML() throws SAXException {
		th.endElement(null, null, "Students");
		th.endDocument();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, JAXBException {
		new Client().readFile();
	}
}
