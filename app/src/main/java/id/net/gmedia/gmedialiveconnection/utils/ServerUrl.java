package id.net.gmedia.gmedialiveconnection.utils;

public class ServerUrl {

	/*private static final String baseUrl = "https://erpsmg.gmedia.id/client_tools/";

	public static final String auth = baseUrl + "api/auth/login/";
	public static final String getLiveTraffic = baseUrl + "api/router/live_traffic/";
	public static final String getUpDownConnection = baseUrl + "api/router/status_up_down/";
	public static final String stopLoveConnection = baseUrl + "api/router/stop_live_traffic/";
	public static final String getLiveTorch = baseUrl + "api/router/live_client_traffic/";
	public static final String getTorchTraffic = baseUrl + "api/router/client_traffic/";
	public static final String stopTorchConnection = baseUrl + "api/router/stop_client_traffic/";*/
//	private static String baseURL = "http://192.168.20.34:8099/";
	private static String baseURL = "https://erpsmg.gmedia.id/customer_bridge/";
	public static String UrlLogin = baseURL + "auth/login";
	public static String UrlDataMenuComplaint = baseURL + "ticket/list";
	public static String UrlDataListEvent = baseURL + "event/list";
	public static String UrlDataAddTicket = baseURL + "ticket/open";
	public static String UrlCoverageArea = baseURL + "coverage/fo";
	public static String UrlDetailEvent = baseURL + "event/view/";
	public static String UrlDataListRouter = baseURL + "router/list";
	public static String UrlStartDataLiveRealtimeTraffict = baseURL + "router/global_traffic/";
	public static String UrlStopDataLiveRealtimeTraffict = baseURL + "router/stop_global_traffic/";
	public static String UrlStartDataLiveLanTraffict = baseURL + "router/lan_traffic/";
	public static String UrlStopDataLiveLanTraffict = baseURL + "router/stop_lan_traffic/";
	public static String UrlUserAccessed = baseURL + "router/user_access/";
	public static String UrlRouterStatus = baseURL + "router/resource_info/";
	public static String UrlPingTest = baseURL + "router/ping_test/";
	public static String UrlBandwitchTest = baseURL + "router/bandwidth_test/";
	public static String UrlProfile = baseURL + "customer/profile";
	public static String UrlBillingData = baseURL + "customer/billing";
	public static String UrlhistoryBillingData = baseURL + "customer/billing_history";
	public static String UrlPanicButton = baseURL + "customer/panik";
}
