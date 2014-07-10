package com.crm.kernel.index;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class BinaryIndex
{
	// synchronize
	private Object						lockedObject	= new Object();

	private ArrayList<IndexNode>		nodes			= new ArrayList<IndexNode>();

	private HashMap<String, IndexNode>	keyMap			= new HashMap<String, IndexNode>();

	private HashMap<Long, IndexNode>	idMap			= new HashMap<Long, IndexNode>();

	private Logger						log				= Logger.getLogger(BinaryIndex.class);

	public BinaryIndex()
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// clear
	// Author : ThangPV
	// Created Date : 06/01/2005
	// //////////////////////////////////////////////////////
	public synchronized void clear()
	{
		nodes.clear();
		keyMap.clear();
		idMap.clear();
	}

	// //////////////////////////////////////////////////////
	// add value into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public boolean add(long id, String alias, IndexNode node)
	{
		synchronized (lockedObject)
		{
			try
			{
				if (node == null)
				{
					return false;
				}
				else
				{
					nodes.add(nodes.size(), node);

					if (id != 0)
					{
						idMap.put(id, node);
					}

					if (!alias.equals(""))
					{
						keyMap.put(alias, node);
					}
				}
			}
			catch (Exception e)
			{
				log.error(e, e);
			}
		}

		return true;
	}

	// //////////////////////////////////////////////////////
	// add value into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public boolean add(IndexNode node)
	{
		synchronized (lockedObject)
		{
			try
			{
				if (node == null)
				{
					return false;
				}
				else
				{
					nodes.add(nodes.size(), node);
				}
			}
			catch (Exception e)
			{
				log.error(e, e);
			}
		}

		return true;
	}

	// //////////////////////////////////////////////////////
	// Nodes
	// //////////////////////////////////////////////////////
	public ArrayList<IndexNode> getNodes()
	{
		return nodes;
	}

	public void setNodes(ArrayList<IndexNode> nodes)
	{
		clear();

		this.nodes = nodes;

		for (IndexNode node : nodes)
		{
			// idMap.put(node.getNodeId(), node);
			keyMap.put(node.getIndexKey(), node);
		}
	}

	public int size()
	{
		return nodes.size();
	}

	public HashMap<Long, IndexNode> getIdMap()
	{
		return idMap;
	}

	// //////////////////////////////////////////////////////
	// get
	// //////////////////////////////////////////////////////
	public IndexNode get(int index) throws Exception
	{
		return nodes.get(index);
	}

	// //////////////////////////////////////////////////////
	// get key
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public String getIndexKey(int index) throws Exception
	{
		return get(index).getIndexKey();
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	protected IndexNode get(IndexNode lookup, int start, int end, boolean fullScan) throws Exception
	{
		int index = start + (end - start) / 2;

		int rescanStart = -1;
		int rescanEnd = -1;

		if (nodes.size() == 0)
		{
			return null;
		}

		IndexNode node = null;
		IndexNode result = null;

		int intResult = 0;

		if (start == end)
		{
			node = nodes.get(start);

			intResult = node.compareTo(lookup);

			if (intResult == 0)
			{
				result = node;
			}
		}
		else
		{
			node = nodes.get(index);

			intResult = node.compareTo(lookup);

			// return if equals and previous element are started with indexKey
			// of current node
			if (intResult == 0)
			{
				if (index != 0)
				{
					if (!node.getIndexKey().equals(""))
					{
						if (!getIndexKey(index - 1).startsWith(node.getIndexKey()))
						{
							result = node;
						}
					}
					else
					{
						result = node;
					}
				}
			}

			if (result == null)
			{
				if (intResult > 0)
				{
					if (start == index)
					{
						start = index + 1;
					}
					else
					{
						start = index;
					}
				}
				else
				{
					// if allow backward search and next element are started
					// with first character of indexKey
					if (fullScan)
					{
						String indexKey = getIndexKey(index + 1).substring(0, 1);

						if (lookup.getIndexKey().startsWith(indexKey))
						{
							rescanStart = index + 1;
							rescanEnd = end;
						}
					}

					end = index;
				}

				result = get(lookup, start, end, fullScan);

				if ((result == null) && (rescanStart >= 0) && fullScan)
				{
					result = get(lookup, rescanStart, rescanEnd, fullScan);
				}
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////

	public IndexNode get(IndexNode lookup, boolean fullScan) throws Exception
	{
		IndexNode result = get(lookup, 0, nodes.size() - 1, fullScan);

		if (log.isDebugEnabled())
		{
			if ((result != null))
			{
				log.debug("Object " + result.getClass().getName() + " : " + result.toString());
			}
			else
			{
				log.debug("Object not found: " + lookup.getClass().getName() + " : " + lookup.toString());
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////

	public IndexNode get(IndexNode lookup) throws Exception
	{
		return get(lookup, 0, nodes.size() - 1, false);
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public IndexNode getByKey(String alias) throws Exception
	{
		return keyMap.get(alias);
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public IndexNode getById(long nodeId) throws Exception
	{
		return idMap.get(nodeId);
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("\n");

		for (int j = 0; j < nodes.size(); j++)
		{
			IndexNode node = nodes.get(j);

			buffer.append(node.toString() + "\n");
		}

		return buffer.toString();
	}
}
