package org.quanto.tests;

import javax.jms.MessageListener;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.quanto.common.ContextUtil;
import org.quanto.web_examples.HelloWorldConsumer;



public class SpotListener_old implements MessageListener {
	
	public static Thread _lock;
	public static boolean _failed = false;
	public static boolean _finished = false;

	

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		Connection connection = null;
		Context context = null;
		_lock = new Thread();
		try {
			System.out.println("Create JNDI Context");
			context = ContextUtil.getInitialContext();

			System.out.println("Get connection facory");
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");

			System.out.println("Create connection");
			connection = connectionFactory.createConnection();

			System.out.println("Create session");
			Session session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

			System.out.println("Lookup queue");
			Queue queue = (Queue) context.lookup("/queue/HelloWorldQueue");

			System.out.println("Start connection");
			connection.start();

			System.out.println("Create consumer");
			MessageConsumer consumer = session.createConsumer(queue);

			System.out.println("set message listener");
			consumer.setMessageListener(new HelloWorldConsumer());

			_lock.start();
			// Wait for the messageConsumer to have received all the messages it needs
			synchronized (_lock)
			{
			    while (!_finished && !_failed)
			    {
			        _lock.wait();
			    }
			}
			if (_failed)
			{
			    System.out.println(": ERROR: invalid message(s)");
			}


		} finally {
			if (connection != null) {
				System.out.println("close the connection");
				connection.close();
				context.close();

			}
		}
	}
	/*
	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("message received");
			System.out.println(((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public void onMessage(Message message)
	{
	    try
	    {
	        String text = null;
	        if (message instanceof TextMessage)
	        {
	            text = ((TextMessage) message).getText();
	        }


	        if (text.equals("That's all, folks!"))
	        {
	            System.out.println(": Received final message " + text);
	            synchronized (_lock)
	            {
	                _finished = true;
	                _lock.notifyAll();
	            }
	        }
	        else
	        {
	            System.out.println( ": Received  message:  " + text);
	        }
	    }
	    catch (JMSException exp)
	    {
	        System.out.println(  ": Caught an exception handling a received message");
	        exp.printStackTrace();
	        synchronized (_lock)
	        {
	            _failed = true;
	            _lock.notifyAll();
	        }
	    }
	}

}