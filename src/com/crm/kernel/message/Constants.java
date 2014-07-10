/**
 * 
 */
package com.crm.kernel.message;

/**
 * @author ThangPV
 * 
 */
public class Constants {
	// log level
	public final static String LOG_LEVEL_OFF = "off";
	public final static String LOG_LEVEL_DEBUG = "debug";
	public final static String LOG_LEVEL_ERROR = "error";
	public final static String LOG_LEVEL_FATAL = "fatal";
	public final static String LOG_LEVEL_INFO = "info";
	public final static String LOG_LEVEL_TRACE = "trace";
	public final static String LOG_LEVEL_WARN = "warn";

	// Id
	public final static long DEFAULT_ID = 0;
	public final static int DEFAULT_PENDING_TIME = 60;
	public final static int DEFAULT_TIMEOUT = 30000;

	// Id
	public final static int QUEUE_MODE_MANUAL = 0;
	public final static int QUEUE_MODE_CONSUMER = 1;
	public final static int QUEUE_MODE_PRODUCER = 2;

	public final static int QUEUE_CONNECTION_SHARING = 0;
	public final static int QUEUE_CONNECTION_DEDICATED = 1;

	// product type
	public final static String PRODUCT_BUNDLE = "bundle";
	public final static String PRODUCT_SINGLE = "single";
	public final static String PRODUCT_CHARGING = "charging";

	// product buy able
	public final static int PRODUCT_NOT_SUPPORT = 0;

	// Channel
	public final static String CHANNEL_SMS = "SMS";
	public final static String CHANNEL_WEB = "web";
	public final static String CHANNEL_CORE = "core";

	/**
	 * Balance state Active (CCWS)
	 */
	public final static String BALANCE_STATE_ACTIVE = "Active";
	/**
	 * Balance state Suspend (S1) (CCWS)
	 */
	public final static String BALANCE_STATE_SUSPEND_S1 = "Suspended(S1)";
	/**
	 * Balance state Disable (S2) (CCWS)
	 */
	public final static String BALANCE_STATE_SUSPEND_S2 = "Disabled(S2)";
	/**
	 * Balance state Retired (S3) (CCWS)
	 */
	public final static String BALANCE_STATE_RETIRED_S3 = "Retired(S3)";
	/**
	 * Balance state Idle (CCWS)
	 */
	public final static String BALANCE_STATE_IDLE = "Idle";
	/**
	 * Balance state Deleted (CCWS)
	 */
	public final static String BALANCE_STATE_DELETED = "Deleted";

	// discount
	public final static String DISCOUNT_FREE_OF_CHARGE = "free";
	public final static String DISCOUNT_RATE = "rate";
	public final static String DISCOUNT_FIX_VALUE = "fix";

	// status
	public final static int SERVICE_BUY_ABLE = 1;
	public final static int SERVICE_DENIED = 0;

	public final static int SERVICE_STATUS_APPROVED = 0;
	public final static int SERVICE_STATUS_DENIED = 4;

	public final static int CAMPAIGN_STATUS_APPROVED = 1;
	public final static int CAMPAIGN_STATUS_DENIED = 0;

	public final static String CAMPAIGN_TYPE_SMS = "SMS";
	public final static String CAMPAIGN_TYPE_CORE = "core";

	// Action

	public final static String ACTION_ADD_MEMBER = "add-member";

	public final static String ACTION_REMOVE_MEMBER = "remove-member";

	public final static String ACTION_TOPUP = "topup";

	public final static String ACTION_UPGRADE = "upsell";

	public final static String ACTION_REGISTER = "register";

	public final static String ACTION_UNREGISTER = "unregister";

	public final static String ACTION_CANCEL = "cancel";

	public final static String ACTION_USER_REACTIVE = "reactive";

	public final static String ACTION_SUPPLIER_REACTIVE = "reactive";

	public final static String ACTION_USER_DEACTIVE = "deactive";

	public final static String ACTION_SUPPLIER_DEACTIVE = "deactive";

	public final static String ACTION_SUPPLIER_BARRING = "barring";

