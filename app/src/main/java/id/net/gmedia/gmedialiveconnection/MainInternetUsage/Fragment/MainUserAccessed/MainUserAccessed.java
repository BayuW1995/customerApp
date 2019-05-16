package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MainUserAccessed;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.CustomItem;
import com.maulana.custommodul.FormatItem;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.net.gmedia.gmedialiveconnection.MainInternetUsage.MainInternetUsage;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainUserAccessed extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();
	private TextView tvDate1, tvDate2;
	private ImageView ivDate1, ivDate2;
	private String dateFrom = "", dateTo = "";
	private String dateFromDbs = "", dateToDbs = "";
	private int start = 0, count = 10;
	private ProgressDialog progressDialog;
	private ListView listView;
	private boolean isLoading = false;
	private View footerList;
	private List<ModelUserAccessed> list;
	private List<ModelUserAccessed> moreList;
	private ListAdapterUserAccessed adapter;

	public MainUserAccessed() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.fragment_main_user_accessed, container, false);
		context = getContext();

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		initUI();
		initEvent();

		return layout;
	}

	private void initUI() {

		tvDate1 = (TextView) layout.findViewById(R.id.tv_date1);
		tvDate2 = (TextView) layout.findViewById(R.id.tv_date2);
		ivDate1 = (ImageView) layout.findViewById(R.id.iv_date1);
		ivDate2 = (ImageView) layout.findViewById(R.id.iv_date2);

		dateFrom = iv.sumDate(iv.getCurrentDate(FormatItem.formatDateDisplay), -1, FormatItem.formatDateDisplay);
		dateTo = iv.getCurrentDate(FormatItem.formatDateDisplay);

		tvDate1.setText(dateFrom);
		tvDate2.setText(dateTo);

		dateFromDbs = iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate);
		dateToDbs = iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate);

		listView = (ListView) layout.findViewById(R.id.lv_user_accesed);
	}

	private void initEvent() {
		prepareDataUserAccessed();
		ivDate1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final Calendar customDate;
				SimpleDateFormat sdf = new SimpleDateFormat(FormatItem.formatDateDisplay);

				Date dateValue = null;

				try {
					dateValue = sdf.parse(dateFrom);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				customDate = Calendar.getInstance();
				final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int date) {
						customDate.set(Calendar.YEAR, year);
						customDate.set(Calendar.MONTH, month);
						customDate.set(Calendar.DATE, date);

						SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
						dateFrom = sdFormat.format(customDate.getTime());
						tvDate1.setText(dateFrom);
						dateFromDbs = iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate);
						prepareDataUserAccessed();
					}
				};

				SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
				new DatePickerDialog(context, date, iv.parseNullInteger(yearOnly.format(dateValue)), dateValue.getMonth(), dateValue.getDate()).show();
			}
		});

		ivDate2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final Calendar customDate;
				SimpleDateFormat sdf = new SimpleDateFormat(FormatItem.formatDateDisplay);

				Date dateValue = null;

				try {
					dateValue = sdf.parse(dateTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				customDate = Calendar.getInstance();
				final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int date) {
						customDate.set(Calendar.YEAR, year);
						customDate.set(Calendar.MONTH, month);
						customDate.set(Calendar.DATE, date);

						SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
						dateTo = sdFormat.format(customDate.getTime());
						tvDate2.setText(dateTo);
						dateToDbs = iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate);
						prepareDataUserAccessed();
					}
				};

				SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
				new DatePickerDialog(context, date, iv.parseNullInteger(yearOnly.format(dateValue)), dateValue.getMonth(), dateValue.getDate()).show();
			}
		});
	}

	private void prepareDataUserAccessed() {
		isLoading = true;
		progressDialog.show();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("start_date", dateFromDbs);
			jBody.put("end_date", dateToDbs);
			jBody.put("start", start);
			jBody.put("limit", count);
			jBody.put("search", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlUserAccessed + MainInternetUsage.idListRouter, -1, new ApiVolley.VolleyCallback() {
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
						JSONArray access_list = object.getJSONObject("response").getJSONArray("access_list");
						for (int i = 0; i < access_list.length(); i++) {
							JSONObject isi = access_list.getJSONObject(i);
							list.add(new ModelUserAccessed(
									isi.getString("ip_device"),
									isi.getString("ip_dst"),
									isi.getString("tr_upload"),
									isi.getString("tr_download"),
									isi.getString("hostname")
							));
						}

						listView.setAdapter(null);
						adapter = new ListAdapterUserAccessed(context, list);
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
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_SHORT).show();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			}
		});
	}

	private void getMoreData() {
		isLoading = true;
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("start_date", dateFromDbs);
			jBody.put("end_date", dateToDbs);
			jBody.put("start", start);
			jBody.put("limit", count);
			jBody.put("search", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlUserAccessed + MainInternetUsage.idListRouter, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				isLoading = false;
				moreList = new ArrayList<>();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray access_list = object.getJSONObject("response").getJSONArray("access_list");
						for (int i = 0; i < access_list.length(); i++) {
							JSONObject isi = access_list.getJSONObject(i);
							moreList.add(new ModelUserAccessed(
									isi.getString("ip_device"),
									isi.getString("ip_dst"),
									isi.getString("tr_upload"),
									isi.getString("tr_download"),
									isi.getString("hostname")
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
				Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Terjadi kesalahan koneksi, harap ulangi kembali nanti", Snackbar.LENGTH_SHORT).show();
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			}
		});
	}

}
