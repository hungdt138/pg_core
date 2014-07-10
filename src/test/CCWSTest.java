package test;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.crm.provisioning.impl.ccws.CCWSConnection;

public class CCWSTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		queryBalance();
	}
	public static void testRegex()
	{
		String text = "Ban da dang ky dich vu MaxiData truoc do tu ngay ~CUR_BALANCE_Core_AMOUNT~ den het ngay ~CUR_BALANCE_Core_EXPIRE~. Chuong trinh chi cho phep dang ky 1 lan 1 ngay. Xin cam on! ";
		
		Pattern pattern = Pattern.compile("~CUR_BALANCE_([a-zA-Z0-9_]+)~");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			String subStr = matcher.group().replace("~", "");
			String property = subStr.replace("CUR_BALANCE_", "");
			
			if (subStr.endsWith("EXPIRE"))
			{
				property = property.replace("_EXPIRE", "") + ".expireDate";
			}
			else if (subStr.endsWith("START")) 
			{
				property = property.replace("_START", "") + ".startDate";
			}
			else 
			{
				property = property.replace("_AMOUNT", "") + ".amount";
			}
			text = text.replace(matcher.group(), property);
			
		}
		
		System.out.println(text);
	}
	
	public static void queryBalance()
	{
		CCWSConnection connection = null;
		try
		{
			String host = "http://10.8.13.140/ccws/ccws.asmx";
			int port = 0;
			String userName = "NMS";
			String password = "nms!23";
			
			String isdn = "84922000514";

			connection = new CCWSConnection();
			connection.setHost(host);
			connection.setPort(port);
			connection.setUserName(userName);
			connection.setPassword(password);

			connection.openConnection();

			String outline = "";
			
			//String[] names = connection.getCCName("84922000514");
			
			//System.out.println(names.length);
			//for (int i = 0; i < names.length; i++)
			//{
			//	System.out.println(names[i]);
			//}
			
			BalanceEntity blcore = connection.getBalance(isdn, "Core");
			
			outline += "Core: " + String.valueOf(blcore.getBalance()) + "\r\n";
			//BalanceEntity blvm18 = connection.getBalance(isdn, "VM18");
			
			//outline += "VM18: " + String.valueOf(blvm18.getBalance()) + "\r\n";
			
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_MONTH, 30);
			//connection.deleteAlco("ALCO_MONTHLY_MAXI18", isdn);
			//connection.createAlco("ALCO_MONTHLY_MAXI18", isdn, end, start);
			
			System.out.println(outline);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (connection != null)
				try
				{
					connection.closeConnection();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
