/**
 * 
 */
package com.crm.loyalty.cache;

import com.crm.kernel.index.IndexNode;

/**
 * @author ThangPV
 * 
 */
public class RankEntry extends IndexNode
{
	private long			rankId		= 0;
	private String			title		= "";
	private int				priority	= 0;
	private long			segmentId	= 0;

	private double			minScore	= 0;
	private double			maxScore	= 0;

	private String			rankUnit	= "month";
	private int				rankPeriod	= 12;

	private String			scoreUnit	= "month";
	private int				scorePeriod	= 12;

	public RankEntry(long rankId, String alias)
	{
		super(alias);

		setRankId(rankId);
	}

	public long getRankId()
	{
		return rankId;
	}

	public void setRankId(long rankId)
	{
		this.rankId = rankId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}

	public double getMinScore()
	{
		return minScore;
	}

	public void setMinScore(double minScore)
	{
		this.minScore = minScore;
	}

	public double getMaxScore()
	{
		return maxScore;
	}

	public void setMaxScore(double maxScore)
	{
		this.maxScore = maxScore;
	}

	public String getRankUnit()
	{
		return rankUnit;
	}

	public void setRankUnit(String rankUnit)
	{
		this.rankUnit = rankUnit;
	}

	public int getRankPeriod()
	{
		return rankPeriod;
	}

	public void setRankPeriod(int rankPeriod)
	{
		this.rankPeriod = rankPeriod;
	}

	public String getScoreUnit()
	{
		return scoreUnit;
	}

	public void setScoreUnit(String scoreUnit)
	{
		this.scoreUnit = scoreUnit;
	}

	public int getScorePeriod()
	{
		return scorePeriod;
	}

	public void setScorePeriod(int scorePeriod)
	{
		this.scorePeriod = scorePeriod;
	}

	public boolean equals(double score)
	{
		boolean result = isRange(minScore, maxScore , score);

		return result;
	}
}
