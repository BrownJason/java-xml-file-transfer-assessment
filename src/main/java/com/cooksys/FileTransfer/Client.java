package com.cooksys.FileTransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	public void readFile() {
		try {
			File file = new File("JasonBrown");
			if(!file.exists()) {
				System.out.println("File does not exist!");
			} else {
				System.out.println("Retrieving file and Converting to XML");
				reading = new BufferedReader(new FileReader("JasonBrown"));
				output = new StreamResult("Jason.xml");
				fileToXML();
				String newLine;
				while((newLine = reading.readLine()) != null) {
					convertToXml(newLine);
				}
				reading.close();
				closeXML();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		try(Socket sock = new Socket("localhost", 10101)){
		
			new Client().readFile();
			
			try {
				Files.list(Paths.get(".")).forEach(System.out::println);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			File file = new File("Jason.xml");
			
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
	}
}
