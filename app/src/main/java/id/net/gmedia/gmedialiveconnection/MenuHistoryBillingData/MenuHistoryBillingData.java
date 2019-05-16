package id.net.gmedia.gmedialiveconnection.MenuHistoryBillingData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MenuHistoryBillingData extends AppCompatActivity {
	private ProgressDialog progressDialog;
	private Context context;
	private ListView listView;
	private TextView tvTitle;
	private ImageView ivMenu;
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private List<ModelHistoryBillingData> list;
	private List<ModelHistoryBillingData> moreList;
	private ListAdapterHistoryBillingData adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_history_billing_data);
		context = this;

		LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);

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
		listView = (ListView) findViewById(R.id.lv_history_billing_data);

		tvTitle.setText("History Tagihan");
	}

	private void initEvent() {
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		prepareDataHistoryBillingData();
	}

	private void prepareDataHistoryBillingData() {
		isLoading = true;
		progressDialog.show();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("start", start);
			jBody.put("limit", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlhistoryBillingData, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				isLoading = false;
				list = new ArrayList<>();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray tagihan_list = object.getJSONObject("response").getJSONArray("tagihan_list");
						for (int i = 0; i < tagihan_list.length(); i++) {
							JSONObject isi = tagihan_list.getJSONObject(i);
							list.add(new ModelHistoryBillingData(
									isi.getString("nomor_tagihan"),
									isi.getString("jumlah_tagihan"),
									isi.getString("tanggal"),
									isi.getString("total_bayar"),
									isi.getString("tanggal_jatuh_tempo")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterHistoryBillingData(context, list);
						listView.setAdapter(adapter);
						listView.setOnScrollListener(new AbsListView.OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView absListView, int i) {

							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								if (view.getLastVisiblePosition() == totalItemCount - 1 && listView.getCount() > (count - 1) && !isLoading) {
									isLoading = true;
									listView.addFooterView(footerList);
									start += count;
//                                        startIndex = 0;
									getMoreData();
									//Log.i(TAG, "onScroll: last ");
								}
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

	private void getMoreData() {
		isLoading = true;
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("start", start);
			jBody.put("limit", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlhistoryBillingData, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				moreList = new ArrayList<>();
				listView.removeFooterView(footerList);
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray tagihan_list = object.getJSONObject("response").getJSONArray("tagihan_list");
						for (int i = 0; i < tagihan_list.length(); i++) {
							JSONObject isi = tagihan_list.getJSONObject(i);
							moreList.add(new ModelHistoryBillingData(
									isi.getString("nomor_tagihan"),
									isi.getString("jumlah_tagihan"),
									isi.getString("tanggal"),
									isi.getString("total_bayar"),
									isi.getString("tanggal_jatuh_tempo")
							));
						}
						isLoading = false;
						listView.removeFooterView(footerList);
						if (adapter != null) adapter.addMoreData(moreList);
					} else {
						Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String result) {
				isLoading = false;
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_LONG).show();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			}
		});
	}

	public String doubleToStringFull(Double number) {
		return String.format("%s", number).replace(",", ".");
	}

	public static Double parseNullDouble(String s) {
		double result = 0;
		if (s != null && s.length() > 0) {
			try {
				result = Double.parseDouble(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String ChangeToRupiahFormat(String number) {

		double value = parseNullDouble(number);

		NumberFormat format = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

		symbols.setCurrencySymbol("Rp ");
		((DecimalFormat) format).setDecimalFormatSymbols(symbols);
		format.setMaximumFractionDigits(0);

		String hasil = String.valueOf(format.format(value));

		return hasil;
	}
}
