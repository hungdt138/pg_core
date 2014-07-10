package com.crm.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jms.Message;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.crm.kernel.queue.QueueFactory;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

/**
 * 
 * @author Nam<br>
 *         Created Date 26/08/2012
 * 
 */
public class MailThread extends DispatcherThread
{
	private String		host			= "";
	private int			port			= 0;
	private String		username		= "";
	private String		password		= "";
	private String		sender			= "";
	private String		recipients		= "";
	private String		subject			= "";
	private String		contentType		= "";

	private boolean		authenticate	= false;
	private Session		mailSession		= null;
	private Transport	transport		= null;

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public String getSender()
	{
		return sender;
	}

	public void setRecipients(String recipients)
	{
		this.recipients = recipients;
	}

	public String getRecipients()
	{
		return recipients;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setAuthenticate(boolean authenticate)
	{
		this.authenticate = authenticate;
	}

	public boolean isAuthenticate()
	{
		return authenticate;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getContentType()
	{
		return contentType;
	}

	@Override
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();
		vtReturn.add(ThreadUtil.createTextParameter("host", 500,
				"Mail host."));
		vtReturn.add(ThreadUtil.createIntegerParameter("port",
				"Mail port."));
		vtReturn.add(ThreadUtil.createBooleanParameter("useAuthenticate",
				"Use authenticate."));
		vtReturn.add(ThreadUtil.createTextParameter("username", 100,
				"Mail username."));
		vtReturn.add(ThreadUtil.createPasswordParameter("password",
				"Mail password."));
		vtReturn.add(ThreadUtil.createTextParameter("sender", 500,
				"Mail Sender."));
		vtReturn.add(ThreadUtil.createTextParameter("recipients", 500,
				"Mail Recipients."));
		vtReturn.add(ThreadUtil.createTextParameter("subject", 100,
				"Mail Subject."));
		vtReturn.add(ThreadUtil.createTextParameter("contentType", 100,
				"Mail content type, default text/plain."));
		vtReturn.addAll(super.getDispatcherDefinition());

		return vtReturn;
	}

	@Override
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();
			setHost(ThreadUtil.getString(this, "host", false, ""));
			setPort(ThreadUtil.getInt(this, "port", 25));
			setAuthenticate(ThreadUtil.getBoolean(this, "useAuthenticate", false));
			setUsername(ThreadUtil.getString(this, "username", false, ""));
			setPassword(ThreadUtil.getString(this, "password", false, ""));
			setSender(ThreadUtil.getString(this, "sender", false, ""));
			setRecipients(ThreadUtil.getString(this, "recipients", false, ""));
			setSubject(ThreadUtil.getString(this, "subject", false, ""));
			setContentType(ThreadUtil.getString(this, "contentType", false, "text/plain"));
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
		}
	}

	@Override
	public void doProcessSession() throws Exception
	{
		try
		{
			while (isAvailable())
			{
				Message message = detachMessage();
				while (isAvailable() && message != null)
				{
					try
					{

						Object request = QueueFactory.getContentMessage(message);

						processMessage(request);
					}
					catch (Exception e)
					{
						debugMonitor(e);
						throw e;
					}

					message = detachMessage();
				}

				ThreadUtil.sleep(this);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		try
		{
			super.beforeProcessSession();
			initMailSession();
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			detroyMailSessioin();
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	/**
	 * Format mail content.
	 * 
	 * @param request
	 * @return
	 */
	public String formatContent(Object request)
	{
		return request.toString();
	}

	/**
	 * Format mail subject.
	 * 
	 * @param request
	 * @return
	 */
	public String formatSubject(Object request)
	{
		return StringUtil.format(new Date(), getSubject());
	}

	public void processMessage(Object request) throws Exception
	{
		String strContent = formatContent(request);

		String strSubject = formatSubject(request);

		sendEmail(strSubject, strContent, "");
	}

	public void initMailSession() throws Exception
	{
		if (mailSession == null)
		{
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", getHost());
			props.put("mail.smtp.port", getPort());
			props.put("mail.smtp.auth", isAuthenticate() ? "true" : "false");

			if (isAuthenticate())
			{
				SMTPAuthenticator auth = new SMTPAuthenticator(getUsername(), getPassword());
				mailSession = Session.getInstance(props, auth);
			}
			else
			{
				mailSession = Session.getInstance(props);
			}
			
			transport = mailSession.getTransport();
			transport.connect();
		}
	}
	
	public void detroyMailSessioin() throws MessagingException
	{
		try
		{
			if (transport != null)
				transport.close();
		}
		finally
		{
			mailSession = null;
			transport = null;
		}
	}

	public void sendEmail(String strSubject, String strContent, String strFileName) throws Exception
	{
		try
		{
			MimeMessage mailMessage = new MimeMessage(mailSession);
			mailMessage.setFrom(new InternetAddress(getSender().toLowerCase()));

			ArrayList<InternetAddress> recipients = new ArrayList<InternetAddress>();

			String[] strRecipientList = StringUtil.toStringArray(getRecipients(), ";");

			for (int j = 0; j < strRecipientList.length; j++)
			{
				if (!strRecipientList[j].trim().equals(""))
				{
					boolean blnExists = false;
					for (int k = 0; (k < j) && !blnExists; k++)
					{
						blnExists = strRecipientList[j].equals(strRecipientList[k]);
					}
					if (!blnExists)
					{
						InternetAddress address = new InternetAddress(strRecipientList[j]);
						mailMessage.addRecipient(javax.mail.Message.RecipientType.TO, address);
						recipients.add(address);
					}
				}
			}

			if (recipients.size() == 0)
			{
				debugMonitor("Invalid receipients list.");
			}
			
			mailMessage.setSubject(strSubject);
			// mailMessage.setContent(strContent, getContentType());
			Address[] addresses = new Address[recipients.size()];

			recipients.toArray(addresses);

			// Create part 1 of mail (only text content)
			BodyPart mailBody = new MimeBodyPart();
			
			mailBody.setContent(strContent, getContentType());
			
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(mailBody);
			if (strFileName != null)
			{
				// Create part 2 of mail (attach file )
				String arrFileName[] = strFileName.split(";");
				FileDataSource fds = null;
				
				for (int i = 0; i < arrFileName.length; i++)
				{
					if (arrFileName[i].equals(""))
						continue;
					fds =  new FileDataSource(arrFileName[i]);
					mailBody =  new MimeBodyPart();
					if (fds != null)
					{
						mailBody.setDataHandler(new DataHandler(fds));
						mailBody.setFileName(fds.getName());
						multiPart.addBodyPart(mailBody);
					}
				}
			}
			
			mailMessage.setContent(multiPart, getContentType());
			mailMessage.saveChanges();
			
			debugMonitor("Send mail [" + strSubject + "] to [" + getRecipients() + "] from [" + getSender() + "].");

			transport.sendMessage(mailMessage, addresses);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		}
	}
	
	public void sendEmail(String strSubject, String strSender, String sreRecipients, String strContent, String strFileName) throws Exception
	{
		try
		{
			MimeMessage mailMessage = new MimeMessage(mailSession);
			mailMessage.setFrom(new InternetAddress(strSender.toLowerCase()));

			ArrayList<InternetAddress> recipients = new ArrayList<InternetAddress>();

			String[] strRecipientList = StringUtil.toStringArray(sreRecipients, ";");

			for (int j = 0; j < strRecipientList.length; j++)
			{
				if (!strRecipientList[j].trim().equals(""))
				{
					boolean blnExists = false;
					for (int k = 0; (k < j) && !blnExists; k++)
					{
						blnExists = strRecipientList[j].equals(strRecipientList[k]);
					}
					if (!blnExists)
					{
						InternetAddress address = new InternetAddress(strRecipientList[j]);
						mailMessage.addRecipient(javax.mail.Message.RecipientType.TO, address);
						recipients.add(address);
					}
				}
			}

			if (recipients.size() == 0)
			{
				debugMonitor("Invalid receipients list.");
			}
			
			mailMessage.setSubject(strSubject);
			// mailMessage.setContent(strContent, getContentType());
			Address[] addresses = new Address[recipients.size()];

			recipients.toArray(addresses);

			// Create part 1 of mail (only text content)
			BodyPart mailBody = new MimeBodyPart();
			
			mailBody.setContent(strContent, getContentType());
			
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(mailBody);
			if (strFileName != null)
			{
				// Create part 2 of mail (attach file )
				String arrFileName[] = strFileName.split(";");
				FileDataSource fds = null;
				
				for (int i = 0; i < arrFileName.length; i++)
				{
					if (arrFileName[i].equals(""))
						continue;
					fds =  new FileDataSource(arrFileName[i]);
					mailBody =  new MimeBodyPart();
					if (fds != null)
					{
						mailBody.setDataHandler(new DataHandler(fds));
						mailBody.setFileName(fds.getName());
						multiPart.addBodyPart(mailBody);
					}
				}
			}
			
			mailMessage.setContent(multiPart, getContentType());
			mailMessage.saveChanges();
			
			debugMonitor("Send mail [" + strSubject + "] to [" + getRecipients() + "] from [" + getSender() + "].");

			transport.sendMessage(mailMessage, addresses);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		}
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator
	{
		private String	username;
		private String	password;

		public SMTPAuthenticator(String username, String password)
		{
			super();
			this.username = username;
			this.password = password;
		}

		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(username, password);
		}
	}
}
