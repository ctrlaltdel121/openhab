/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.x10;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Benji Arnold
 * @since 1.4.0-SNAPSHOT
 */
public interface x10BindingProvider extends BindingProvider {

	/**
	 * Gets the item code for the specified item
	 * @param itemName	The item to get the code for
	 * @return	"<houseCode><unitCode>"
	 */
	public String getItemCode(String itemName);
}
