package com.maulana.custommodul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "GmediaClientTools";

	// All Shared Preferences Keys
	public static final String TAG_USERNAME = "username";
	public static final String TAG_ID_SITE = "ID_SITE";
	public static final String TAG_CUSTOMER_NAME = "customer_name";
	public static final String TAG_SITE_NAME = "site_name";
	public static final String TAG_CUSTOMER_ID = "customer_id";
	public static final String TAG_SERVICE_ID = "service_id";
	public static final String TAG_CONTACT_ID = "CONTACT";
	public static final String TAG_USER_ID = "USERID";
	public static final String TAG_TOKEN = "TOKEN";
	public static final String TAG_FCMID = "FCM";


	// Constructor
	public SessionManager(Context context) {
		this.context = context;
		pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 */
	public void createLoginSession(String contact_id, String user_id, String token) {
		editor.putString(TAG_CONTACT_ID, contact_id);
		editor.putString(TAG_USER_ID, user_id);
		editor.putString(TAG_TOKEN, token);
		editor.commit();
	}

	public void createFcmID(String FcmID) {
		editor.putString(TAG_FCMID, FcmID);
		editor.commit();
	}

	/**
	 * Get stored session data
	 */
	public String getToken() {
		return pref.getString(TAG_TOKEN, "");
	}

	public String getUserID() {
		return pref.getString(TAG_USER_ID, "");
	}

	public String getContactID() {
		return pref.getString(TAG_CONTACT_ID, "");
	}

	public String getUserInfo(String key) {
		return pref.getString(key, "");
	}

	public String getUsername() {
		return pref.getString(TAG_USERNAME, "");
	}

	public String getIdSite() {
		return pref.getString(TAG_ID_SITE, "");
	}

	public String getCustomerName() {
		return pref.getString(TAG_CUSTOMER_NAME, "");
	}

	public String getSiteName() {
		return pref.getString(TAG_SITE_NAME, "");
	}

	public String getFcmID() {
		return pref.getString(TAG_FCMID, "");
	}

	public void logoutUser(Intent logoutIntent) {

		// Clearing all data from Shared Preferences
		try {
			editor.clear();
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(logoutIntent);
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	/**
	 * Quick check for login
	 **/
	// Get Login State
	public boolean isLoggedIn() {
		if (getUserID().isEmpty()) {
			return false;
		} else {
			return true;
		}
		/*return pref.getBoolean(IS_LOGIN, false);*/
	}
}
