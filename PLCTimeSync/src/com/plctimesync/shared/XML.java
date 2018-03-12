package com.plctimesync.shared;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class XML {

	private final ResourceBundle resources = ResourceBundle.getBundle("resources.resources");

	public static void main(String[] args) {
		new XML();

	}

	public XML() {

		loadSettingsFromFile();
	}

	// @SuppressWarnings("unchecked")
	private void loadSettingsFromFile() {

		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File("Test.xml").getAbsolutePath());
			p.loadFromXML(fis);
			
			System.out.println(p);
			if (p.containsKey("user")) {
				System.out.println(p.getProperty("user"));
			}
			if (p.containsKey("serial")) {
				System.out.println(p.getProperty("serial"));
			} else {
				// chkAutoConnect.setSelected(false);
			}

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

			FileOutputStream fos = new FileOutputStream(new File("settings.xml").getAbsolutePath());
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
