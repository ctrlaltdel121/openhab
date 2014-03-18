/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.x10.internal;

import java.util.Dictionary;

import org.openhab.binding.x10.x10BindingProvider;
import org.openhab.binding.x10.internal.protocol.x10Controller;
import org.openhab.binding.x10.internal.x10GenericBindingProvider.x10BindingConfig;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Benji Arnold
 * @since 1.4.0-SNAPSHOT
 */
public class x10Binding extends AbstractActiveBinding<x10BindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(x10Binding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the x10
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/**
	 * The serial port for the x10 controller
	 */
	private String port;
	
	/**
	 * The X10 controller
	 */
	private volatile x10Controller xController; 
	
	public x10Binding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "x10 Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		logger.trace("internalReceiveCommand itemName = " + itemName);
		logger.trace("internalReceiveCommand command = " + command.toString());
		
		
		for(x10BindingProvider provider: providers) {
			if(provider.providesBindingFor(itemName)) {
				logger.trace("Found a binding provider for item: " + itemName);
				
				String itemCode = provider.getItemCode(itemName);
				
				String c = "";
				
				if(command.toString().equals("ON")) {
					// turn the item on
					c = "./x10cmd rf " + itemCode + " on";
					
				} else if(command.toString().equals("OFF")) {
					// turn the item off
					c = "./x10cmd rf " + itemCode + " off";
				} else if(command.toString().equals("DECREASE")) {
					c = "./x10cmd rf " + itemCode + " dim";
				} else if(command.toString().equals("INCREASE")) {
					c = "./x10cmd rf " + itemCode + " bright";
				}
				StringBuffer output = new StringBuffer();
				 
				Process p;
				try {
					p = Runtime.getRuntime().exec(c);
					p.waitFor();
					BufferedReader reader = 
		                            new BufferedReader(new InputStreamReader(p.getInputStream()));
		 
		                        String line = "";			
					while ((line = reader.readLine())!= null) {
						output.append(line + "\n");
					}
		 
				} catch (Exception e) {
					logger.trace(e.getMessage());
				}
		 
				logger.trace("OUTPUT: " + output.toString());
			}
			
		}
		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// get the serial port
			String portString = (String) config.get("port");
			if(StringUtils.isNotBlank(portString)) {
				port = portString;
				logger.info("updated config: port = " + port);
				xController = new x10Controller(port);
			}

			setProperlyConfigured(true);
		}
	}
	

}
