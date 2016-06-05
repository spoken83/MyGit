package org.quanto.common;

import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class QuantoUtility {

	
	public static String getStringFromDoc(Document doc) {
		DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
		LSSerializer lsSerializer = domImplementation.createLSSerializer();
		return lsSerializer.writeToString(doc);
	}

	public static double randSpot(double init, double discountFactor) {
		int minPip = 1;
		int maxPip = 8;

		Random rand = new Random();

		int sign = rand.nextInt(2); // 0: positive 1: negative
		double delta = rand.nextInt(maxPip - minPip) * discountFactor;

		if (sign == 0)
			init = init + delta;
		else
			init = init - delta;

		return Math.round(init * (1 / discountFactor)) / (1 / discountFactor);
	}
}
