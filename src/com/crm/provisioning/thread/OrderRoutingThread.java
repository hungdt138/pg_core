/**
 * 
 */
package com.crm.provisioning.thread;

/**
 * @author ThangPV
 * 
 */
public class OrderRoutingThread extends ProvisioningThread
{
//	private HashMap<String, Object>	processingIsdn	= null;
//	private Object					lockedObject	= new Object();

	public OrderRoutingThread()
	{
		super();
	}

	/**
	 * Check duplicated processing. Key check: ProductId + Isdn
	 * 
	 * @param request
	 * @throws AppException
	 */
//	public void checkProcessing(CommandMessage request) throws AppException
//	{
//		synchronized (lockedObject)
//		{
//			if (request == null)
//				return;
//
//			String key = request.getProductId() + request.getIsdn();
//				
//			if (processingIsdn == null)
//			{
//				processingIsdn = new HashMap<String, Object>();
//				processingIsdn.put(key, request);
//				return;
//			}
//			if (processingIsdn.containsKey(key))
//			{
//				throw new AppException(Constants.ERROR_DUPLICATED);
//			}
//			else
//			{
//				processingIsdn.put(request.getIsdn(), request);
//			}
//		}
//	}

	/**
	 * Remove processing marker. Key check: ProductId + Isdn
	 * 
	 * @param request
	 */
//	public void removeProcessing(CommandMessage request)
//	{
//		synchronized (lockedObject)
//		{
//			if (processingIsdn != null && request != null)
//			{
//				String key = request.getProductId() + request.getIsdn();
//				processingIsdn.remove(key);
//			}
//		}
//	}
	
	@Override
	public void afterProcessSession() throws Exception
	{
		//processingIsdn = null;
		super.afterProcessSession();
	}
}
