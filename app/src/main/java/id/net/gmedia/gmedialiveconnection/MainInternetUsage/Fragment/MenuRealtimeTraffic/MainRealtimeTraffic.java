package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuRealtimeTraffic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainInternetUsage.MainInternetUsage;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.ModelListRouter;
import id.net.gmedia.gmedialiveconnection.NotificationUtils.InitFirebaseSetting;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainRealtimeTraffic extends Fragment {

	private static Context context;
	private static SessionManager session;
	private View layout;
	private static GraphView gvLiveDownload;
	private static LineGraphSeries<DataPoint> seriesDownload, seriesUpload;
	private static int lastXDownload = 0, lastXUpload = 0;
	private static Viewport viewportDownload;
	private static List<Double> listYDownload = new ArrayList<>(), listYUpload = new ArrayList<>();
	private static int maxList = 10;
	public static boolean isActive = false;
	private static Button btnProses;
	private static String TAG = "LIVECHART";
	private static ItemValidation iv = new ItemValidation();
	private static TextView tvDownload, tvUpload;
	private ProgressDialog progressDialog;
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private List<ModelListRouter> listRouter;
	private ArrayAdapter<ModelListRouter> adapterDropdownListRouter;
	private Spinner dropdownListRouter;
	private static String idListRouter = "";
	private LinearLayout layoutRealtimeTrafic;

	public MainRealtimeTraffic() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		layout = inflater.inflate(R.layout.fragment_main_live_chart, container, false);
		context = getContext();
		session = new SessionManager(context);

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		initUI();
		initEvent();

		return layout;
	}

	private void initUI() {

		gvLiveDownload = (GraphView) layout.findViewById(R.id.gv_live_download);
		tvDownload = (TextView) layout.findViewById(R.id.tv_download);
		tvUpload = (TextView) layout.findViewById(R.id.tv_upload);
		btnProses = (Button) layout.findViewById(R.id.btn_proses);
//		dropdownListRouter = (Spinner) layout.findViewById(R.id.pilihRouter);
		layoutRealtimeTrafic = (LinearLayout) layout.findViewById(R.id.layoutRealtimeTraffic);

		seriesDownload = new LineGraphSeries<DataPoint>();
		seriesDownload.setColor(context.getResources().getColor(R.color.color_trafd));
		seriesUpload = new LineGraphSeries<DataPoint>();
		seriesUpload.setColor(context.getResources().getColor(R.color.color_trafu));

		gvLiveDownload.addSeries(seriesDownload);
		gvLiveDownload.addSeries(seriesUpload);

		// customize a little bit viewport
		viewportDownload = gvLiveDownload.getViewport();
		viewportDownload.setYAxisBoundsManual(true);
		viewportDownload.setMinY(0);
		viewportDownload.setScrollable(true);
		viewportDownload.setXAxisBoundsManual(true);
		viewportDownload.setBorderColor(Color.WHITE);

        /*viewportUpload = gvLiveUpload.getViewport();
        viewportUpload.setYAxisBoundsManual(true);
        viewportUpload.setMinY(0);
        viewportUpload.setScrollable(true);
        viewportUpload.setXAxisBoundsManual(true);*/

        /*new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		disconnectLive();
	}

	private void initEvent() {
//		prepareDataListRouter();
		btnProses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isActive) {

					disconnectLive();
					/*btnProses.setText("Mulai");
					btnProses.setTextColor(context.getResources().getColor(R.color.color_default));
					btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_blue));*/
					btnProses.setText("Mulai");
					btnProses.setTextColor(context.getResources().getColor(R.color.color_white));
					btnProses.setBackground(context.getResources().getDrawable(R.drawable.bcg_btn_mulai_trouble_analytic));
				} else {

					isActive = true;

					lastXDownload = 0;
					listYDownload = new ArrayList<>();

					lastXUpload = 0;
					listYUpload = new ArrayList<>();

					/*btnProses.setText("Berhenti");
					btnProses.setTextColor(context.getResources().getColor(R.color.color_red));
					btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_red));*/
					btnProses.setText("Berhenti");
					btnProses.setTextColor(context.getResources().getColor(R.color.color_white));
					btnProses.setBackground(context.getResources().getDrawable(R.drawable.bcg_btn_berhenti_trouble_analytic));

					getDataLiveTraffict();
				}


			}
		});
	}

	private void prepareDataListRouter() {
		isLoading = true;
		progressDialog.show();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("start", start);
			jBody.put("limit", count);
			jBody.put("search", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlDataListRouter, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				isLoading = false;
				listRouter = new ArrayList<>();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray router_list = response.getJSONArray("router_list");
						for (int i = 0; i < router_list.length(); i++) {
							JSONObject isi = router_list.getJSONObject(i);
							listRouter.add(new ModelListRouter(
									isi.getString("id"),
									isi.getString("name")
							));
						}
						listRouter.add(new ModelListRouter(
								"0",
								"Silahkan pilih..."
						));
						final int listSize = listRouter.size() - 1;
						adapterDropdownListRouter = new ArrayAdapter<ModelListRouter>(context, R.layout.layout_simple_list, listRouter) {
							@Override
							public int getCount() {
								return listSize;
							}
						};
						dropdownListRouter.setAdapter(adapterDropdownListRouter);
						dropdownListRouter.setSelection(listSize);
						dropdownListRouter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								ModelListRouter model = listRouter.get(position);
								idListRouter = model.getId();
								if (!idListRouter.equals("0")) {
									Log.d("idList", idListRouter);
									layoutRealtimeTrafic.setVisibility(View.VISIBLE);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
					} else {
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_LONG).show();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			}
		});
	}

	private void getDataLiveTraffict() {
		String fcm_id = InitFirebaseSetting.token;
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("fcm_id", fcm_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlStartDataLiveRealtimeTraffict + MainInternetUsage.idListRouter, 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {

			}

			@Override
			public void onError(String result) {

				disconnectLive();
				Log.d(TAG, "onError: ");
			}
		});
	}

	public static void addDownloadEntry(double trDownload, double trUpload) {
		// here, we choose to display max 10 points on the viewport and we scroll to end
		if (isActive) {
			int x = lastXDownload++;
			seriesDownload.appendData(new DataPoint(x, trDownload), true, 11);
			if (listYDownload.size() > maxList) {

				listYDownload.remove(0);
			}


			seriesUpload.appendData(new DataPoint(x, trUpload), true, 11);
			if (listYUpload.size() > maxList) {

				listYUpload.remove(0);
			}

			listYDownload.add(trDownload);
			listYUpload.add(trUpload);
			viewportDownload.setMaxY(getMaxY());
			viewportDownload.setMinX(getMinX(x));
			viewportDownload.setMaxX(x);

			tvDownload.setText("Download " + iv.doubleToStringFull(trDownload) + " Mb");
			tvUpload.setText("Upload " + iv.doubleToStringFull(trUpload) + " Mb");
		}
	}

    /*public static void addUploadEntry(double trUpload) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        if(isActive){

            int x = lastXUpload++;
            seriesUpload.appendData(new DataPoint(x, trUpload), true, 11);
            if(listYUpload.size() > maxList){

                listYUpload.remove(0);
            }

            listYUpload.add(trUpload);
            viewportUpload.setMaxY(getMaxY(listYUpload));
            viewportUpload.setMinX(getMinX(x));
            viewportUpload.setMaxX(x);
            viewportUpload.setBorderColor(Color.RED);
            tvUpload.setText("Upload " + iv.doubleToStringFull(trUpload) + " Mb");
        }
    }*/

	private static int getMaxY() {

		int max = 0;

		for (Double d : listYUpload) {

			if (max < d) max = (int) Math.ceil(d);
		}

		for (Double d : listYDownload) {

			if (max < d) max = (int) Math.ceil(d);
		}

		max = (int) Math.ceil(1.5 * max);
		return max;
	}

	private static int getMinX(int x) {

		int min = 0;

		if (x - maxList > 0) min = x - maxList;

		return min;
	}

	public static void disconnectLive() {

		try {
			ApiVolley.cancelALL();
			isActive = false;
			btnProses.setText("Mulai");
			btnProses.setTextColor(context.getResources().getColor(R.color.color_default));
			btnProses.setBackground(context.getResources().getDrawable(R.drawable.btn_oval_blue));
			gvLiveDownload.removeAllSeries();
			//gvLiveUpload.removeAllSeries();

			seriesDownload = new LineGraphSeries<DataPoint>();
			seriesDownload.setColor(context.getResources().getColor(R.color.color_trafd));
			seriesUpload = new LineGraphSeries<DataPoint>();
			seriesUpload.setColor(context.getResources().getColor(R.color.color_trafu));

			gvLiveDownload.addSeries(seriesDownload);
			gvLiveDownload.addSeries(seriesUpload);
			tvDownload.setText("Download");
			tvUpload.setText("Upload");

			stopTraffict();
		} catch (Exception e) {
		}
	}

	private static void stopTraffict() {

		/*JSONObject jBody = new JSONObject();
		String idSite = session.getIdSite();*/
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlStopDataLiveRealtimeTraffict + idListRouter, 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					} else {
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//disconnectLive();
				Log.d(TAG, "onSuccess: ");
			}

			@Override
			public void onError(String result) {

				//disconnectLive();
				Log.d(TAG, "onError: ");
			}
		});
	}
}
