package com.crm.provisioning.impl.charging;

public class ExtDebitDataListener extends SubModifyListener
{
	@Override
	public void onReceive(byte[] data) throws Exception
	{
		String receiveData = new String(data);

		synchronized (lastData)
		{
			receiveData = lastData + receiveData;

			int endIndex = receiveData.indexOf(ExtDebitConnection.SEPARATE_CHARS);
			int startIndex = 0;
			while (endIndex > 0)
			{
				String receive = receiveData.substring(startIndex, endIndex);
				try
				{
					// System.out.println("received: " + receive);
					String command = ExtDebitConnection.getCommand(receive);
					if (((!command.equals(ExtDebitConnection.CMD_CREATE_SESSION))
							& (!command.equals(ExtDebitConnection.CMD_DEBIT)))
									| command.equals(""))
					{
						logMonitor(receive);
					}
					else
					{
						String seq = "";
						if (command.equals(ExtDebitConnection.CMD_CREATE_SESSION))
							seq = ExtDebitConnection.CMD_CREATE_SESSION;
						else
							seq = ExtDebitConnection.getFieldValue(ExtDebitConnection.FIELD_TRANSACTION_ID, receive);

						receive = receive.substring(receive.indexOf(ExtDebitConnection.SEPARATOR_RESPONSE_CMD_FIELD)
								+ ExtDebitConnection.SEPARATOR_RESPONSE_CMD_FIELD.length());
						
						Object notifyObject = notifyMap.remove(seq);
						if (seq.length() < receive.length())
						{
							logMonitor(receive);
							responseMap.put(seq, receive);
							if (notifyObject != null)
								synchronized (notifyObject)
								{
									notifyObject.notify();
								}
						}
					}
				}
				catch (Exception e)
				{
				}

				startIndex = endIndex + SubModifyConnection.SEPARATE_CHARS.length();

				endIndex = receiveData.indexOf(SubModifyConnection.SEPARATE_CHARS, startIndex);
			}

			lastData = receiveData.substring(startIndex);
		}
	}
}
