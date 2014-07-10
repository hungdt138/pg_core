package test;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis.Version;

import com.crm.kernel.message.Constants;
import com.fss.util.AppException;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String dateValidatePattern = "\\s((3[0-1])|(([0-2]*)([1-9])))(\\D)((1[0-2])|(0*[1-9]))$";

		String keyword = "DK TV 1 12";
		Pattern p = Pattern.compile(dateValidatePattern);
		Matcher matcher = p.matcher(keyword);
		
		String date = "";
		String month = "";
		
		if (matcher.find())
		{
			date = matcher.group(1);
			month = matcher.group(7);
			
			if (date.equals("") || month.equals(""))
			{
			}
			else
			{
				if (date.length() == 1)
					date = "0" + date;
				
				if (month.length() == 1)
					month = "0" + month;
				
				String birthdate = date + "/" + month;
			}
		}
		else
		{
			
		}
	}
}
