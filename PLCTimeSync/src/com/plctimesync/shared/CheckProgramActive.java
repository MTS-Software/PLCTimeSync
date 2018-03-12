package com.plctimesync.shared;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.JOptionPane;

public class CheckProgramActive {

	private static File f;
	private static FileChannel channel;
	private static FileLock lock;

	public void check_active() {
		try {
			f = new File("RingOnRequest.lock");
			// Check if the lock exist
			if (f.exists()) {
				// if exist try to delete it
				f.delete();

			}
			// Try to get the lock
			channel = new RandomAccessFile(f, "rw").getChannel();
			lock = channel.tryLock();
			if (lock == null) {
				// File is lock by other application
				JOptionPane.showMessageDialog(null, "Das Programm kann nur 1 Mal gestartet werden.");
				channel.close();
				System.exit(0);

				throw new RuntimeException("Only 1 instance of PLCTimeSync can run.");
			}
			// Add shutdown hook to release lock when application shutdown
			ShutdownHook shutdownHook = new ShutdownHook();
			Runtime.getRuntime().addShutdownHook(shutdownHook);

			// Your application tasks here..
			System.out.println("Running");

		} catch (IOException e) {
			throw new RuntimeException("Could not start process.", e);
		}

	}

	public static void unlockFile() {
		// release and delete file lock
		try {
			if (lock != null) {
				lock.release();
				channel.close();
				f.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ShutdownHook extends Thread {

		public void run() {
			unlockFile();
		}
	}

}