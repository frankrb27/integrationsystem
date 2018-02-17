package com.toures.balon.integration.transform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;

import javax.jms.JMSException;

import com.toures.balon.integration.producer.Producer;

public class TransforMessage {

	private static final String PATH = "/home/frank/personal/javeriana/Semestre_II/Implementacion/proyecto/java-code/accountsystem/";

	private static final String FILE = "consolid-account-";

	private static final String EXTENSION = ".txt";

	public TransforMessage() {}

	/**
	 * Proccess consolid file account
	 * @throws Exception
	 */
	private void proccessFile() throws Exception {
		String fileName = getFileName();
		String linea = null;
		FileReader fileReader = new FileReader(PATH.concat(fileName));
		BufferedReader buffer = new BufferedReader(fileReader);
		while((linea=buffer.readLine()) != null) {
			String message = transforMessage(linea);
			sendMessage(message);
		}
		buffer.close();
	}

	/**
	 * Get consolid file name
	 * @return
	 */
	private String getFileName() {
		String fileName = FILE;
		Calendar c = Calendar.getInstance();
		fileName = fileName.concat(""+c.get(Calendar.YEAR)).concat(""+c.get((Calendar.MONTH+1))).concat(""+c.get(Calendar.DAY_OF_MONTH)).concat(EXTENSION);
		return fileName;
	}

	/**
	 * Transform String to XML
	 * @param message
	 * @return
	 */
	private String transforMessage (String message) {
		String XML = null;
		
		return XML;
	}
	
	/**
	 * Send message to ActiveMQ
	 * @param message
	 * @throws JMSException
	 */
	private void sendMessage(String message) throws JMSException{
		Producer.getInstance().sendMessages(message);
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
