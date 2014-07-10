/**
 * 
 */
package com.crm.product.impl;

import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;

/**
 * @author hungdt
 *
 */
public class PGOrderRoutingImpl extends OrderRoutingImpl {
	public CommandMessage parser(OrderRoutingInstance instance, ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		order = super.parser(instance, orderRoute, order);
		return order;
	}
}
