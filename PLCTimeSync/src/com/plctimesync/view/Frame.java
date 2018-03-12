package com.plctimesync.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.plctimesync.comm.DevicesConfig;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class Frame extends JFrame {

	private final ResourceBundle resources;
	private Properties properties = new Properties();

	private DevicesConfig device;
	public static TrayIcon trayIcon;
	public static final String VERSION = "1.9";

	private JTextArea txtTrace;

	public Frame(ResourceBundle resources, Properties properties) {

		this.resources = resources;
		this.properties = properties;

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		
		setTitle(resources.getString("programm_name"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 400));
		initSystemTray();
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/plctimesync/resource/time32.png")));

		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BorderLayout(0, 0));

		JPanel pnlHeader = new JPanel();
		pnlHeader.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlHeader.setBackground(Color.WHITE);
		FlowLayout fl_pnlHeader = (FlowLayout) pnlHeader.getLayout();
		fl_pnlHeader.setAlignment(FlowLayout.LEFT);
		pnlCenter.add(pnlHeader, BorderLayout.NORTH);

		JLabel lblHeader = new JLabel("Trace");
		pnlHeader.add(lblHeader);
		lblHeader.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JScrollPane scrollPane = new JScrollPane(pnlCenter);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel pnlText = new JPanel();
		pnlText.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlCenter.add(pnlText, BorderLayout.CENTER);
		pnlText.setLayout(new BorderLayout(0, 0));
		txtTrace = new JTextArea();
		pnlText.add(txtTrace);
		txtTrace.setEditable(false);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				if (properties.containsKey("zeige_trayinfos")) {
					if (properties.getProperty("zeige_trayinfos").equalsIgnoreCase("true")) {
						trayIcon.displayMessage(resources.getString("programm_name"),
								resources.getString("programm_minimiert"), TrayIcon.MessageType.INFO);
					}

				}

				txtTrace.setText("");
				super.windowClosing(e);
			}

		});

		JMenuBar menuBar = new JMenuBar();

		JMenu mnuDatei = new JMenu("Datei");
		menuBar.add(mnuDatei);

		JMenuItem mnuBeenden = new JMenuItem("Beenden");
		mnuDatei.add(mnuBeenden);
		mnuBeenden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		JMenu mnuHilfe = new JMenu("Hilfe");
		menuBar.add(mnuHilfe);

		JMenuItem mnuAbout = new JMenuItem("About");
		mnuHilfe.add(mnuAbout);
		mnuAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Frame.this,
						"Entwickler: " + resources.getString("entwickler") + "\nVersion: " + VERSION);

			}
		});

		setJMenuBar(menuBar);

		if (properties.containsKey("starte_minimiert")) {
			if (properties.getProperty("starte_minimiert").equalsIgnoreCase("false")) {
				setVisible(true);
			}

		}

		if (properties.containsKey("zeige_trayinfos")) {
			if (properties.getProperty("zeige_trayinfos").equalsIgnoreCase("true")) {
				trayIcon.displayMessage(resources.getString("programm_name"), resources.getString("programm_gestartet"),
						TrayIcon.MessageType.INFO);
			}

		}

		device = new DevicesConfig(this);

	}

	private void initSystemTray() {

		Image image = Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource("/com/plctimesync/resource/time32.png"));

		PopupMenu popup = new PopupMenu();

		MenuItem item = new MenuItem("Öffnen");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame.this.setVisible(true);
			}
		});
		popup.add(item);

		item = new MenuItem("Beenden");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});

		popup.add(item);

		trayIcon = new TrayIcon(image, resources.getString("programm_name"), popup);
		trayIcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Frame.this.setVisible(true);
				super.mouseClicked(e);
			}
		});
		trayIcon.setImageAutoSize(true);

		try {
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);

		} catch (AWTException e1) {
			e1.printStackTrace();
		}

	}

	public void addText(String text) {

		if (isVisible()) {
			txtTrace.append(text);
		}
	}

}
