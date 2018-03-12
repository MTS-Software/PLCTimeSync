package com.plctimesync.comm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import PLCCom.TCP_ISO_Device;
import PLCCom.ePLCType;

public class DevicesConfig {

	private static final Logger logger = Logger.getLogger(DevicesConfig.class);

	private JFrame frame;

	private PLCThread plcThread;
	private PLCTimeThread plcTimeThread;

	private PLC plc;
	private List<PLC> devices = new ArrayList<PLC>();

	public DevicesConfig(JFrame frame) {

		this.frame = frame;

		loadSettingsFromFile();

	}

	private void loadSettingsFromFile() {

		TCP_ISO_Device device;
		String id;
		String ip;
		String rack;
		String slot;
		String connectiontimeout;
		String readtimeout;
		String syncinterval = null;

		try {

			if (logger.isInfoEnabled()) {
				logger.info("Lade Konfiguration aus .xml");
			}

			File fXmlFile = new File("plc_config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("root element: " + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("syncinterval");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\ncurrent element: " + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					System.out.println("time: " + eElement.getAttribute("time"));
					syncinterval = eElement.getAttribute("time");

				}
			}

			nList = doc.getElementsByTagName("plc");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\ncurrent element: " + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					System.out.println(eElement.getNodeName());

					System.out.println("name: " + eElement.getAttribute("name"));
					id = eElement.getAttribute("name");

					System.out.println("ip: " + eElement.getElementsByTagName("ip").item(0).getTextContent());
					ip = eElement.getElementsByTagName("ip").item(0).getTextContent();

					System.out.println("rack: " + eElement.getElementsByTagName("rack").item(0).getTextContent());
					rack = eElement.getElementsByTagName("rack").item(0).getTextContent();

					System.out.println("slot: " + eElement.getElementsByTagName("slot").item(0).getTextContent());
					slot = eElement.getElementsByTagName("slot").item(0).getTextContent();

					System.out.println("connectiontimeout: "
							+ eElement.getElementsByTagName("connectiontimeout").item(0).getTextContent());
					connectiontimeout = eElement.getElementsByTagName("connectiontimeout").item(0).getTextContent();

					System.out.println(
							"readtimeout: " + eElement.getElementsByTagName("readtimeout").item(0).getTextContent());
					readtimeout = eElement.getElementsByTagName("readtimeout").item(0).getTextContent();

					if (!ip.isEmpty()) {

						device = new TCP_ISO_Device(ip, Integer.parseInt(rack), Integer.parseInt(slot),
								ePLCType.S7_300_400_compatibel);
						device.setConnecttimeout(Integer.parseInt(connectiontimeout));
						device.setReadTimeout(Integer.parseInt(readtimeout));

						plc = new PLC(id, device);

						devices.add(plc);

						// startThread(id, device, Integer.parseInt(syncinterval));
					}

				}
			}

			startTimeThread(devices, Integer.parseInt(syncinterval));

		} catch (Exception e) {

			if (logger.isInfoEnabled()) {
				logger.error(e.getMessage());
			}
			e.printStackTrace();
		}
	}

	private void startThread(String id, TCP_ISO_Device device, int syncinterval) {

		plcThread = new PLCThread(id, device, frame, syncinterval);
		Thread th = new Thread(plcThread);
		th.start();

	}

	private void startTimeThread(List<PLC> devices, int syncinterval) {

		plcTimeThread = new PLCTimeThread(devices, frame, syncinterval);
		Thread th = new Thread(plcTimeThread);
		th.start();

	}

}
