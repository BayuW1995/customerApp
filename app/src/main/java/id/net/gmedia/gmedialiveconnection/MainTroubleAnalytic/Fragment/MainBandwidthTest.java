package id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.utils.SpeedTestUtils;
import id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.MainTroubleAnalytic;
import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainBandwidthTest extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();
	private static ItemValidation ivStatic = new ItemValidation();
	private int i;
	private Button btnProses;
	private static TextView tvUpload, tvDownload,txtBandwidthTest;
//	private static PointerSpeedometer psTest;
	private static float maxLenth = 10;
	public static boolean isBandwitchTest = false;

	//download
	private final static int SOCKET_TIMEOUT = 5000;
	//private final static String SPEED_TEST_SERVER_URI_DL = "http://ipv4.ikoula.testdebit.info/10M.iso";
	private final static String SPEED_TEST_SERVER_URI_DL = "https://erpsmg.gmedia.id/client_tools/file/10M.iso";

	//upload

	/**
	 * spedd examples server uri.
	 */
	private static final String SPEED_TEST_SERVER_URI_UL = "http://ipv4.ikoula.testdebit.info/";
	String fileName = SpeedTestUtils.generateFileName() + ".txt";
	//private static final String SPEED_TEST_SERVER_URI_UL = "ftp://speedtest.tele2.net/upload/" + fileName;
	//private static final String SPEED_TEST_SERVER_URI_UL = "http://192.168.10.104/perkasamobile/pol/file";

	/**
	 * upload 10Mo file size.
	 */
