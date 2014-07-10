package com.crm.provisioning.impl.ccws;

import java.text.ParseException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntity;
import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.FundsType;
import com.comverse_in.prepaid.ccws.SubscriberAccountType;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

public class CCWSXmlParser
{
	public static SubscriberRetrieve parseFromDocument(Document document) throws Exception
	{
		SubscriberRetrieve subRetrieve = new SubscriberRetrieve();

		Element subIDElement = (Element) document.getElementsByTagName("SubscriberID").item(0);

		subRetrieve.setSubscriberID(subIDElement.getTextContent());

		parseSubscriberData(subRetrieve, document);

		return subRetrieve;
	}

	/**
	 * Get subscriber data
	 * 
	 * @param subRetrieve
	 * @param document
	 * @throws DOMException
	 * @throws ParseException
	 */
	private static void parseSubscriberData(SubscriberRetrieve subRetrieve, Document document) throws DOMException,
			ParseException
	{
		NodeList subDataNodes = document.getElementsByTagName("SubscriberData");

		if (subDataNodes.getLength() == 0)
			return;

		Element subscriberDataElement = (Element) document.getElementsByTagName("SubscriberData").item(0);

		if (!subscriberDataElement.hasChildNodes())
			return;
		SubscriberEntity subscriberData = new SubscriberEntity();

		/**
		 * COSName
		 */
		ParseValue mCOSName = getValue(subscriberDataElement, "COSName");
		if (mCOSName.hasValue())
			subscriberData.setCOSName(mCOSName.getValue());

		/**
		 * CreationDate
		 */
		ParseValue mCreationDate = getValue(subscriberDataElement, "CreationDate");
		if (mCreationDate.hasValue())
			subscriberData.setCreationDate(mCreationDate.getCalendarValue());

		/**
		 * DateEnterActive
		 */
		ParseValue mDateEnterActive = getValue(subscriberDataElement, "DateEnterActive");
		if (mDateEnterActive.hasValue())
			subscriberData.setDateEnterActive(mDateEnterActive.getCalendarValue());

		/**
		 * PrevTransDate
		 */
		ParseValue mPrevTransDate = getValue(subscriberDataElement, "PrevTransDate");
		if (mPrevTransDate.hasValue())
			subscriberData.setPrevTransDate(mPrevTransDate.getCalendarValue());

		/**
		 * LastTransDate
		 */
		ParseValue mLastTransDate = getValue(subscriberDataElement, "LastTransDate");
		if (mLastTransDate.hasValue())
			subscriberData.setLastTransDate(mLastTransDate.getCalendarValue());

		/**
		 * CurrentState
		 */
		ParseValue mCurrentState = getValue(subscriberDataElement, "CurrentState");
		if (mCurrentState.hasValue())
			subscriberData.setCurrentState(mCurrentState.getValue());

		/**
		 * PreviousState
		 */
		ParseValue mPreviousState = getValue(subscriberDataElement, "PreviousState");
		if (mPreviousState.hasValue())
			subscriberData.setPreviousState(mPreviousState.getValue());

		/**
		 * LanguageName
		 */
		ParseValue mLanguageName = getValue(subscriberDataElement, "LanguageName");
		if (mLanguageName.hasValue())
			subscriberData.setLanguageName(mLanguageName.getValue());

		/**
		 * NotificationLanguage
		 */
		ParseValue mNotificationLanguage = getValue(subscriberDataElement, "NotificationLanguage");
		if (mNotificationLanguage.hasValue())
			subscriberData.setNotificationLanguage(mNotificationLanguage.getValue());

		/**
		 * RoamingCreditLimitAsString
		 */
		ParseValue mRoamingCreditLimitAsString = getValue(subscriberDataElement, "RoamingCreditLimitAsString");
		if (mRoamingCreditLimitAsString.hasValue())
			subscriberData.setRoamingCreditLimitAsString(mRoamingCreditLimitAsString.getValue());

		/**
		 * PINChangeNeeded
		 */
		ParseValue mPINChangeNeeded = getValue(subscriberDataElement, "PINChangeNeeded");
		if (mPINChangeNeeded.hasValue())
			subscriberData.setPINChangeNeeded(mPINChangeNeeded.getBooleanValue());

		/**
		 * NumberFreeAnCallsString
		 */
		ParseValue mNumberFreeAnCallsString = getValue(subscriberDataElement, "NumberFreeAnCallsString");
		if (mNumberFreeAnCallsString.hasValue())
			subscriberData.setNumberFreeAnCallsString(mNumberFreeAnCallsString.getValue());

		/**
		 * BillCycleDay
		 */
		ParseValue mBillCycleDay = getValue(subscriberDataElement, "BillCycleDay");
		if (mBillCycleDay.hasValue())
			subscriberData.setBillCycleDay(mBillCycleDay.getValue());

		/**
		 * NotificationLevel
		 */
		ParseValue mNotificationLevel = getValue(subscriberDataElement, "NotificationLevel");
		if (mNotificationLevel.hasValue())
			subscriberData.setNotificationLevel(mNotificationLevel.getValue());

		/**
		 * MarketSegment
		 */
		ParseValue mMarketSegment = getValue(subscriberDataElement, "MarketSegment");
		if (mMarketSegment.hasValue())
			subscriberData.setMarketSegment(mMarketSegment.getValue());

		/**
		 * LimitType
		 */
		ParseValue mLimitType = getValue(subscriberDataElement, "LimitType");
		if (mLimitType.hasValue())
			subscriberData.setLimitType(mLimitType.getValue());

		/**
		 * SPName
		 */
		ParseValue mSPName = getValue(subscriberDataElement, "SPName");
		if (mSPName.hasValue())
			subscriberData.setSPName(mSPName.getValue());

		/**
		 * AccountType
		 */
		ParseValue mAccountType = getValue(subscriberDataElement, "AccountType");
		if (mAccountType.hasValue())
			subscriberData.setAccountType(SubscriberAccountType.fromString(mAccountType.getValue()));

		/**
		 * LastCallCharge
		 */
		ParseValue mLastCallCharge = getValue(subscriberDataElement, "LastCallCharge");
		if (mLastCallCharge.hasValue())
			subscriberData.setLastCallCharge(mLastCallCharge.getDoubleValue());

		/**
		 * FreeFFChgAllowance
		 */
		ParseValue mFreeFFChgAllowance = getValue(subscriberDataElement, "FreeFFChgAllowance");
		if (mFreeFFChgAllowance.hasValue())
			subscriberData.setFreeFFChgAllowance(mFreeFFChgAllowance.getValue());

		/**
		 * FFChangeCount
		 */
		ParseValue mFFChangeCount = getValue(subscriberDataElement, "FFChangeCount");
		if (mFFChangeCount.hasValue())
			subscriberData.setFFChangeCount(mFFChangeCount.getValue());

		/**
		 * CurrencyCode
		 */
		ParseValue mCurrencyCode = getValue(subscriberDataElement, "CurrencyCode");
		if (mCurrencyCode.hasValue())
			subscriberData.setCurrencyCode(mCurrencyCode.getValue());

		/**
		 * PromisedPaymentAmount
		 */
		ParseValue mPromisedPaymentAmount = getValue(subscriberDataElement, "PromisedPaymentAmount");
		if (mPromisedPaymentAmount.hasValue())
			subscriberData.setPromisedPaymentAmount(mPromisedPaymentAmount.getDoubleValue());

		/**
		 * PromisedPaymentDueDate
		 */
		ParseValue mPromisedPaymentDueDate = getValue(subscriberDataElement, "PromisedPaymentDueDate");
		if (mPromisedPaymentDueDate.hasValue())
			subscriberData.setPromisedPaymentDueDate(mPromisedPaymentDueDate.getCalendarValue());

		/**
		 * PromisedPaymentBalanceID
		 */
		ParseValue mPromisedPaymentBalanceID = getValue(subscriberDataElement, "PromisedPaymentBalanceID");
		if (mPromisedPaymentBalanceID.hasValue())
			subscriberData.setPromisedPaymentBalanceID(mPromisedPaymentBalanceID.getIntValue());

		/**
		 * CosInstantiationTimeStamp
		 */
		ParseValue mCosInstantiationTimeStamp = getValue(subscriberDataElement, "CosInstantiationTimeStamp");
		if (mCosInstantiationTimeStamp.hasValue())
			subscriberData.setCosInstantiationTimeStamp(mCosInstantiationTimeStamp.getCalendarValue());

		/**
		 * LoyaltyEnrollmentDate
		 */
		ParseValue mLoyaltyEnrollmentDate = getValue(subscriberDataElement, "LoyaltyEnrollmentDate");
		if (mLoyaltyEnrollmentDate.hasValue())
			subscriberData.setLoyaltyEnrollmentDate(mLoyaltyEnrollmentDate.getCalendarValue());


		/**
		 * NextLoyaltySyncDate
		 */
		ParseValue mNextLoyaltySyncDate = getValue(subscriberDataElement, "NextLoyaltySyncDate");
		if (mNextLoyaltySyncDate.hasValue())
			subscriberData.setNextLoyaltySyncDate(mNextLoyaltySyncDate.getCalendarValue());

		/**
		 * LoyaltyScaleRate
		 */
		ParseValue mLoyaltyScaleRate = getValue(subscriberDataElement, "LoyaltyScaleRate");
		if (mLoyaltyScaleRate.hasValue())
			subscriberData.setLoyaltyScaleRate(mLoyaltyScaleRate.getDoubleValue());

		/**
		 * LoyaltyBonusScale
		 */
		ParseValue mLoyaltyBonusScale = getValue(subscriberDataElement, "LoyaltyBonusScale");
		if (mLoyaltyBonusScale.hasValue())
			subscriberData.setLoyaltyBonusScale(mLoyaltyBonusScale.getIntValue());

		/**
		 * CurrentHappyHourActiveDate
		 */
		ParseValue mCurrentHappyHourActiveDate = getValue(subscriberDataElement, "CurrentHappyHourActiveDate");
		if (mCurrentHappyHourActiveDate.hasValue())
			subscriberData.setCurrentHappyHourActiveDate(mCurrentHappyHourActiveDate.getCalendarValue());


		/**
		 * CurrentHappyHourEndDate
		 */
		ParseValue mCurrentHappyHourEndDate = getValue(subscriberDataElement, "CurrentHappyHourEndDate");
		if (mCurrentHappyHourEndDate.hasValue())
			subscriberData.setCurrentHappyHourEndDate(mCurrentHappyHourEndDate.getCalendarValue());

		/**
		 * PreferredNumberDate
		 */
		ParseValue mPreferredNumberDate = getValue(subscriberDataElement, "PreferredNumberDate");
		if (mPreferredNumberDate.hasValue())
			subscriberData.setPreferredNumberDate(mPreferredNumberDate.getCalendarValue());

		/**
		 * Balances
		 */
		Element eBalances = (Element) subscriberDataElement.getElementsByTagName("Balances").item(0);
		ArrayOfBalanceEntity aoBalance = new ArrayOfBalanceEntity();
		NodeList nodeBalances = eBalances.getChildNodes();
		BalanceEntity[] balances = new BalanceEntity[nodeBalances.getLength()];
		for (int i = 0; i < balances.length; i++)
		{
			balances[i] = new BalanceEntity();
			Element eBalanceNode = (Element) nodeBalances.item(i);

			/**
			 * BalanceName
			 */
			ParseValue mBalanceName = getValue(eBalanceNode, "BalanceName");
			if (mBalanceName.hasValue())
				balances[i].setBalanceName(mBalanceName.getValue());

			/**
			 * Balance
			 */
			ParseValue mBalance = getValue(eBalanceNode, "Balance");
			if (mBalance.hasValue())
				balances[i].setBalance(mBalance.getDoubleValue());

			/**
			 * AvailableBalance
			 */
			ParseValue mAvailableBalance = getValue(eBalanceNode, "AvailableBalance");
			if (mAvailableBalance.hasValue())
				balances[i].setAvailableBalance(mAvailableBalance.getDoubleValue());

			/**
			 * AccountExpiration
			 */
			ParseValue mAccountExpiration = getValue(eBalanceNode, "AccountExpiration");
			if (mAccountExpiration.hasValue())
				balances[i].setAccountExpiration(mAccountExpiration.getCalendarValue());

			/**
			 * NextMaximumSpendingLimit
			 */
			ParseValue mNextMaximumSpendingLimit = getValue(eBalanceNode, "NextMaximumSpendingLimit");
			if (mNextMaximumSpendingLimit.hasValue())
				balances[i].setNextMaximumSpendingLimit(mNextMaximumSpendingLimit.getDoubleValue());

			/**
			 * TotalSpendingLimit
			 */
			ParseValue mTotalSpendingLimit = getValue(eBalanceNode, "TotalSpendingLimit");
			if (mTotalSpendingLimit.hasValue())
				balances[i].setTotalSpendingLimit(mTotalSpendingLimit.getDoubleValue());

			/**
			 * FundsType
			 */
			ParseValue mFundsType = getValue(eBalanceNode, "FundsType");
			if (mFundsType.hasValue())
				balances[i].setFundsType(FundsType.fromString(mFundsType.getValue()));

			/**
			 * MaximumSpendingLimit
			 */
			ParseValue mMaximumSpendingLimit = getValue(eBalanceNode, "MaximumSpendingLimit");
			if (mMaximumSpendingLimit.hasValue())
				balances[i].setMaximumSpendingLimit(mMaximumSpendingLimit.getDoubleValue());


			/**
			 * AvailableSpendingLimit
			 */
			ParseValue mAvailableSpendingLimit = getValue(eBalanceNode, "AvailableSpendingLimit");
			if (mAvailableSpendingLimit.hasValue())
				balances[i].setAvailableSpendingLimit(mAvailableSpendingLimit.getDoubleValue());

			/**
			 * PrecisionPoint
			 */
			ParseValue mPrecisionPoint = getValue(eBalanceNode, "PrecisionPoint");
			if (mPrecisionPoint.hasValue())
				balances[i].setPrecisionPoint(mPrecisionPoint.getIntValue());
			/**
			 * ExclusiveBalance
			 */
			ParseValue mExclusiveBalance = getValue(eBalanceNode, "ExclusiveBalance");
			if (mExclusiveBalance.hasValue())
				balances[i].setExclusiveBalance(mExclusiveBalance.getBooleanValue());
		}
		aoBalance.setBalance(balances);
		subscriberData.setBalances(aoBalance);

		subRetrieve.setSubscriberData(subscriberData);
	}

	private static ParseValue getValue(Element e, String tagName)
	{
		if (e == null)
			return new ParseValue();

		NodeList nodeList = e.getElementsByTagName(tagName);

		if (nodeList == null)
			return new ParseValue();

		Element element = (Element) nodeList.item(0);
		if (element == null)
			return new ParseValue();

		return new ParseValue(true, element.getTextContent());
	}
}
