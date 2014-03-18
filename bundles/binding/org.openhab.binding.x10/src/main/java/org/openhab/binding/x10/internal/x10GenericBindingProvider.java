/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.x10.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.x10.x10BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
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
public class x10GenericBindingProvider extends AbstractGenericBindingProvider implements x10BindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(x10GenericBindingProvider.class);
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "x10";
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
		
		x10BindingConfig config = new x10BindingConfig(item);
		
		// Validate the bindingConfig
		String houseCode, unitCode;
		
		if(bindingConfig.length() == 2 || bindingConfig.length() == 3) {
			houseCode = bindingConfig.substring(0, 1);
			unitCode = bindingConfig.substring(1);
			
			if(validateBindingConfig(houseCode, unitCode, item.getName())) {
				config.addItem(houseCode, unitCode);
				
				addBindingConfig(item, config);
			}
			
		} else {
			logger.error("Invalid bindingConfig for item: " + item.getName());
		}
				
	}
	
	private boolean validateBindingConfig(String houseCode, String unitCode, String itemName) {
		String[] validHouseCodes = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p"};
		int[] validUnitCodes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		int unit = 0;
		
		if(!Arrays.asList(validHouseCodes).contains(houseCode)) {
			logger.error("Invalid house code for item: " + itemName);
			return false;
		}
		
		try {
			unit = Integer.parseInt(unitCode);
		} catch(NumberFormatException e) {
			logger.error("Invalid unit code for item: " + itemName);
			return false;
		}
		
		if(!Arrays.asList(ArrayUtils.toObject(validUnitCodes)).contains(unit)) {
			logger.error("Invalid unit code for item: " + itemName);
			return false;
		}
		
		return true;
	}
	
	class x10BindingConfig implements BindingConfig {
		
		// The item that this configuration represents
		private Item item;
		// The house code for the item
		private String houseCode;
		// The unit code for the item
		private String unitCode;
		
		public x10BindingConfig(Item item) {
			super();
			this.item = item;
		}
		
		public void addItem(String houseCode, String unitCode) {
			this.houseCode = houseCode;
			this.unitCode = unitCode;
		}
		
		public String getItemCode() {
			return this.houseCode + this.unitCode;
		}
		
		public Item getItem() {
			return this.item;
		}
	}

	@Override
	public String getItemCode(String itemName) {
		x10BindingConfig config = (x10BindingConfig)bindingConfigs.get(itemName);
		return config.getItemCode();
	}
	

	
	
}
