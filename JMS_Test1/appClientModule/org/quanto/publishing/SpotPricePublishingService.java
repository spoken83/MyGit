package org.quanto.publishing;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;

import org.quanto.common.ContextUtil;
import org.quanto.common.QuantoUtility;
import org.quanto.common.XMLWriterSpot;

public class SpotPricePublishingService {

	private Session session = null;
	private Queue queue = null;
	private Topic topic = null;
	private MessageProducer producer = null;
	private Connection conn = null;

	public static void main(String[] args) {

		SpotPricePublishingService sp = new SpotPricePublishingService();
		sp.createConnection();

		String topicFlag = "Topic"; // possible values: "Topic", "Queue"

		//initialize with starting Sport rate and discount factor
		SpotData EURUSDSPOT = new SpotData("EUR/USD", 1.12062, 0.00001);
		SpotData USDJPYSPOT = new SpotData("USD/JPY", 109.413, 0.001);

		//number of publishes
		int count = 5;
		String xmlObject = null;
		while (count > 0) {
			try {
				Thread.sleep(1000); // 1000 milliseconds is one second.
				count--;
				
				xmlObject = sp.createXML(EURUSDSPOT);
				EURUSDSPOT.updateRates();
				sp.publishMessage(xmlObject, topicFlag);

				xmlObject = sp.createXML(USDJPYSPOT);
				USDJPYSPOT.updateRates();
				sp.publishMessage(xmlObject, topicFlag);

			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}


	/*
	 * this is pulishing line by line int count = 6; double EURUSDSpot = 1.1201;
	 * double EURUSDDF = 0.0001; double USDJPYSpot = 109.88; double USDJPYDF =
	 * 0.001;
	 * 
	 * while (count > 0) { try { Thread.sleep(1000); // 1000 milliseconds is one
	 * second. count--;
	 * 
	 * EURUSDSpot = QuantoUtility.randSpot(EURUSDSpot, EURUSDDF);
	 * sp.createSpotMsg("EUR-USD", EURUSDSpot, topicFlag); USDJPYSpot =
	 * QuantoUtility.randSpot(USDJPYSpot, USDJPYDF); sp.createSpotMsg("USD-JPY",
	 * USDJPYSpot, topicFlag);
	 * 
	 * } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
	 * }
	 * 
	 * sp.createSpotMsg("Bye", USDJPYSpot, topicFlag);
	 */

	sp.closeConnection();

	}

	private void createConnection() {
		try {
			System.out.println("Create JNDI Context");
			Context context = ContextUtil.getInitialContext();

			System.out.println("Get connection facory");
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");

			System.out.println("Create connection");
			conn = connectionFactory.createConnection();

			/// TOPIC
			// PUBLISH TO TOPIC -- > "WebMXStreamingSpot"
			System.out.println("Create topic session");
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = (Topic) context.lookup("topic/WebMXStreamingSpot");

			//// Queue
			// System.out.println("Create queue session");
			// session = conn.createSession(false,
			//// QueueSession.AUTO_ACKNOWLEDGE);
			// System.out.println("Lookup queue");
			// queue = (Queue) context.lookup("/queue/HelloWorldQueue");

			System.out.println("Start connection");
			conn.start();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void closeConnection() {
		System.out.println("close the connection");
		try {
			conn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createSpotMsg(String curr, double spot, String TopicQueue) {
		try {

			if (TopicQueue == "Topic")
				// System.out.println("Create producer");
				producer = session.createProducer(topic);
			else
				producer = session.createProducer(queue);

			// System.out.println("Create Spot Message");
			Message SPOT_MSG = session.createTextMessage(curr + " : " + spot);

			System.out.println(curr + " : " + spot);
			producer.send(SPOT_MSG);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String createXML(SpotData spotData) {
		// make instance of XMLWriter
		XMLWriterSpot xmlWriter = new XMLWriterSpot("print", spotData);
		xmlWriter.createXMLMessage();
		String xmlStr = QuantoUtility.getStringFromDoc(xmlWriter.getXMLDoc());
		return xmlStr;
	}

	private void publishMessage(String msg, String TopicQueue) {
		try {
			if (TopicQueue == "Topic")
				// System.out.println("Create producer");
				producer = session.createProducer(topic);
			else
				producer = session.createProducer(queue);

			// System.out.println("Create Spot Message");
			Message XML_MSG = session.createTextMessage(msg);

			producer.send(XML_MSG);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}