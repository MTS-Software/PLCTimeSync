package com.plctimesync.comm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.plctimesync.view.Frame;

import PLCCom.ConnectResult;
import PLCCom.ReadResult;
import PLCCom.TCP_ISO_Device;
import PLCCom.WriteResult;
import PLCCom.authentication;

public class PLCThread implements Runnable {

	private static final Logger logger = Logger.getLogger(PLCThread.class);

	private TCP_ISO_Device device;
	private JFrame frame;
	private String name;
	private int interval;

	private String log;

	public PLCThread(String name, TCP_ISO_Device device, JFrame frame, int intervall) {

		this.name = name;
		this.interval = intervall;
		this.device = device;
		this.frame = frame;

		connect();

		log = name + "; ip: " + device.getIPAdress() + " ";

	}

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			if (device.IsConnected()) {

				getPLCTime();

				setPLCTime();

			} else
				connect();

			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {

				if (logger.isInfoEnabled()) {
					logger.error(e.getMessage());
				}

				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}

	}

	private void connect() {

		authentication.Serial("86471-35181-102135-2966975");
		authentication.User("magna");
		ConnectResult res = device.Connect();

		if (!res.HasConnected()) {

			String text = log + "[connect] " + res.Message();

			if (logger.isInfoEnabled()) {
				logger.info(text);
			}
			((Frame) frame).addText(text + "\n");
		}

	}

	private void setPLCTime() {
		Calendar now = new GregorianCalendar();
		now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - 0);
		WriteResult res = device.SetPLCTime(now);

		String text = log + "[set plc time] " + res.Message();

		if (logger.isInfoEnabled())
			logger.info(text);

		((Frame) frame).addText(text + "\n");

	}

	private void getPLCTime() {
		ReadResult res = device.GetPLCTime();

		if (res.HasWorked() == true) {
			SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Calendar c = res.get_DATE_AND_TIME();

			String text = log + "[get plc time] " + SDF.format(c.getTime());

			if (logger.isInfoEnabled())
				logger.info(text);

			((Frame) frame).addText(text + "\n");

		}
	}

}
