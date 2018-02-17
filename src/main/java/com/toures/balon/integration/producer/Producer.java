package com.toures.balon.integration.producer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
public class Producer {
	
	private static Producer instance = null;
 
	/**
	 * Constructor
	 */
	private Producer () {}
	
	/**
	 * Get instace Producer
	 * @return
	 */
	public static Producer getInstance() {
		return instance;
	}	
	
    private static final String URL = "tcp://localhost:61616";
 
    private static final String USER = ActiveMQConnection.DEFAULT_USER;
 
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
 
    private static final String DESTINATION_QUEUE = "ERP.QUEUE";
 
    private static final boolean TRANSACTED_SESSION = true;
    
    private static final int MESSAGES_TO_SEND = 20;
 
    /**
     * 
     * @param message
     * @throws JMSException
     */
    public void sendMessages(String message) throws JMSException {
 
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
 
        final Session session = connection.createSession(TRANSACTED_SESSION, Session.AUTO_ACKNOWLEDGE);
        final Destination destination = session.createQueue(DESTINATION_QUEUE);
 
        final MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 
        sendMessages(session, producer, message);
        session.commit();
 
        session.close();
        connection.close();
 
        System.out.println("Mensajes enviados correctamente");
    }
 
    /**
     * 
     * @param session
     * @param producer
     * @param message
     * @throws JMSException
     */
    private void sendMessages(Session session, MessageProducer producer, String message) throws JMSException {
        final Producer messageSender = new Producer();
        for (int i = 1; i <= MESSAGES_TO_SEND; i++) {
            messageSender.sendMessage(message, session, producer);
        }
    }
 
    /**
     * 
     * @param message
     * @param session
     * @param producer
     * @throws JMSException
     */
    private void sendMessage(String message, Session session, MessageProducer producer) throws JMSException {
        final TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);
    }
 
}