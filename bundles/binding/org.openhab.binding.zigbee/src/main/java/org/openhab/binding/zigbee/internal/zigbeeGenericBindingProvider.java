/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zigbee.internal;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.zigbee.zigbeeBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Benji Arnold
 * @since 1.4.0-SNAPSHOT
 */
public class zigbeeGenericBindingProvider extends AbstractGenericBindingProvider implements zigbeeBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(zigbeeGenericBindingProvider.class);
	
	private static final int DEFAULT_BAUD_RATE = 9600;
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "zigbee";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		logger.trace("processBindingConfiguration({}, {})", item.getName(), bindingConfig);
		super.processBindingConfiguration(context, item, bindingConfig);
		
		int b_rate;
		
		try {
			b_rate = Integer.parseInt(bindingConfig);
			if(baudRateIsValid(b_rate)) {
				zigbeeBindingConfig.setBaudRate(b_rate);
				zigbeeBindingConfig.setItem(item);
				//addBindingConfig(item, config);
			}
		} catch(NumberFormatException e) {
			logger.warn("Baud rate must be an integer. Using default baud_rate of " + DEFAULT_BAUD_RATE);
		}
				
	}
	
	private boolean baudRateIsValid(int b_rate) {
		int valid_baud_rates[] = {1200,2400,4800,9600,19200,38400,57600,115200,230400,460800,921600};
		
		if(!Arrays.asList(ArrayUtils.toObject(valid_baud_rates)).contains(b_rate)) {
			logger.warn("Invalid baud rate. Using default baud_rate of " + DEFAULT_BAUD_RATE);
			return false;
		}
		
		return true;
	}
	
	static class zigbeeBindingConfig implements BindingConfig {
		
		// The baud rate to connect to the xbee
		static private int baudRate = DEFAULT_BAUD_RATE;
		static private Item item;
		
		static public void setBaudRate(int b_rate) {
			baudRate = b_rate;
		}
		
		static public int getBaudRate() {
			return baudRate;
		}
		
		static public void setItem(Item i) {
			item = i;
		}
		
		static public Item getItem() {
			return item;
		}
		
	}
	

	
	
}
