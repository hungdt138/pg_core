package com.crm.provisioning.impl.vncs;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.crm.provisioning.cache.ProvisioningConnection;

public class VNCSConnection extends ProvisioningConnection
{
	private BufferedReader output = null;
	private HttpsURLConnection conn = null;
	private URL url = null;

	public VNCSConnection()
	{
		setHost("10.8.39.68");
		setPort(443);
		setPassword("vncs@d");
	}

	private URL getURL(String host, int port, String keyword, String secret,
			String isdn) throws Exception
	{
		String strUrl = "https://" + getHost() + "/vas/vasman.php";

		String params = "?keyword=" + URLEncoder.encode(keyword, "UTF-8")
				+ "&secret=" + URLEncoder.encode(secret, "UTF-8") + "&msisdn="
				+ URLEncoder.encode(isdn, "UTF-8");

		URL url = new URL(strUrl + params);
		return url;
	}
	
	private void connect() throws Exception
	{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType)
			{
				// Trust always
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType)
			{
				// Trust always
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		// Create empty HostnameVerifier
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1)
			{
				return true;
			}
		};

		sc.init(null, trustAllCerts, new java.security.SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		this.conn = (HttpsURLConnection) this.url.openConnection();
		
		this.conn.setRequestProperty("Content-Type",
				"text/plain; charset=\"utf8\"");
		this.conn.setDoOutput(true);
		this.conn.setRequestMethod("GET");
		this.conn.setUseCaches(false);
		this.conn.setConnectTimeout((int) getTimeout());
		this.conn.connect();
	}

	public String sendGetRequest(String keyword, String isdn) throws Exception
	{
		String result = null;
		try
		{
			this.url = getURL(getHost(), getPort(), keyword,
					encryptMD5(getPassword() + isdn), isdn);

			connect();
			
			this.output = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
			
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = this.output.readLine()) != null)
			{
				sb.append(line);
			}

			result = sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				this.conn.disconnect();
			}
			catch (Exception e)
			{
			}
			
			try
			{
				this.conn.disconnect();
			}
			catch (Exception e)
			{
			}
			
		}

		return result;
	}

	public String encryptMD5(String message) throws Exception
	{
		String digest = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			// converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash)
			{
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
		}
		catch (Exception e)
		{
			throw e;
		}
		return digest;
	}

	public static void main(String[] args) throws Exception
	{
		try
		{
			VNCSConnection conn = new VNCSConnection();
			String response = conn.sendGetRequest("unregister", "84922000512");
			System.out.println(response);
//			response = conn.sendGetRequest("changehandset", "84922000512");
//			System.out.println(response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
