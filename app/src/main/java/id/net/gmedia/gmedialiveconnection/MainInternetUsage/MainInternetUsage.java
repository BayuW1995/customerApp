package id.net.gmedia.gmedialiveconnection.MainInternetUsage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.LoginScreen;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuRealtimeTraffic.MainRealtimeTraffic;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MainUserAccessed.MainUserAccessed;
import id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuLanTraffic.MainLanTraffic;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainInternetUsage extends AppCompatActivity {

	private Context context;
	private TextView tvTitle;
	private ImageView ivMenu;
	private RadioGroup rbTab;
	private RadioButton rbTab1, rbTab2, rbTab3;
	private static int stateFragment = -1;
	private int state = 0;
	private Spinner dropdownPilihRouter;
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private ProgressDialog progressDialog;
	public static String idListRouter = "";
	private List<ModelListRouter> listRouter;
	private ArrayAdapter<ModelListRouter> adapterDropdownListRouter;
	private LinearLayout layoutUtama;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_internet_usage);

		context = this;
		session = new SessionManager(context);

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		initUI();
		initEvent();
	}

	private void initUI() {

		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivMenu = (ImageView) findViewById(R.id.iv_menu);
		dropdownPilihRouter = (Spinner) findViewById(R.id.dropdownPilihRouter);
		layoutUtama = (LinearLayout) findViewById(R.id.layoutUtamaInternetUsage);
		rbTab = (RadioGroup) findViewById(R.id.rg_tab);
		rbTab1 = (RadioButton) findViewById(R.id.rb_tab1);
		rbTab2 = (RadioButton) findViewById(R.id.rb_tab2);
		rbTab3 = (RadioButton) findViewById(R.id.rb_tab3);

		tvTitle.setText("Internet Usage");
	}

	private void initEvent() {

		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		prepareDataDropdownPilihRouter();

		rbTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {

				int checkedID = radioGroup.getCheckedRadioButtonId();
				if (checkedID == rbTab1.getId()) {

					ChangeFragment(0);
				} else if (checkedID == rbTab2.getId()) {

					ChangeFragment(1);
				} else if (checkedID == rbTab3.getId()) {

					ChangeFragment(2);
				}
			}
		});


	}

	private void prepareDataDropdownPilihRouter() {
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
						dropdownPilihRouter.setAdapter(adapterDropdownListRouter);
						dropdownPilihRouter.setSelection(listSize);
						dropdownPilihRouter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								ModelListRouter model = listRouter.get(position);
								idListRouter = model.getId();
								if (!idListRouter.equals("0")) {
									Log.d("idList", idListRouter);
									layoutUtama.setVisibility(View.VISIBLE);

									if (stateFragment == -1) {

										ChangeFragment(1);
										ChangeFragment(0);

										rbTab1.setChecked(true);
									}
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
					} else if (status.equals("401")) {
						Intent intent = new Intent(context, LoginScreen.class);
						session.logoutUser(intent);
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

	@Override
	protected void onDestroy() {

		if (MainRealtimeTraffic.isActive) MainRealtimeTraffic.disconnectLive();
		if (MainLanTraffic.isActive) MainLanTraffic.disconnectLive();
		super.onDestroy();
	}

	private void ChangeFragment(int stateChecked) {

		if (MainRealtimeTraffic.isActive) MainRealtimeTraffic.disconnectLive();
		if (MainLanTraffic.isActive) MainLanTraffic.disconnectLive();
		stateFragment = stateChecked;

		switch (stateChecked) {
			case 0:
				//setTitle("Live Chart");
				fragment = new MainRealtimeTraffic();
				break;
			case 1:
				setTitle(context.getResources().getString(R.string.title_dashboard));
				fragment = new MainLanTraffic();
				break;
			case 2:
				//setTitle("Ping");
				//fragment = new MainBandwidthTest();
				fragment = new MainUserAccessed();
				break;
			default:
				//setTitle("Live Chart");
				fragment = new MainRealtimeTraffic();
				break;
		}

		if (stateChecked > state) {

			callFragment(context, fragment);
		} else if (stateChecked < state) {
			callFragmentBack(context, fragment);
		}

		state = stateChecked;
	}

	private static Fragment fragment;

	private static void callFragment(Context context, Fragment fragment) {
		((AppCompatActivity) context).getSupportFragmentManager()
				.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
				.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
				.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName())
				.addToBackStack(null)
				.commit();
	}

	private static void callFragmentBack(Context context, Fragment fragment) {
		((AppCompatActivity) context).getSupportFragmentManager()
				.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down)
				.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
				.replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName())
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();

		if (state != 0) {

			rbTab1.setChecked(true);
		} else {

			stateFragment = -1;
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	}
}
