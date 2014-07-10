/**
 * 
 */
package com.crm.provisioning.message;

import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

/**
 * @author ThangPV
 * 
 */
public class VNMMessage extends CommandMessage
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8368953062175546989L;

	private SubscriberRetrieve	subscriberRetrieve	= null;

	public void copyTo(VNMMessage result)
	{
		super.copyTo(result);

		if (result != null)
		{
			result.setSubscriberRetrieve(subscriberRetrieve);
		}
	}

	public VNMMessage clone()
	{
		VNMMessage result = new VNMMessage();

		copyTo(result);

		return result;
	}

	public SubscriberRetrieve getSubscriberRetrieve()
	{
		return subscriberRetrieve;
	}

	public void setSubscriberRetrieve(SubscriberRetrieve subscriberRetrieve)
	{
		this.subscriberRetrieve = subscriberRetrieve;
	}

	public SubscriberEntity getSubscriberEntity()
	{
		return (subscriberRetrieve == null) ? null : subscriberRetrieve.getSubscriberData();
	}

	public void setSubscriberEntity(SubscriberEntity subscriberEntity)
	{
		if (subscriberRetrieve == null)
		{
			subscriberRetrieve = new SubscriberRetrieve();
		}
		
		subscriberRetrieve.setSubscriberData(subscriberEntity);
	}

}