	public final static String ACTION_RECONCILE = "reconcile";

	public final static String ACTION_SUBSCRIPTION = "subscription";

	public final static String ACTION_CHARGING = "charging";

	public final static String ACTION_CHANGE_ISDN = "isdn";

	public final static String ACTION_CHANGE_IMSI = "imsi";

	public final static String ACTION_CHANGE_PROFILE = "profile";

	public final static String ACTION_RANK = "ranking";

	public final static String ACTION_ROLLBACK = "rollback";

	public final static String ACTION_ADVERTISING = "advertising";

	public final static String ACTION_CONFIRM = "confirm";

	public final static String ACTION_RENEW = "renew";

	public final static String ACTION_FREE = "free";

	public final static String ACTION_FREE_TO_NORMAL = "free-to-normal";

	public final static String ACTION_INVITE = "invite";

	// order constants
	public static final int ORDER_STATUS_APPROVED = 0;

	public static final int ORDER_STATUS_PENDING = 1;

	public static final int ORDER_STATUS_DRAFT = 2;

	public static final int ORDER_STATUS_EXPIRED = 3;

	public static final int ORDER_STATUS_DENIED = 4;

	public static final int ORDER_STATUS_ANY = -1;

	// subscriber type constants
	public static final int ALL_SUB_TYPE = 0;

	public static final int PREPAID_SUB_TYPE = 1;

	public static final int POSTPAID_SUB_TYPE = 2;

	public static final int UNKNOW_SUB_TYPE = -1;

	// status constants
	public static final int USER_ACTIVE_STATUS = 1;

	public static final int USER_BARRING_STATUS = 2;

	public static final int USER_CANCEL_STATUS = 3;

	public static final int SUPPLIER_UNKNOW_STATUS = -1;

	public static final int SUPPLIER_ACTIVE_STATUS = 1;

	public static final int SUPPLIER_BARRING_STATUS = 2;

	public static final int SUPPLIER_CANCEL_STATUS = 3;

	public static final int SUPPLIER_CANCEL_CONFIRM_STATUS = 4;

	// subscriber status constants
	public static final int SUBSCRIBER_PENDING_STATUS = 0;

	public static final int SUBSCRIBER_REGISTER_STATUS = 1;

	public static final int SUBSCRIBER_FREE_WITH_REACTIVE_STATUS = 2;

	public static final int SUBSCRIBER_ALERT_EXPIRE_STATUS = 3;

	public static final int SUBSCRIBER_ALERT_BALANCE_STATUS = 4;

	public static final int SUBSCRIBER_NOT_EXTEND_STATUS = 5;

	public static final int SUBSCRIBER_ACCEPT_EXTEND_STATUS = 6;

	public static final int SUBSCRIBER_FREE_NOT_REACTIVE_STATUS = 7;

	public static final int SUBSCRIBER_SUBSCRIPTION_STATUS = 8;

	public static final int SUBSCRIBER_ALERT_FREE_NOT_REACTIVE_STATUS = 9;

	public static final int SUBSCRIBER_TERMINATE_FREE_STATUS = 10;

	public static final int SUBSCRIBER_INVITE_STATUS = 11;

	// ISDN
	public final static String COUNTRY_CODE = "84";
	public final static String DOMESTIC_CODE = "0";

	public final static String SHORT_CODE_VNM_8492 = "8492";
	public final static String SHORT_CODE_VNM_84186 = "84186";
	public final static String SHORT_CODE_VNM_84188 = "84188";

	// Provisioning
	public final static String PROVISIONING_OSA = "OSA";
	public final static String PROVISIONING_CCWS = "CCWS";
	public final static String PROVISIONING_CRM = "CRM";

	// Keyword
	public final static String INVITE_KEYWORD_SMS = "TANG ";

	// Language
	public final static String DEFAULT_LANGUAGE = "vni";

