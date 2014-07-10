/**
 * 
 */
package com.crm.provisioning.thread;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.cache.ProvisioningRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DatasourceInstance;
import com.crm.thread.DispatcherThread;

import com.fss.util.AppException;
import com.sun.messaging.jmq.jmsserver.multibroker.raptor.handlers.GetInterestUpdateHandler;

/**
 * @author ThangPV
 * 
 */
public class CommandRoutingInstance extends DatasourceInstance
{
	public CommandRoutingInstance() throws Exception
	{
		super();
	}

	public CommandRoutingThread getDispatcher()
	{
		return (CommandRoutingThread) dispatcher;
	}

	@Override
	public void doProcessSession() throws Exception
	{
		MQConnection connection = null;
		//QueueSession session = null;
		MessageProducer producer = null;

		ProvisioningEntry provisioning = null;
		CommandEntry command = null;

		Message message = null;
		CommandMessage request = null;

		Exception error = null;

		try
		{

			connection = getMQConnection();

			do
			{
				request = (CommandMessage) QueueFactory.detachCommandRouting();

				if (request == null)
				{
					break;
				}

				try
				{

					ProvisioningRoute route =
							((CommandRoutingThread) dispatcher)
									.getRoute(request.getProvisioningType(), "ISDN", request.getIsdn());

					if (route == null)
					{
						throw new AppException(Constants.ERROR_ROUTE_NOT_FOUND);
					}

					// forward request to related provisioning queue
					request.setProvisioningId(route.getProvisioningId());

					provisioning = ProvisioningFactory.getCache().getProvisioning(route.getProvisioningId());
					command = ProvisioningFactory.getCache().getCommand(request.getCommandId());

					if (provisioning == null)
					{
						throw new AppException(Constants.ERROR_PROVISIONING_NOT_FOUND);
					}
					else
					{
						String queueName = provisioning.getQueueName();

						if (queueName.equals(""))
						{
							queueName = ((CommandRoutingThread) dispatcher).queuePrefix + "/" + provisioning.getIndexKey();
						}

						try
						{
//							if (session == null)
//							{
//								session = connection.getSession();
//							}

							Queue queue = QueueFactory.getQueue(queueName);

							message = QueueFactory.createObjectMessage(connection.getSession(), request);

							if (producer == null)
							{
								producer = QueueFactory.createQueueProducer(connection.getSession(), null, 0, dispatcher.queuePersistent);
							}
							producer.send(queue, message);

							// message = connection.sendMessage(request,
							// queueName, 0, dispatcher.queuePersistent);

							/**
							 * Add log ISDN: PROVISIONING_ALIAS - COMMAND_ALIAS<br>
							 * NamTA<br>
							 * 21/08/2012
							 */
							if (provisioning != null && command != null && getDispatcher().displayDebug)
							{
								String isdn = request.getIsdn();
								ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
								if (product != null
										&& request.getKeyword().startsWith(Constants.INVITE_KEYWORD_SMS + product.getAlias()))
								{
									if (provisioning.getAlias().equals(Constants.PROVISIONING_OSA))
									{
										isdn = request.getParameters().getString("INVITER_ISDN");
										debugMonitor(isdn + ": " + provisioning.getAlias() + " - " + command.getAlias());
									}
									else if (provisioning.getAlias().equals(Constants.PROVISIONING_CCWS)
											|| provisioning.getAlias().equals(Constants.PROVISIONING_CRM))
									{
										isdn = request.getParameters().getString("INVITEE_ISDN");
										debugMonitor(isdn + ": " + provisioning.getAlias() + " - " + command.getAlias());
									}
									else
									{
										debugMonitor(isdn + ": " + provisioning.getAlias() + " - " + command.getAlias());
									}
								}
								else
								{
									debugMonitor(request.getIsdn() + ": " + provisioning.getAlias() + " - " + command.getAlias());
								}
							}

							// debugMonitor(request.toShortString());
						}
						catch (Exception e)
						{
							if(e instanceof JMSException)
							{
								error = e;
								connection.markError();
							}
							
							debugMonitor(e);

							int retryCounter = request.getRetryCounter();

							if (retryCounter < getDispatcher().maxRetryRouting)
							{
								request.setRetryCounter(retryCounter + 1);

								QueueFactory.attachCommandRouting(request);
							}
							else
							{
								request.setDescription("over-max-retry");
								debugMonitor(request.toLogString());

								debugMonitor(e);
							}
							
						}
					}
				}
				catch (Exception e)
				{
					error = e;
				}

				if (error != null)
				{
					request.setStatus(Constants.ORDER_STATUS_DENIED);

					if (error instanceof AppException)
					{
						request.setCause(error.getMessage());
						request.setDescription(((AppException) error).getContext());

						logMonitor(request);
					}
					else
					{
						request.setCause(Constants.ERROR);
						request.setDescription(error.getMessage());

						logMonitor(request);

						throw error;
					}
				}
			}
			while (isAvailable());
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			QueueFactory.closeQueue(producer);
			//QueueFactory.closeQueue(session);
			returnMQConnection(connection);
		}
	}
}
