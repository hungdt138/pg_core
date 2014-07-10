package com.crm.provisioning.impl.ccws;

import com.comverse_in.prepaid.ccws.*;
import org.apache.axis.AxisFault;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ServiceSoapStubEXT extends ServiceSoapStub
{
	public ServiceSoapStubEXT() throws org.apache.axis.AxisFault
	{
		super();
	}

	public ServiceSoapStubEXT(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
	{
		super(endpointURL, service);
	}

	public ServiceSoapStubEXT(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault
	{
		super(service);

	}

	public Object rechargeAccountBySubscriber(String strIsdn, String secretCode,
												String rechargeComment)
	{
		Map mpResponse;
		try
		{
			mpResponse = new LinkedHashMap();
			ArrayOfDeltaBalance LBalance = this.rechargeAccountBySubscriber(
					strIsdn, null, secretCode, rechargeComment);

			for (int i = 0; i < LBalance.getDeltaBalance().length; ++i)
			{
				DeltaBalance balance = LBalance.getDeltaBalance(i);
				mpResponse.put(balance.getBalanceName().toLowerCase(),
								String.valueOf(balance.getBalanceName()));
			}
			return mpResponse;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return ex;
		}
	}

}
