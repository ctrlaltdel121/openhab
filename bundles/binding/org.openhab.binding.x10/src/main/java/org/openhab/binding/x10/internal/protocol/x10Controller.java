package org.openhab.binding.x10.internal.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * X10 Controller class. Implements communication with the X10
 * controller with serial communication.
 * 
 * @author Benji Arnold
 *
 */
public class x10Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(x10Controller.class);
	
	public x10Controller(String port) {
		logger.info("x10 Controller started");
	}
}
