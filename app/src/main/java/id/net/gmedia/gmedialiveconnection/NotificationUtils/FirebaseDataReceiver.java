package id.net.gmedia.gmedialiveconnection.NotificationUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.maulana.custommodul.ItemValidation;

import id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuRealtimeTraffic.MainRealtimeTraffic;
import id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.Fragment.MainBandwidthTest;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

	private final String TAG = "FirebaseDataReceiver";
	private ItemValidation iv = new ItemValidation();

	public void onReceive(Context context, Intent intent) {

		Bundle dataBundle = intent.getExtras();
		if (dataBundle != null) {

			String trDownload = dataBundle.getString("tr_download");
			String trUpload = dataBundle.getString("tr_upload");

			String downloadBandwitchTest = dataBundle.getString("traffic_download");
			String type = dataBundle.getString("type");
			String uploadBandwitchTest = dataBundle.getString("traffic_upload");
			String resultDownload = dataBundle.getString("result_download");
			//Log.d(TAG, "onReceive: ");

			if (MainRealtimeTraffic.isActive) {

				MainRealtimeTraffic.addDownloadEntry(iv.parseNullDouble(trDownload), iv.parseNullDouble(trUpload));
				Log.d("jajal", String.valueOf(iv.parseNullDouble(trDownload)) + " " + String.valueOf(iv.parseNullDouble(trUpload)));
				//MainRealtimeTraffic.addUploadEntry(iv.parseNullDouble(trUpload));
			}
			if (MainBandwidthTest.isBandwitchTest) {
				MainBandwidthTest.addBandwitchTest(iv.parseNullString(downloadBandwitchTest), iv.parseNullString(type),
						iv.parseNullString(uploadBandwitchTest), iv.parseNullString(resultDownload));
			}
		}
	}
}