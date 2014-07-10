package com.crm.kernel.index;

import java.util.*;

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
public class TreeNode extends IndexNode
{
	private BinaryIndex				items	= null;

	private LinkedList<TreeNode>	nodes	= new LinkedList<TreeNode>();

	public TreeNode(String indexKey)
	{
		super(indexKey);
	}

	// //////////////////////////////////////////////////////
	// clear
	// Author : ThangPV
	// Created Date : 06/01/2005
	// //////////////////////////////////////////////////////
	public void clear() throws Exception
	{
		items.clear();

		TreeNode node = null;

		for (int index = 0; index < nodes.size(); index++)
		{
			node = nodes.get(index);

			if (node != null)
			{
				node.clear();
			}
		}
	}

	// //////////////////////////////////////////////////////
	// insert key into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	protected TreeNode addNode(TreeNode root, String indexKey, int maxLevel, int level) throws Exception
	{
		if (indexKey.equals(""))
		{
			throw new Exception("Key must be has value");
		}

		if ((indexKey.length() <= level) || (level >= maxLevel))
		{
			return this;
		}

		int index = indexKey.charAt(level);

		TreeNode node = nodes.get(index);

		if (node == null)
		{
			node = new TreeNode(indexKey.substring(0, level));

			nodes.set(level, node);
		}

		return addNode(node, indexKey, maxLevel, level + 1);
	}

	// //////////////////////////////////////////////////////
	// insert key into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	protected IndexNode get(IndexNode lookup, int level, boolean checkAll, boolean fullScan) throws Exception
	{
		IndexNode item = null;

		IndexNode result = null;

		boolean overDepth = (lookup.getIndexKey().length() < level);

		if (checkAll || overDepth)
		{
			item = items.get(lookup, fullScan);
		}

		if (!overDepth)
		{
			int index = lookup.getIndexKey().charAt(level);

			TreeNode node = nodes.get(index);

			if (node != null)
			{
				result = node.get(lookup, level + 1, checkAll, fullScan);
			}
		}

		if (result == null)
		{
			result = item;
		}

		return result;
	}

	public void setItems(BinaryIndex items)
	{
		this.items = items;
	}

	public BinaryIndex getItems()
	{
		return items;
	}

	public void setNodes(LinkedList<TreeNode> nodes)
	{
		this.nodes = nodes;
	}

	public LinkedList<TreeNode> getNodes()
	{
		return nodes;
	}
}
