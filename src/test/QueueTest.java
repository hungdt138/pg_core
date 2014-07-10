package test;

import javax.jms.Message;

import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;

public class QueueTest extends DispatcherThread
{
	@Override
	public void doProcessSession() throws Exception
	{
		// TODO Auto-generated method stub
		logMonitor("Starting to dequeue...");
		Message message = detachMessage();
		int count = 0;
		while (message != null && isAvailable())
		{
			ThreadUtil.sleep(this);
			
			count ++;
			message = detachMessage();
		}
		
		logMonitor("Dequeued " + count + "msgs.");
	}
}
