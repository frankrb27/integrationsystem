package com.toures.balon.integration.transform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.jms.JMSException;

import com.toures.balon.integration.producer.Producer;

public class TransforMessage {

	private static String RESOURCE_BUNDLE_APP = "com.toures.balon.integration.application";

	private static ResourceBundle bundleApp = ResourceBundle.getBundle(RESOURCE_BUNDLE_APP);

	public TransforMessage() {}

	/**
	 * Proccess consolid file account
	 * @throws Exception
	 */
	private void proccessFile() throws Exception {
		String fileName = getFileName(false);
		String line = null;
		FileReader fileReader = new FileReader(bundleApp.getString("file.read").concat(fileName));
		BufferedReader buffer = new BufferedReader(fileReader);
		while((line=buffer.readLine()) != null) {
			String message = transforMessage(line);
			if(message != null) {
				sendMessage(message);
			}
		}
		buffer.close();
	}

	/**
	 * Get consolid file name
	 * @return
	 */
	private String getFileName(boolean isFileError) {
		String fileName = bundleApp.getString("file.name");
		String extension = bundleApp.getString("file.extension");
		Calendar c = Calendar.getInstance();
		if(isFileError) {
			fileName = fileName.concat("Err-").concat(""+c.get(Calendar.YEAR)).concat(""+c.get((Calendar.MONTH+1))).concat(""+c.get(Calendar.DAY_OF_MONTH)).concat(extension);
		}else {
			fileName = fileName.concat(""+c.get(Calendar.YEAR)).concat(""+c.get((Calendar.MONTH+1))).concat(""+c.get(Calendar.DAY_OF_MONTH)).concat(extension);
		}
		return fileName;
	}

	/**
	 * Transform String to XML
	 * @param message
	 * @return
	 */
	private String transforMessage (String message) {
		String XML = null;
		int initialPos=0, finalPos=0;
		int length = Integer.parseInt(bundleApp.getString("message.length"));
		int size = Integer.parseInt(bundleApp.getString("message.size"));
		String arrayInfo[] = new String[size];
		if(message.length() == length) {
			System.out.println("Message: "+message);
			//Info
			for(int i = 0; i<size; i++) {
				initialPos = Integer.parseInt(bundleApp.getString("message.part"+i+".initial"));
				finalPos = Integer.parseInt(bundleApp.getString("message.part"+i+".final"));
				arrayInfo[i] = message.substring(initialPos, finalPos);				
			}
			XML = MessageFormat.format(bundleApp.getString("message.xml"),arrayInfo);
			System.out.println(XML);
			return XML;	
		}else {
			try {
				writeFileError(message);	
			}catch(Exception e) {
				System.out.println("ERROR: "+e.getMessage());
			}			
			System.out.println(XML);
			return null;
		}		
	}

	/**
	 * Send message to ActiveMQ
	 * @param message
	 * @throws JMSException
	 */
	private void sendMessage(String message) throws JMSException{
		new Producer().sendMessages(message);
	}

	/**
	 * 
	 * @param message
	 * @throws JMSException
	 * @throws Exception
	 */
	private void writeFileError(String line) throws Exception {
		String fileName = getFileName(true);
		//File archivo = new File(PATH.concat(fileName));
		BufferedWriter bw = new BufferedWriter(new FileWriter(bundleApp.getString("file.error").concat(fileName), true));
		bw.append(line);
		bw.append("\n");
		bw.close();            
	}

	/**
	 * TEST
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final TransforMessage message = new TransforMessage();
		message.proccessFile();
	}
}
