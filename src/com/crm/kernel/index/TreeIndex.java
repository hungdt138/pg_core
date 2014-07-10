package com.crm.kernel.index;

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
public class TreeIndex extends TreeNode
{
	private int	maxLevel	= 0;

	public TreeIndex()
	{
		super("");
	}

	// //////////////////////////////////////////////////////
	// set max level
	// Author : ThangPV
	// Created Date : 06/01/2005
	// //////////////////////////////////////////////////////
	public void setMaxLevel(int maxLevel) throws Exception
	{
		clear();

		this.maxLevel = maxLevel;
	}

	// //////////////////////////////////////////////////////
	// insert key into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public TreeNode add(TreeNode node) throws Exception
	{
		return this.addNode(this, node.getIndexKey(), maxLevel, 0);
	}

	// //////////////////////////////////////////////////////
	// insert key into list
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public boolean addItem(IndexNode item) throws Exception
	{
		TreeNode node = this.addNode(this, item.getIndexKey(), maxLevel, 0);

		return node.getItems().add(item);

	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	protected IndexNode get(IndexNode lookup, boolean checkAll, boolean fullScan) throws Exception
	{
		return this.get(lookup, 0, checkAll, fullScan);
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public IndexNode get(TreeNode lookup, boolean fullScan) throws Exception
	{
		return get(lookup, true, fullScan);
	}

	// //////////////////////////////////////////////////////
	// get node
	// Author : ThangPV
	// Created Date : 30/03/2006
	// //////////////////////////////////////////////////////
	public IndexNode get(TreeNode lookup) throws Exception
	{
		return get(lookup, true, true);
	}
}
