package com.crm.util;

import java.io.FileOutputStream;
import java.io.File;
import java.nio.channels.FileLock;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class GeneratorSeq
{

	private static final String		CURRENT_SEQ	= "CurrentSeq = ";
	private static final String		MIN_SEQ		= "MinSeq = ";
	private static final String		MAX_SEQ		= "MaxSeq = ";
	private static final String		STEP_SEQ	= "Step_Seq = ";

	private static RandomAccessFile	file;
	private static FileChannel		channel;
	private static FileLock			lock;

	private static int				StepSeq		= 1000;
	private static int				MinSeq		= 0;
	private static int				MaxSeq		= 500000;

	private static final String		fileName	= "CCS_SeqID.cfg";
	private static final String		fileLock	= "CCS_SeqID.lock";
	private static String			filePath	= System.getProperty("user.home") +
														File.separator + "config" + File.separator;
	private static int				nCurrentSeq	= 0;												// Sequence
																									// doc
																									// duoc
																									// trong
																									// file
																									// hien
																									// tai
	private static int				nSubSeq;

	private static void lockFile() throws Exception
	{
		file = new RandomAccessFile(filePath + fileLock, "rw");
		channel = file.getChannel();
		lock = channel.lock();
	}

	private static void unLock() throws Exception
	{
		lock.release();
		file.close();
		channel.close();
	}

	static
	{
		File dir = new File(filePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		File lock = new File(filePath + fileLock);
		try
		{
			if (!lock.exists())
			{
				lock.createNewFile();
			}
			lockFile();
			File sequenceFile = new File(filePath + fileName);
			if (!sequenceFile.exists())
			{
				File sequenceFileBackup = new File(filePath + fileName + ".bk");
				// Kiem tra xem co ton tai file back up khong
				if (!sequenceFileBackup.exists())
				{
					FileOutputStream fout = new FileOutputStream(filePath + fileName);
					nCurrentSeq = MinSeq;
					String current_seq = CURRENT_SEQ + nCurrentSeq + "\n";
					String min_seq = MIN_SEQ + MinSeq + "\n";
					String max_seq = MAX_SEQ + MaxSeq + "\n";
					String step_seq = STEP_SEQ + StepSeq + "\n";
					fout.write(current_seq.getBytes());
					fout.write(min_seq.getBytes());
					fout.write(max_seq.getBytes());
					fout.write(step_seq.getBytes());
					fout.close();
				}
				else
				{
					sequenceFileBackup.renameTo(new File(filePath + fileName));
				}
			}
			readValueFromSeqFile();
			writeNewValueInFile(nCurrentSeq + StepSeq, MinSeq, MaxSeq, StepSeq);
			unLock();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static synchronized int getNextSeq()
	{
		try
		{
			if (nSubSeq >= StepSeq)
			{
				lockFile();
				try
				{
					readValueFromSeqFile();
					// Neu ko du cho 1 lan ghi nua
					if ((nCurrentSeq + StepSeq) > MaxSeq)
					{
						// Reset
						writeNewValueInFile(MinSeq, MinSeq, MaxSeq, StepSeq);
						StepSeq = MaxSeq - nCurrentSeq; // gan lai de khong chay
														// qua gia tri max
					}
					else
					{
						writeNewValueInFile(nCurrentSeq + StepSeq, MinSeq, MaxSeq, StepSeq);
					}
					nSubSeq = 0;
				}
				finally
				{
					unLock();
				}
			}
			nSubSeq++;
			return nSubSeq + nCurrentSeq;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
		}
		// return 0;
	}

	/**
	 * Read
	 * 
	 * @throws Exception
	 */
	private static void readValueFromSeqFile() throws Exception
	{
		FileInputStream fin = new FileInputStream(filePath + fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
		String sCurrentSeq = reader.readLine();
		GeneratorSeq.nCurrentSeq = Integer.parseInt(sCurrentSeq.split("=")[1].trim());
		String sMinSeq = reader.readLine();
		GeneratorSeq.MinSeq = Integer.parseInt(sMinSeq.split("=")[1].trim());
		String sMaxSeq = reader.readLine();
		GeneratorSeq.MaxSeq = Integer.parseInt(sMaxSeq.split("=")[1].trim());
		String sStepSeq = reader.readLine();
		GeneratorSeq.StepSeq = Integer.parseInt(sStepSeq.split("=")[1].trim());
		reader.close();
		fin.close();
	}

	/**
	 * 
	 * @param current
	 *            int
	 * @param min
	 *            int
	 * @param max
	 *            int
	 */
	private static void writeNewValueInFile(int current, int min, int max,
											int step) throws
			Exception
	{
		File file = new File(filePath + fileName);
		File bkFile = new File(filePath + fileName + ".bk");
		if (bkFile.exists())
		{
			bkFile.delete();
		}

		file.renameTo(new File(filePath + fileName + ".bk"));
		FileOutputStream fout = new FileOutputStream(filePath + fileName + ".temp");
		String current_seq = CURRENT_SEQ + current + "\n";
		String min_seq = MIN_SEQ + min + "\n";
		String max_seq = MAX_SEQ + max + "\n";
		String step_seq = STEP_SEQ + step + "\n";
		fout.write(current_seq.getBytes());
		fout.write(min_seq.getBytes());
		fout.write(max_seq.getBytes());
		fout.write(step_seq.getBytes());
		fout.close();
		File filetemp = new File(filePath + fileName + ".temp");

		filetemp.renameTo(new File(filePath + fileName));
	}

	public static void main(String args[])
	{
		for (int i = 0; i < 110000; i++)
		{
//			System.out.println(getNextSeq());
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ex)
			{
			}
		}
	}

}
