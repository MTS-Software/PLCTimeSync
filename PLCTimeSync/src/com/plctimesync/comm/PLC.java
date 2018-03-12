package com.plctimesync.comm;

import PLCCom.TCP_ISO_Device;

public class PLC {

	private String name;
	private TCP_ISO_Device device;

	public PLC(String name, TCP_ISO_Device device) {
		this.name = name;
		this.device = device;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TCP_ISO_Device getDevice() {
		return device;
	}

	public void setDevice(TCP_ISO_Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "PLC [name=" + name + ", device=" + device + "]";
	}
	
	
	

}