	// COLUMN
	public final static String PRIMARY_ID_FIELD = "$primaryId$";
	public final static String COMPANY_ID_FIELD = "$companyId$";
	public final static String GROUP_ID_FIELD = "$groupId$";
	public final static String USER_ID_FIELD = "$userId$";
	public final static String USER_NAME_FIELD = "$userName$";
	public final static String CREATE_DATE_FIELD = "$createDate$";
	public final static String MODIFIED_DATE_FIELD = "$modifiedDate$";
	public final static String ERROR_FIELD = "$error$";

	public final static int PRIMARY_ID_COLUMN = 1000;
	public final static int COMPANY_ID_COLUMN = 1010;
	public final static int GROUP_ID_COLUMN = 1020;
	public final static int USER_ID_COLUMN = 1030;
	public final static int USER_NAME_COLUMN = 1040;
	public final static int CREATE_DATE_COLUMN = 1050;
	public final static int MODIFIED_DATE_COLUMN = 1060;
	public final static int ERROR_COLUMN = 1070;

	// BIND
	public final static int BIND_ACTION_NONE = 0;
	public final static int BIND_ACTION_SUCCESS = 1;
	public final static int BIND_ACTION_EXPORT = 2;
	public final static int BIND_ACTION_ERROR = 3;
	public final static int BIND_ACTION_BYPASS = 4;

	// Audit
	public final static String AUDIT_SUBSCRIBER = "00";
	public final static String AUDIT_SUB_PRODUCT = "10";
	public final static String AUDIT_SUB_ORDER = "20";

	// Associate type
	public final static String ASSOCIATE_CROSS_SELL = "cross-sell";
	public final static String ASSOCIATE_UP_SELL = "up-sell";
	public final static String ASSOCIATE_BLACK_LIST = "black-list";

	// Command
	public final static String PROVISIONING_SMSC = "SMSC";
	public final static String COMMAND_SEND_SMS = "SMSC.SEND_SMS";

	// Response
	public final static String SUCCESS = "success";

	public final static String ERROR = "error";

	public final static String UPGRADING = "upgrading";

	public static final String EPOS_COS_CHANGED = "OOM3-10042";
	public static final String EPOS_COS_CANCELED = "OOM3-10043";

	// NamTA
	public final static String REQUEST_PREFIX = "request.";
	public final static String RESPONSE_PREFIX = "response.";

	public final static String ERROR_RESOURCE_BUSY = "resource-busy";
	public final static String ERROR_INVALID_REQUEST = "invalid-request";
	public final static String ERROR_INVALID_SYNTAX = "invalid-syntax";
	public final static String ERROR_OVER_TRANSACTION_LIMIT = "over-limit";
	public final static String ERROR_CREATE_ORDER_FAIL = "create-order-fail";

	public final static String ERROR_PRODUCT_NOT_FOUND = "unknow-product";
	public final static String ERROR_ROUTE_NOT_FOUND = "unknow-route";
	public final static String ERROR_CAMPAIGN_NOT_FOUND = "unknow-campaign";
	public final static String ERROR_SEGMENT_NOT_FOUND = "unknow-segment";
	public final static String ERROR_RANK_NOT_FOUND = "unknow-rank";
	public final static String ERROR_PROVISIONING_NOT_FOUND = "unknow-provisioning";
	public final static String ERROR_COMMAND_NOT_FOUND = "unknow-command";
	public final static String ERROR_ORDER_NOT_FOUND = "unknow-order";
	public final static String ERROR_SUBSCRIBER_NOT_FOUND = "unknow-subscriber";
	public final static String ERROR_SUBSCRIPTION_NOT_FOUND = "unknow-subscription";
	public final static String ERROR_MEMBER_NOT_FOUND = "unknow-member";
	public final static String ERROR_GROUP_NOT_FOUND = "unknow-group";
	public final static String ERROR_BALANCE_NOT_FOUND = "unknow-balance";
	public final static String ERROR_PROCESS_CLASS = "unknow-class";
	public final static String ERROR_PROCESS_METHOD = "unknow-method";
	public final static String ERROR_UNSUPPORT = "unsupport";
	public final static String ERROR_UNKNOW_SUB_TYPE = "unknow-sub-type";
	public final static String ERROR_MERCHANT_NOT_FOUND = "unknow-mechant";
	public final static String ERROR_AGENT_NOT_FOUND = "unknow-agent";

