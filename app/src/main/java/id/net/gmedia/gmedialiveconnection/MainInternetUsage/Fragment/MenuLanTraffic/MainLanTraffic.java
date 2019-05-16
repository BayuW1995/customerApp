package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuLanTraffic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.CustomItem;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainInternetUsage.ModelListRouter;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.MainInternetUsage;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ScrollToTopListview;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainLanTraffic extends Fragment {

	private static Context context;
	private static SessionManager session;
	private View layout;
	private ListView lvTorch;
	private Button btnProses, btnStop;
	private List<CustomItem> listTorch = new ArrayList<>();
	private ListLanTrafficAdapter adapter;
	public static boolean isActive = false;
	private static final String TAG = "TORCH";
	private ProgressBar pbLoading;
	private Spinner dropdownPilihRouter;
	private int start = 0, count = 10;
	private List<ModelListRouter> listRouter;
	private ArrayAdapter<ModelListRouter> adapterDropdownListRouter;
	private static String idListRouter = "";
	private RelativeLayout layoutUtama;
	private ProgressDialog progressDialog;
	private FloatingActionButton btnScrollUp;
	private SwipeRefreshLayout layoutRefresh;

	public MainLanTraffic() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.fragment_main_lan_traffic, container, false);
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

		lvTorch = (ListView) layout.findViewById(R.id.lv_torch);
//		btnProses = (Button) layout.findViewById(R.id.btn_proses_lan_traffic);
//		btnStop = (Button) layout.findViewById(R.id.btn_stop);
		pbLoading = (ProgressBar) layout.findViewById(R.id.pb_loading);
		layoutRefresh = (SwipeRefreshLayout) layout.findViewById(R.id.layoutLanTraffic);
		btnScrollUp = (FloatingActionButton) layout.findViewById(R.id.btnScrollUpListLanTraffic);

//		dropdownPilihRouter = (Spinner) layout.findViewById(R.id.pilihRouter);
//		layoutUtama = (RelativeLayout) layout.findViewById(R.id.layoutLanTraffic);




        /*listTorch.add(new CustomItem(
                "1"
                ,"101.101.101.12"
                ,"Rumah"
                ,"101.101.101.17"
                ,"12 Mb"
                ,"10 Mb"
        ));

        listTorch.add(new CustomItem(
                "1"
                ,"101.101.101.13"
                ,"Rumah Tetangga"
                ,"101.101.101.16"
                ,"11 Mb"
                ,"10 Mb"
        ));

        adapter.notifyDataSetChanged();*/
	}

	private void initEvent() {

		if (!isActive) {

//					pbLoading.setVisibility(View.VISIBLE);
			isActive = true;
			getDataTorch();
		}
		layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (isActive) {
					getDataTorch();
					layoutRefresh.setRefreshing(false);
				}
			}
		});
		/*btnProses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (!isActive) {

//					pbLoading.setVisibility(View.VISIBLE);
					isActive = true;
					getDataTorch();
				}
			}
		});*/

		/*btnStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isActive) {

					disconnectLive();
				}
			}
		});*/
	}

	private void getDataTorch() {
		/*JSONObject jBody = new JSONObject();
		try {
			jBody.put("id_site", session.getIdSite());
			jBody.put("to", InitFirebaseSetting.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String idSite = session.getIdSite();*/
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlStartDataLiveLanTraffict + MainInternetUsage.idListRouter, 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

				pbLoading.setVisibility(View.GONE);
				Log.d(TAG, "onSuccess: " + result);
				listTorch.clear();

				try {
					JSONObject response = new JSONObject(result);
					String status = response.getJSONObject("metadata").getString("status");
					String message = response.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray ja = response.getJSONObject("response").getJSONArray("devices");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = ja.getJSONObject(i);
							listTorch.add(new CustomItem(
									String.valueOf(i)
									, jo.getString("ip_device")
									, jo.getString("name_device")
									, jo.getString("ip_dst")
									, jo.getString("tr_download") + " Mb"
									, jo.getString("tr_upload") + " Mb"
									, jo.getString("tr_download")
									, jo.getString("hostname")
							));
						}

						Collections.sort(listTorch, new Comparator<CustomItem>() {
							@Override
							public int compare(CustomItem lhs, CustomItem rhs) {
								// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

								ItemValidation iv = new ItemValidation();

								double downloadL = iv.parseNullDouble(lhs.getItem7());
								double downloadR = iv.parseNullDouble(rhs.getItem7());

								return downloadL > downloadR ? -1 : (downloadL < downloadR) ? 1 : 0;
							}
						});
						lvTorch.setAdapter(null);
						adapter = new ListLanTrafficAdapter((Activity) context, listTorch);
						lvTorch.setAdapter(adapter);
						lvTorch.setOnScrollListener(new AbsListView.OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView view, int i) {
                                /*int threshold = 1;
                                int countMerchant = listView.getCount();

                                if (i == SCROLL_STATE_IDLE) {
                                    if (listView.getLastVisiblePosition() >= countMerchant - threshold && !isLoading) {

                                        isLoading = true;
                                        listView.addFooterView(footerList);
                                        startIndex += count;
//                                        startIndex = 0;
                                        getMoreData();
                                        //Log.i(TAG, "onScroll: last ");
                                    }
                                }*/
							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								if (view.getFirstVisiblePosition() >= 3) {
									visibleScrollUp();
								} else {
									goneScrollUp();
								}
							}
						});
					} else {
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				adapter.notifyDataSetChanged();

				/*if (isActive) {
					getDataTorch();
				}*/
			}

			@Override
			public void onError(String result) {
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_SHORT).show();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				Log.d(TAG, "onError: " + result);
//				pbLoading.setVisibility(View.GONE);
				if (isActive) {
					getDataTorch();
				}
			}
		});

	}

	@SuppressLint("RestrictedApi")
	private void visibleScrollUp() {
		btnScrollUp.setVisibility(View.VISIBLE);
		btnScrollUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				ScrollToTopListview.smoothScrollToPositionFromTop(lvTorch, 0);
				lvTorch.smoothScrollToPosition(0);
				goneScrollUp();
//				lvTorch.setSelectionAfterHeaderView();
			}
		});
	}

	@SuppressLint("RestrictedApi")
	private void goneScrollUp() {
		btnScrollUp.setVisibility(View.GONE);
	}

	public static void disconnectLive() {

		try {

			ApiVolley.cancelALL();
			isActive = false;
			stopTraffict();
		} catch (Exception e) {
		}

	}

	private static void stopTraffict() {

		JSONObject jBody = new JSONObject();
		String idSite = session.getIdSite();
		/*ApiVolley request = new ApiVolley(context, jBody, "GET", ServerUrl.stopTorchConnection + idSite, 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {

				//disconnectLive();
				Log.d(TAG, "onSuccess: " + result);
			}

			@Override
			public void onError(String result) {

				//disconnectLive();
				Log.d(TAG, "onError: " + result);
			}
		});*/
	}
}
