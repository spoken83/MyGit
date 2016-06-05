package org.quanto.publishing;

import java.util.HashMap;

import org.quanto.common.QuantoUtility;

public class SpotData {

	// inputs
	String inputKey1 = "Spot-Fwd.1.Instrument";
	String inputValue1 = "AUD/USD";
	String inputKey2 = "Spot-Fwd.1.Maturity period";
	String inputValue2 = "SPOT";
	String inputKey3 = "Spot-Fwd.1.Maturity";
	String inputValue3 = "";

	// outputs
	String outputKey1 = "Spot-Fwd.1.Instrument";
	String outputValue1 = "AUD/USD";
	String outputKey2 = "Spot-Fwd.1.Maturity period";
	String outputValue2 = "SPOT";
	String outputKey3 = "Spot-Fwd.1.Maturity";
	String outputValue3 = "20160201";
	
	String outputKey4 = "Spot-Fwd.1.Ref spot";
	String outputValue4 = "0.702900000000/0.703500000000";
	String outputKey5 = "Swap Points";
	String outputValue5 = "0";
	
	String outputKey6 = "Timestamp";
	String outputValue6 = "20160310 12:11:39:107";
	String outputKey7 = "Operating mode";
	
	String outputValue7 = "Normal";
	String outputKey8 = "N1";
	String outputValue8 = "10000";
	String outputKey9 = "N2";
	String outputValue9 = "100000";
	String outputKey10 = "N3";
	String outputValue10 = "1000000";
	String outputKey11 = "N4";
	String outputValue11 = "0";

	String outputKey12 = "Trader_N1";
	String outputValue12 = "0.699200000000/0.707200000000";
	String outputKey13 = "Trader_N2";
	String outputValue13 = "0.697200000000/0.709200000000";
	String outputKey14 = "Trader_N3";
	String outputValue14 = "0.698200000000/0.708200000000";
	String outputKey15 = "Trader_N4";
	String outputValue15 = "0.695200000000/0.711200000000";

	public HashMap<String, String> Inputmap;
	public HashMap<String, String> Outputmap;

	String CurrencyPair;
	double currentSpot = 1.1212111;
	double discountFactor = 0.0001;
	String fmt = "%.4f";
	
	double N1Spread = 4;
	double N2Spread = 5;
	double N3Spread = 7;
	double N4Spread = 6;

	public SpotData(String curr, double initialSpot, double df) {
		this.CurrencyPair = curr;
		this.discountFactor = df;
		this.currentSpot = initialSpot;
		
		if (df == 0.001) fmt = "%.3f";
		else  fmt = "%.5f";

		createData();
		initialiseCurrencyPair();
	}

	public HashMap<String, String> getInputMap() {
		return Inputmap;
	}

	public HashMap<String, String> getOutputMap() {
		return Outputmap;
	}

	private void createData() {
		/*
		 * Map<String, Dog> inputMap = new HashMap<String, Dog>();
		 * dogMap.put("Fido", new Dog("Fido"));
		 * 
		 * Dog myPet = dogMap.get("Fido");
		 */
		Inputmap = new HashMap<String, String>();
		Outputmap = new HashMap<String, String>();

		Inputmap.put(inputKey1, inputValue1);
		Inputmap.put(inputKey2, inputValue2);
		Inputmap.put(inputKey3, inputValue3);

		Outputmap.put(outputKey1, outputValue1);
		Outputmap.put(outputKey2, outputValue2);
		Outputmap.put(outputKey3, outputValue3);
		Outputmap.put(outputKey4, outputValue4);
		Outputmap.put(outputKey5, outputValue5);
		Outputmap.put(outputKey6, outputValue6);
		Outputmap.put(outputKey7, outputValue7);
		Outputmap.put(outputKey8, outputValue8);
		Outputmap.put(outputKey9, outputValue9);
		Outputmap.put(outputKey10, outputValue10);
		Outputmap.put(outputKey11, outputValue11);
		Outputmap.put(outputKey12, outputValue12);
		Outputmap.put(outputKey13, outputValue13);
		Outputmap.put(outputKey14, outputValue14);
		Outputmap.put(outputKey15, outputValue15);
	}

	public void initialiseCurrencyPair() {
		Inputmap.put(inputKey1, CurrencyPair);
		Outputmap.put(outputKey1, CurrencyPair);
		
		String initialRefSpot = String.format(fmt, currentSpot-(2* discountFactor)) +"/"+ String.format(fmt,currentSpot + (2 * discountFactor));
		Outputmap.put(outputKey4, initialRefSpot);

	}

	public void updateRates() {
		System.out.println("CutrentSpoyyyy = "+currentSpot);
		
		double newSpot = QuantoUtility.randSpot( currentSpot,  discountFactor);
		this.currentSpot = newSpot; 
		String refSpot = String.format(fmt, currentSpot-(2* discountFactor)) +"/"+ String.format(fmt,currentSpot + (2 * discountFactor));
		Outputmap.put(outputKey4, refSpot);

		System.out.println("RefSpot = "+ refSpot);
		
		String markup1 = String.format(fmt, newSpot-(N1Spread * discountFactor)) +"/"+ String.format(fmt,newSpot+(N1Spread * discountFactor));
		String markup2 = String.format(fmt, newSpot-(N2Spread * discountFactor)) +"/"+ String.format(fmt,newSpot+(N2Spread * discountFactor));
		String markup3 = String.format(fmt, newSpot-(N3Spread * discountFactor)) +"/"+ String.format(fmt,newSpot+(N3Spread * discountFactor));
		String markup4 = String.format(fmt, newSpot-(N4Spread * discountFactor)) +"/"+ String.format(fmt,newSpot+(N4Spread * discountFactor));
		
		System.out.println("Markup1 = "+markup1);
		System.out.println("Markup2 = "+markup2);
		System.out.println("Markup3 = "+markup3);
		System.out.println("Markup4 = "+markup4);
		
		Outputmap.put(outputKey12, markup1);
		Outputmap.put(outputKey13, markup2);
		Outputmap.put(outputKey14, markup3);
		Outputmap.put(outputKey15, markup4);
		
	}

}