	public final static String ERROR_DUPLICATED = "duplicated";
	public final static String ERROR_PENDING_COMMANDREQUEST = "pending-command-request";
	public final static String ERROR_REGISTERED = "registered";
	public final static String ERROR_UNREGISTERED = "unregistered";
	public final static String ERROR_EXPIRED = "expired";
	public final static String ERROR_BLACKLIST_PRODUCT = "blacklist-product";

	public final static String ERROR_KEYWORD = "unknow-keyword";
	public final static String ERROR_OUT_OF_TIME = "out-of-time";

	public final static String ERROR_CONNECTION = "disconnected";
	public final static String ERROR_TIMEOUT = "timeout";
	public final static String ERROR_RECURSIVE_COMMAND = "recursive-command";
	public final static String ERROR_DENIED_COS = "denied-cos";
	public final static String ERROR_DENIED_STATUS = "denied-status";
	public final static String ERROR_DENIED_SUBSCRIBER_TYPE = "denied-subscriber-type";
	public final static String ERROR_NOT_ENOUGH_MONEY = "not-enought-money";
	public final static String ERROR_BALANCE_TOO_LARGE = "balance-too-large";
	public final static String ERROR_EXPIRE_TOO_LARGE = "expire-too-large";
	public final static String ERROR_OUT_OF_EXPIRE = "out-of-expire";
	public final static String ERROR_BALANCE_TOO_SMALL = "balance-too-small";
	public final static String ERROR_INVALID_OWNER = "invalid-owner";
	public final static String ERROR_INVALID_DELIVER = "invalid-deliver";
	public final static String ERROR_OVER_MEMBER_LIMITATION = "over-max-member";
	public final static String ERROR_EXSITED_MEMBER = "existed-member";
	public final static String ERROR_INVALID_ACTIVE_DATE = "invalid-active-date";
	public final static String ERROR_UNREGISTER_INDAY = "unregister-inday";
	public final static String ERROR_SUBSCRIPTION_DUPPLICATE = "subscription-dupplicate";
	// VB Error response
	public final static String ERROR_NOTIFY_OVER_EXPIRED = "notify-expired-success";
	public final static String ERROR_NOTIFY_OVER_BALANCE = "notify-balance-success";
	public final static String VB_RENEW_SUCCESS = "renew-success";

	public final static String ERROR_CANNOT_GET_CONNECTION = "cannot-get-connection";

	// MP service
	public final static String ERROR_NOT_ENOUGH_CONDITION = "verify-condition-fail";
	public final static int VERIFY_CONDITION_SUCCESS = 0;
	public final static int VERIFY_CONDITION_FAIL = 1;

	// DATE
	public final static String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	// Balances
	public final static String PARM_OFFER_NAME = "offer.name";
	public final static String PARM_OFFER_DURATION = "offer.duration";
	public final static String PARM_OFFER_DURATION_VALUE = "364";
	public final static String PARM_MAX_MEMBER = "max-member";

	// Command cost
	public final static String COST_LOW = "low";
	public final static String COST_MEDIUM = "medium";
	public final static String COST_HIGH = "high";
	public final static int COST_LOW_VALUE = 500;
	public final static int COST_HIGH_VALUE = 1000;

	// Lucky sim product keyword
	public final static String INVALID_RECHARGE_NOT_USED = "invalid-recharge-not-used";
	public final static String INVALID_RECHARGE_USED = "invalid-recharge-used";

	// Insert table product keyword
	public final static String VERIFY_KEYWORD = "true";

	// HungDT Spam
	public static final int NO_SPAM_ACTION = 0;
	public static final int ADD_SPAM_ACTION = 1;
	public static final int WAIT_EXPIRE_SPAM_ACTION = 2;
	public static final int EXPIRE_SPAM_ACTION = 3;
	
	public static final String ERROR_SPAM = "error-spam";

	public final static String ERROR_INVALID_AMOUNT = "invalid-amount";
	public final static String ERROR_CARD = "invalid-card";

}
