package com.crm.provisioning.thread;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.crm.kernel.sql.Database;
import com.crm.thread.MailThread;
import com.crm.util.StringUtil;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class SendMailReportVB extends MailThread
{
	protected String folderPath;
	protected String SQLSubscription = "";
	protected String SQLUnSubscription = "";
	protected String SQLRenew = "";
	protected String productList = "";
	protected String content = "";

	protected PreparedStatement _stmtSubscription = null;
	protected PreparedStatement _stmtUnSubscription = null;
	protected PreparedStatement _stmtRenewal = null;
	protected PreparedStatement _stmtUpdate = null;

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private Connection connection = null;
	{

	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("FolderPath", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("SQLSubscription", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("SQLUnsubscription", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("SQLRenew", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("ProductList", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("MailContent", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		
		vtReturn.addAll(super.getParameterDefinition());
		
		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();
	
			// Fill parameter
			folderPath = loadMandatory("FolderPath");
			SQLSubscription = loadMandatory("SQLSubscription");
			SQLUnSubscription = loadMandatory("SQLUnsubscription");
			SQLRenew = loadMandatory("SQLRenew");
			productList = loadMandatory("ProductList");
			content = loadMandatory("MailContent");
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			String strSQL = "Update subscriberproduct set status = 1 where subproductid = ?";
			_stmtUpdate = connection.prepareStatement(strSQL);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(_stmtSubscription);
			Database.closeObject(_stmtUnSubscription);
			Database.closeObject(_stmtRenewal);
			Database.closeObject(_stmtUpdate);
			Database.closeObject(connection);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	public void doProcessSession() throws Exception
	{
		try
		{
			File file = new File(folderPath);
			if (!file.exists())
			{
				file.mkdirs();
			}

			String subscriptionFilePath = "";
			String unsubscriptionFilePath = "";
			String renewalFilePath = "";
			if (folderPath.endsWith("/"))
			{
				subscriptionFilePath = folderPath + "SubscriptionList.xls";
				unsubscriptionFilePath = folderPath + "UnsubscriptionList.xls";
				renewalFilePath = folderPath + "RenewalList.xls";
			}
			else
			{
				subscriptionFilePath = folderPath + "/SubscriptionList.xls";
				unsubscriptionFilePath = folderPath + "/UnsubscriptionList.xls";
				renewalFilePath = folderPath + "/RenewalList.xls";
			}
			
			String[] arrProduct = StringUtil.toStringArray(productList, ";");
			String[] arrSubject = StringUtil.toStringArray(getSubject(), ";");
			String[] arrSender = StringUtil.toStringArray(getSender(), ";");
			for (int i=0; i<arrProduct.length; i++)
			{
				String strSQL = SQLSubscription;
				_stmtSubscription = connection.prepareStatement(strSQL);
				_stmtSubscription.setLong(1, Long.parseLong(arrProduct[i]));
				writeExcelFile(subscriptionFilePath, "Subscription List",
						"Subscription List", "No.,Subscription Date,A-number",
						_stmtSubscription);
				
				strSQL = SQLUnSubscription;
				_stmtUnSubscription = connection.prepareStatement(strSQL);
				_stmtUnSubscription.setLong(1, Long.parseLong(arrProduct[i]));
				writeExcelFile(unsubscriptionFilePath, "Unsubscription List",
						"Unsubscription List", "No.,Unsubscription Date,A-number",
						_stmtUnSubscription);
				
				strSQL = SQLRenew;
				_stmtRenewal = connection.prepareStatement(strSQL);
				_stmtRenewal.setLong(1, Long.parseLong(arrProduct[i]));
				writeExcelFile(renewalFilePath, "Renewal List", "Renewal List",
						"No.,Renew Date,A-number", _stmtRenewal);
				
				// Send mail
				Date date = new Date();
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH");
	
				String strFileName = subscriptionFilePath + ";"
						+ unsubscriptionFilePath + ";" + renewalFilePath;

				sendEmail(arrSubject[i] + df.format(date), arrSender[i], getRecipients(), content, strFileName);
			}
			
			storeConfig();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
		}
	}

	public void writeExcelFile(String outputFile, String sheetName,
			String strHeader, String strSubHeader, PreparedStatement obj)
			throws IOException, WriteException
	{
		try
		{
			File file = new File(outputFile);
			WorkbookSettings wbSettings = new WorkbookSettings();

			wbSettings.setLocale(new Locale("en", "EN"));

			WritableWorkbook workbook = Workbook.createWorkbook(file,
					wbSettings);
			workbook.createSheet(sheetName, 0);
			WritableSheet excelSheet = workbook.getSheet(0);
			createLabel(excelSheet, strHeader, strSubHeader);
			createContent(excelSheet, 5, obj);

			workbook.write();
			workbook.close();
		}
		catch (Exception ex)
		{
			debugMonitor("Loi xay ra khi tao file excel:" + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void createLabel(WritableSheet sheet, String strHeader,
			String strSubHeader) throws WriteException
	{
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		Border border = Border.ALL;
		BorderLineStyle lineStyle = BorderLineStyle.THIN;
		times.setBorder(border, lineStyle);

		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with no underlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		timesBoldUnderline.setBorder(border, lineStyle);

		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		sheet.mergeCells(0, 0, 2, 0);
		addCaption(sheet, 0, 0, strHeader);
		addCaption(sheet, 0, 2, "Date");
		sheet.mergeCells(1, 2, 2, 2);
		Date date = new Date();
		DateFormat obj = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		addCaption(sheet, 1, 2, obj.format(date));
		String[] array = strSubHeader.split(",");
		for (int i = 0; i < array.length; i++)
		{
			addCaption(sheet, i, 4, array[i]);
		}
	}

	private void createContent(WritableSheet sheet, int StartRow,
			PreparedStatement obj) throws WriteException, RowsExceededException
	{
		int counter = 1;
		int row = StartRow;
		ResultSet result = null;

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		try
		{
			result = obj.executeQuery();
			while (result.next())
			{
				addNumber(sheet, 0, row, counter);

				addLabel(sheet, 1, row, df.format(result.getTimestamp("dates")));

				addLabel(sheet, 2, row, result.getString("isdn"));
				counter++;
				row++;

				_stmtUpdate.setLong(1, result.getLong("subproductid"));
				_stmtUpdate.addBatch();
			}
			if (counter > 1)
			{
				_stmtUpdate.executeBatch();
				connection.commit();
			}

		}
		catch (Exception ex)
		{
			debugMonitor("Loi xay ra khi tao noi dung file excel:"
					+ ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{

			try
			{
				result.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException
	{
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException
	{
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException
	{
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}
}
