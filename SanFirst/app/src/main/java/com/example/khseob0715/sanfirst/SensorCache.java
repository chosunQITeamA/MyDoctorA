package com.example.khseob0715.sanfirst;

public class SensorCache {
	public BioHarnessSessionData bioHarnessSessionData=new BioHarnessSessionData();;
	
	private static SensorCache instance = new SensorCache();
	public static SensorCache getInstance() {
		return instance;
	}	
	
}
