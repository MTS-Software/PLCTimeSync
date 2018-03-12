package com.plctimesync;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.log4j.PropertyConfigurator;

import com.plctimesync.shared.CheckProgramActive;
import com.plctimesync.view.Frame;

public class Main {

	private final ResourceBundle resources = ResourceBundle.getBundle("com/plctimesync/resource.resources");
	private Properties properties = new Properties();

	public static void main(String[] args) {

		new Main();

	}

	public Main() {

		// CheckProgramActive active = new CheckProgramActive();
		// active.check_active();

		PropertyConfigurator.configure(getClass().getClassLoader().getResource("log4j.properties"));

		loadSettingsFromFile();

		new Frame(resources, properties);

	}

	private void loadSettingsFromFile() {

		try {
			FileInputStream fis = new FileInputStream(new File("settings.xml").getAbsolutePath());
			properties.loadFromXML(fis);

			System.out.println(properties);
			// if (properties.containsKey("user")) {
			// System.out.println(properties.getProperty("user"));
			// }
			// if (properties.containsKey("serial")) {
			// System.out.println(properties.getProperty("serial"));
			// } else {
			// // chkAutoConnect.setSelected(false);
			// }

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private void saveSettings() {
		try {
			// Write Settings
			Properties p = new Properties();
			p.setProperty("user", "Markus");
			p.setProperty("serial", "1234");

			FileOutputStream fos = new FileOutputStream(new File("Settings.xml").getAbsolutePath());
			p.storeToXML(fos, "Settings");
			fos.close();
			JOptionPane
					.showMessageDialog(
							null, resources.getString("successfully_saved") + System.getProperty("line.separator")
									+ "File: " + new File("Settings.xml").getAbsolutePath(),
							"", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		} catch (HeadlessException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}

}
