/**
 * 
 */
package com.crm.merchant.cache;

import com.crm.kernel.index.IndexNode;

/**
 * @author hungdt
 *
 */
public class MerchantEntry extends IndexNode {
	private long 			merchantId 		= 0;
	private String			alias			= "";
	private String 			username 		= "";
	private String			password 		= "";
	
	private String			host 			= "";
	private int				port			= 0;
	private String			serviceAddress 	= "";
	
	private int				status 			= 3;
	
	
	public MerchantEntry(long merchantId, String alias){
		super(alias);
		
		setMerchantId(merchantId);
	}
	
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getServiceAddress()
	{
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress)
	{
		this.serviceAddress = serviceAddress;
	}
	
	
		
}
