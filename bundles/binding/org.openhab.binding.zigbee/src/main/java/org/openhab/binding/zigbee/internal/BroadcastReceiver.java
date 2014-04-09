package org.openhab.binding.zigbee.internal;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastReceiver {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastReceiver.class);
	
	public BroadcastReceiver(String port, int baud_rate) throws XBeeException {
		
		logger.trace("BroadcastReceiver created");
		
		XBee xbee = new XBee();
		logger.trace("xbee object created");
		try {
			logger.trace("Trying to initialize with port: " + port + " and baud_rate: " + baud_rate);
			// replace with your com port and baud rate. this is the com port of my coordinator
			xbee.open(port, baud_rate);
			
			logger.trace("Initialized with port: " + port + " and baud_rate: " + baud_rate);
			
			while (true) {				
				XBeeResponse response = xbee.getResponse();
				logger.trace("received response " + response);
			}
		} finally {
			xbee.close();
		}
	
	}
}
