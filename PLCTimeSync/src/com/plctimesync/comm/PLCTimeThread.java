package com.plctimesync.comm;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.plctimesync.view.Frame;

import PLCCom.BasicInfoResult;
import PLCCom.ConnectResult;
import PLCCom.ReadResult;
import PLCCom.TCP_ISO_Device;
import PLCCom.WriteResult;
import PLCCom.authentication;

public class PLCTimeThread implements Runnable {

	private static final Logger logger = Logger.getLogger(PLCTimeThread.class);

	private List<PLC> devices;
	private JFrame frame;
	private int interval;

	private String log;

	public PLCTimeThread(List<PLC> devices, JFrame frame, int interval) {

		this.devices = devices;
		this.interval = interval;
		this.frame = frame;

	}

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			for (PLC plc : this.devices) {

				log = plc.getName() + "/" + plc.getDevice().getIPAdress() + "\n";

				if (!plc.getDevice().IsConnected()) {
					connect(plc);
				}
				if (plc.getDevice().IsConnected()) {
					getPLCTime(plc);
					setPLCTime(plc);
					getPLCTime(plc);
				}

				((Frame) frame).addText(log + "\n");
			}

			try {
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {

				if (logger.isInfoEnabled()) {
					logger.error(e.getMessage());
				}

				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}

	}

	private void connect(PLC plc) {

		authentication.Serial("86471-35181-102135-2966975");
		authentication.User("magna");

		ConnectResult res = plc.getDevice().Connect();

		if (!res.HasConnected()) {

			String text = "[connect] " + res.Message();
			log = log + text + "\n";

			if (logger.isInfoEnabled()) {
				logger.info(plc.getName() + "/" + plc.getDevice().getIPAdress() + text);
			}
		}

	}

	private void setPLCTime(PLC plc) {

		Calendar now = new GregorianCalendar();
		now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - 0);
		WriteResult res = plc.getDevice().SetPLCTime(now);

		String text = "[set plc time] " + res.Message();
		log = log + text + "\n";

		if (logger.isInfoEnabled())
			logger.info(plc.getName() + "/" + plc.getDevice().getIPAdress() + text);

	}

	private void getPLCTime(PLC plc) {
		ReadResult res = plc.getDevice().GetPLCTime();

		if (res.HasWorked() == true) {
			SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Calendar c = res.get_DATE_AND_TIME();

			String text = "[get plc time] " + SDF.format(c.getTime());

			log = log + text + "\n";

			if (logger.isInfoEnabled())
				logger.info(plc.getName() + "/" + plc.getDevice().getIPAdress() + text);

		}
	}

	private void getPLCInfo(PLC plc) {

		try {
			BasicInfoResult res = plc.getDevice().GetBasicInfo();

			if (res.HasWorked() == true) {
				System.out.println(res.Name());

				String text = "[get plc info] " + res.Ordernummer();

				log = log + text + "\n";

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getPLCDiagnose(PLC plc) {

		String res = plc.getDevice().getDiagnosestring();

		String text = "[get plc diagnostic] " + res;

		log = log + text + "\n";

	}

}
