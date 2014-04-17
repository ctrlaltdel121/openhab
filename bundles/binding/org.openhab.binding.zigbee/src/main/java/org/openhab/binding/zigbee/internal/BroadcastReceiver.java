package org.openhab.binding.zigbee.internal;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastReceiver extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastReceiver.class);
	
	private String port;
	private int baud_rate;
	private Item item;
	private boolean isRunning;
	private XBee xbee;
	
	zigbeeBinding binding;
	
	public BroadcastReceiver(String port, int baud_rate, Item item, zigbeeBinding bind) {
		this.port = port;
		this.baud_rate = baud_rate;
		this.binding = bind;
		this.item = item;
		this.isRunning = true;
		logger.trace("BroadcastReceiver created");
	}
	
	
	@Override
	public void run() {
		try {
			listen();
		} catch(XBeeException e) {
			logger.error(e.getMessage());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		if(xbee.isConnected()) {
			xbee.close();
		}
		logger.trace("BROADCAST RECEIVER THREAD DONE");
	} 
	
	public void listen() throws XBeeException {
		// ADC value from the xbee
		int digVal = 0;
		// Variables to parse analog value
		int index;
		String analogVal;
		
		Type currentType, oldType = OpenClosedType.CLOSED;
		
		xbee = new XBee();
		
		try {
			logger.trace("Trying to initialize with port: " + port + " and baud_rate: " + baud_rate);
			// replace with your com port and baud rate. this is the com port of my coordinator
			xbee.open(port, baud_rate);
			
			logger.trace("Initialized with port: " + port + " and baud_rate: " + baud_rate);
			
			while (isRunning) {				
				String response = xbee.getResponse().toString();
				//logger.trace("received response: " + response);
				
				index = response.indexOf("analog[");
				analogVal = response.substring(index + 10, response.length());
				
				digVal = Integer.parseInt(analogVal);
				
				MotionDetectorState mds;
				if(digVal > 500) {
					currentType = OpenClosedType.OPEN;
				} else {
					currentType = OpenClosedType.CLOSED;				
				}
				
				mds = new MotionDetectorState(currentType);
				
				if(currentType != oldType) {
					logger.trace("UPDATE STATE");
					binding.internalReceiveCommand(item.getName(), mds.getCommand());
					oldType = currentType;
				}
			}
		} finally {
			logger.trace("finally");
			xbee.close();
		}
	}
	
	public void stopThread() {
		this.isRunning = false;
	}
}