//    private static final int FILE_SIZE = 10000000;
	private static final int FILE_SIZE = 50000;
	private static final String TAG = "PING";
	private static int durationSpeed = 5000;
	private static LinearLayout layoutAngkaTraffic;
	private SessionManager session;

	public MainBandwidthTest() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.fragment_main_ping, container, false);
		context = getContext();

		session = new SessionManager(context);

		initUI();
		initEvent();

		return layout;
	}

	private void initUI() {

		tvUpload = (TextView) layout.findViewById(R.id.tv_upload);
		tvDownload = (TextView) layout.findViewById(R.id.tv_download);
//		psTest = (PointerSpeedometer) layout.findViewById(R.id.ps_test);
//		txtProcess = (TextView) layout.findViewById(R.id.txtProcessMainPing);
//		layoutAngkaTraffic = (LinearLayout) layout.findViewById(R.id.layoutAngkaTraffic);
		btnProses = (Button) layout.findViewById(R.id.btn_proses);
		txtBandwidthTest=(TextView)layout.findViewById(R.id.txtBandwidthTest);
//		psTest.speedTo(0);

	}

	private void initEvent() {

		btnProses.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/*maxLenth = 10;
				psTest.setMaxSpeed(maxLenth);
//                psTest.speedTo(maxLenth);
				tvDownload.setText("Processing...");
				tvUpload.setText("");
//                psTest.speedTo(5,10000);
				new getDownladTraffic().execute();*/
				prepareDataBandwitchTest();
				/*for (int i = 0; i < 5; i++) {
					psTest.setUnit("Mb/s");
					psTest.setMaxSpeed(100);
					psTest.setAccelerate(0.1f);
					if (i == 0) {
						psTest.speedTo(10,1000);
					} else if (i == 1) {
						psTest.speedTo(20,1000);
					} else if (i == 2) {
						psTest.speedTo(30,1000);
					} else if (i == 3) {
						psTest.speedTo(50,1000);
					} else {
						psTest.speedTo(0,1000);
					}
				}*/
				/*if (isBandwitchTest) {
					isBandwitchTest = false;
					ApiVolley.cancelALL();
				} else {
					isBandwitchTest = true;
					prepareDataBandwitchTest();
				}*/
			}

		});
	}

	private void prepareDataBandwitchTest() {
		tvDownload.setText("");
		tvUpload.setText("");
		isBandwitchTest = true;
		String fcm_id = InitFirebaseSetting.token;
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("fcm_id", fcm_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlBandwitchTest + MainTroubleAnalytic.idListRouter, 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
//				psTest.speedTo(0);
//				txtProcess.setVisibility(View.GONE);
//				layoutAngkaTraffic.setVisibility(View.VISIBLE);
				isBandwitchTest = false;
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						tvDownload.setText(response.getString("download") + " Mb/s");
						tvUpload.setText(response.getString("upload") + " Mb/s");
						txtBandwidthTest.setText("0");
					} else {
//						psTest.speedTo(0);
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
//				psTest.speedTo(0);
				isBandwitchTest = false;
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_LONG).show();
			}
		});
	}

	public static void addBandwitchTest(String download, String type, String upload, String resultDownload) {
//		txtProcess.setVisibility(View.VISIBLE);
//		layoutAngkaTraffic.setVisibility(View.GONE);
		if (!type.equals("")) {
			if (type.equals("download")) {
				if (!download.equals("")) {
//					txtProcess.setText("Process download");
					tvDownload.setText("Processing...");
					txtBandwidthTest.setText(download);
//					psTest.setUnit("Mb/s");
//					psTest.setMaxSpeed(ivStatic.parseNullFloat(download) + 10f);
//					psTest.speedTo(ivStatic.parseNullFloat(download), 1000);
					/*psTest.setUnit("Mb/s");
					psTest.setMaxSpeed(200);
					psTest.speedTo(150,10000);*/
				}
			} else if (type.equals("transisi")) {
				if (!resultDownload.equals("0")) {
					tvDownload.setText(resultDownload + " Mb/s");
					txtBandwidthTest.setText("0");
				}
//				psTest.speedTo(0);
			} else if (type.equals("upload")) {
				if (!upload.equals("")) {
					if (!resultDownload.equals("0")) {
						tvDownload.setText(resultDownload + " Mb/s");
					}
//					psTest.speedTo(0);
//					txtProcess.setText("Process upload");
					tvUpload.setText("Processing...");
					txtBandwidthTest.setText(upload);
//					psTest.setUnit("Mb/s");
//					psTest.setMaxSpeed(ivStatic.parseNullFloat(upload) + 10f);
//					psTest.speedTo(ivStatic.parseNullFloat(upload), 1000);
					/*psTest.setUnit("Mb/s");
					psTest.setMaxSpeed(200);
					psTest.speedTo(150,10000);*/
				}
			} else if (type.equals("selesai")) {
				if (!resultDownload.equals("0")) {
					tvDownload.setText(resultDownload + " Mb/s");
				}
//				psTest.speedTo(0);
			}
		}

	}

	public class getDownladTraffic extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			// instantiate speed examples
			final SpeedTestSocket speedTestSocket = new SpeedTestSocket();

			//set timeout for download
			speedTestSocket.setSocketTimeout(SOCKET_TIMEOUT);

			// add a listener to wait for speed examples completion and progress
			speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

				@Override
				public void onCompletion(final SpeedTestReport report) {

					//called when download/upload is complete
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {

							/*psTest.setUnit(getUnit(report.getTransferRateBit()));
							psTest.setMaxSpeed(getSpeed(report.getTransferRateBit()) + 10);
							psTest.speedTo(getSpeed(report.getTransferRateBit()));*/

							tvDownload.setText(iv.floatToString(getSpeed(report.getTransferRateBit())) + " " + getUnit(report.getTransferRateBit()));
							tvUpload.setText("");
							maxLenth = 10;
//                            psTest.speedTo(maxLenth);
							mStartTX = 0;
							new getUploadTraffic().execute();
						}
					});
				}

				@Override
				public void onError(final SpeedTestError speedTestError, final String errorMessage) {

					Log.d(TAG, "onError: " + errorMessage);
				}

				@Override
				public void onProgress(final float percent, final SpeedTestReport downloadReport) {

					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {

							maxLenth = getSpeed(downloadReport.getTransferRateBit()) + 10;
//							psTest.setMaxSpeed(maxLenth);
//							psTest.setLowSpeedPercent(10);
//							psTest.setUnit(getUnit(downloadReport.getTransferRateBit()));
//							psTest.speedTo(getSpeed(downloadReport.getTransferRateBit()), durationSpeed);

							tvDownload.setText("Processing...");
							tvUpload.setText("");
						}
					});
				}
			});

			speedTestSocket.startDownload(SPEED_TEST_SERVER_URI_DL);

			return null;
		}
	}

	private long mStartTX = 0;

	public class getUploadTraffic extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			// instantiate speed examples
			final SpeedTestSocket speedTestSocket = new SpeedTestSocket();

			//set timeout for download
			speedTestSocket.setSocketTimeout(SOCKET_TIMEOUT);

			//speedTestSocket.setUploadStorageType(UploadStorageType.FILE_STORAGE);

			// add a listener to wait for speed examples completion and progress
			speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

				@Override
				public void onCompletion(final SpeedTestReport report) {

					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {

							/*psTest.setUnit(getUnit(report.getTransferRateBit()));
							psTest.setMaxSpeed(getSpeed(report.getTransferRateBit()) + 10);
							psTest.speedTo(getSpeed(report.getTransferRateBit()));

							tvUpload.setText(iv.floatToString(getSpeed(report.getTransferRateBit())) + " " + getUnit(report.getTransferRateBit()));
							psTest.speedTo(0);*/
						}
					});
				}

				@Override
				public void onError(final SpeedTestError speedTestError, final String errorMessage) {

					Log.d(TAG, "onError: " + errorMessage);
				}

				@Override
				public void onProgress(final float percent, final SpeedTestReport downloadReport) {

					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {

							maxLenth = getSpeed(downloadReport.getTransferRateBit()) + 10;
							/*psTest.setMaxSpeed(maxLenth);
							psTest.setLowSpeedPercent(10);
							psTest.setUnit(getUnit(downloadReport.getTransferRateBit()));
							psTest.speedTo(getSpeed(downloadReport.getTransferRateBit()), durationSpeed);*/

							// Get running processes
                            /*long txBytes = TrafficStats.getTotalRxBytes() - mStartTX;
                            mStartTX = TrafficStats.getTotalRxBytes();
                            String uploadX = Long.toString(txBytes) + " bytes";
                            if (txBytes >= 1024) {
                                // KB or more
                                long txKb = txBytes / 1024;
                                uploadX = Long.toString(txKb) + " KBs";
                                if (txKb >= 1024) {
                                    // MB or more
                                    long txMB = txKb / 1024;
                                    uploadX = Long.toString(txMB) + " MBs";
                                    if (txMB >= 1024) {
                                        // GB or more
                                        long txGB = txMB / 1024;
                                        uploadX = Long.toString(txGB);
                                    }// rxMB>1024
                                }// rxKb > 1024
                            }// rxBytes>=1024

                            //Log.e("Total", "Bytes received " + android.net.TrafficStats.getMobileTxBytes());
                            Log.e("Total", "Bytes received " + uploadX);*/
							tvUpload.setText("Processing...");
						}
					});
				}
			});

			speedTestSocket.startUpload(SPEED_TEST_SERVER_URI_UL, FILE_SIZE);

			return null;
		}
	}

	private static float getSpeed(BigDecimal size) {

		long sizeCeil = size.longValue();

		if (sizeCeil <= 0)
			return 0;

		final String[] units = new String[]{"b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s"};
		int digitGroups = (int) (Math.log10(sizeCeil) / Math.log10(1024));

		return (float) (sizeCeil / Math.pow(1024, digitGroups));
	}

	private static String getUnit(BigDecimal size) {

		long sizeCeil = size.longValue();

		if (sizeCeil <= 0)
			return "0";

		final String[] units = new String[]{"b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s"};
		int digitGroups = (int) (Math.log10(sizeCeil) / Math.log10(1024));

		return units[digitGroups];
	}
}
