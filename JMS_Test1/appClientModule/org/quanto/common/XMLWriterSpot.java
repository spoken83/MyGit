package org.quanto.common;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.quanto.publishing.SpotData;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriterSpot {

	private SpotData spotData;
	private DOMSource xmlSource;
	private Document xmlDoc;
	private String outputMode; // file, topic

	public DOMSource getXML() {
		return xmlSource;
	}

	public Document getXMLDoc() {
		return xmlDoc;
	}

	public XMLWriterSpot(String outputMode, SpotData SD) {
		this.outputMode = outputMode;
		this.spotData = SD;

	}

	@SuppressWarnings("rawtypes")
	private void createInputSection(Element field) {
		// INPUTS
		Element inputs = xmlDoc.createElement("inputs");
		field.appendChild(inputs);

		//HashMap<String, String> hhmap = spotData.getInputMap();
		Set set = spotData.Inputmap.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			//System.out.print("key is: " + mentry.getKey() + " & Value is: ");
			//System.out.println(mentry.getValue());
			
			Element input = xmlDoc.createElement("input");
			input.setAttribute("stbName", (String) mentry.getKey());
			input.setAttribute("value", (String) mentry.getValue());
			inputs.appendChild(input);

		}
		/*
		// input elements
		Element input = xmlDoc.createElement("input");
		input.setAttribute("stbName", "Spot-Fwd.1.Instrument");
		input.setAttribute("value", "AUD/USD");
		Element input2 = xmlDoc.createElement("input");
		input2.setAttribute("stbName", "Spot-Fwd.1.Maturity period");
		input2.setAttribute("value", "SPOT");

		inputs.appendChild(input);
		inputs.appendChild(input2);
		*/
	}

	@SuppressWarnings("rawtypes")
	private void createOutputSection(Element field) {

		// OUTPUTS
		Element outputs = xmlDoc.createElement("outputs");
		field.appendChild(outputs);

		Set set = spotData.Outputmap.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			//System.out.print("key is: " + mentry.getKey() + " & Value is: ");
			//System.out.println(mentry.getValue());
			
			Element output = xmlDoc.createElement("output");
			output.setAttribute("stbName", (String) mentry.getKey());
			output.setAttribute("value", (String) mentry.getValue());
			outputs.appendChild(output);

		}		
		
		/*
		// output elements
		Element output = xmlDoc.createElement("output");
		output.setAttribute("stbName", "Spot-Fwd.1.Instrument");
		output.setAttribute("value", "AUD/USD");
		Element output2 = xmlDoc.createElement("output");
		output2.setAttribute("stbName", "Swap Points");
		output2.setAttribute("value", "0.0");

		outputs.appendChild(output);
		outputs.appendChild(output2);
		*/
	}

	public void createXMLMessage() { // (String[][] inputAR, String[][] outputAR) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			xmlDoc = docBuilder.newDocument();
			Element rootElement = xmlDoc.createElement("field");
			rootElement.setAttribute("stbName", "Spot-Fwd.1.Maturity period");
			rootElement.setAttribute("value", "SPOT");

			xmlDoc.appendChild(rootElement);

			Element field = xmlDoc.createElement("field");
			field.setAttribute("stbName", "Spot-Fwd.1.Instrument");
			field.setAttribute("value", "AUD/USD");

			rootElement.appendChild(field);

			createInputSection(field);

			createOutputSection(field);

			xmlSource = new DOMSource(xmlDoc);

			StreamResult result = null;
			if (outputMode == "file") {
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();

				result = new StreamResult(new File("D:\\file.xml"));
				transformer.transform(xmlSource, result);
				System.out.println("File saved!");
			} else {
				result = new StreamResult(System.out); // Output to console for
														// testing
				System.out.println("Done");

			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	/*
	public static void main(String argv[]) {
		System.out.println("Start!");
		SpotData EURUSD = new SpotData("EUR/USD", 1.1212, 0.0001);

		XMLWriterSpot tester = new XMLWriterSpot("file", EURUSD);

		tester.createXMLMessage(); // dummy1, dummy1);
	}
	*/

}