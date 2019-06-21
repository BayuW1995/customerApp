package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuRequest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.net.gmedia.gmedialiveconnection.LoginScreen;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ListAdapterMenuComplaint;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ModelMenuComplaint;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainRequest extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();
	private List<ModelMenuRequest> list;
	private List<ModelMenuRequest> moreList;
	private ListAdapterMenuRequest adapter;
	private TextView tvDate1, tvDate2, txtNoData;
	private ImageView ivDate1, ivDate2;
	private String dateFrom = "", dateTo = "";
	private String dateFromDbs = "", dateToDbs = "";
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private ListView listView;
	private ProgressDialog progressDialog;
	private FloatingActionButton btnAddTicket;
	private String kategori = "";
	private EditText isian;
	private SessionManager session;
	private ImageView imageNoData;

	public MainRequest() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.fragment_main_request, container, false);
		context = getContext();
		session = new SessionManager(context);

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		kategori = "request";

		initUI();
		initEvent();

		return layout;
	}

	private void initUI() {

		tvDate1 = (TextView) layout.findViewById(R.id.tv_date1);
		tvDate2 = (TextView) layout.findViewById(R.id.tv_date2);
		ivDate1 = (ImageView) layout.findViewById(R.id.iv_date1);
		ivDate2 = (ImageView) layout.findViewById(R.id.iv_date2);
		txtNoData = (TextView) layout.findViewById(R.id.txtNoDataRequest);


		dateFrom = iv.sumDate(iv.getCurrentDate(FormatItem.formatDateDisplay), -1, FormatItem.formatDateDisplay);
		dateTo = iv.getCurrentDate(FormatItem.formatDateDisplay);

		tvDate1.setText(dateFrom);
		tvDate2.setText(dateTo);

		dateFromDbs = iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate);
		dateToDbs = iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate);

		listView = (ListView) layout.findViewById(R.id.LvTicketRequest);

		imageNoData = (ImageView) layout.findViewById(R.id.imageNoDataRequest);

		btnAddTicket = (FloatingActionButton) layout.findViewById(R.id.btnAddTicketRequest);
	}

	private void initEvent() {
		prepareDataMenuRequest();
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
						prepareDataMenuRequest();
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
						prepareDataMenuRequest();
					}
				};

				SimpleDateFormat yearOnly = new SimpleDateFormat("yyyy");
				new DatePickerDialog(context, date, iv.parseNullInteger(yearOnly.format(dateValue)), dateValue.getMonth(), dateValue.getDate()).show();
			}
		});
		btnAddTicket.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_tambah_ticket);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				LinearLayout layoutTgl = (LinearLayout) dialog.findViewById(R.id.layoutTglPopupAddTiket);
				layoutTgl.setVisibility(View.GONE);
				isian = (EditText) dialog.findViewById(R.id.etKeteranganPopupInputTicket);
				RelativeLayout btnOK = (RelativeLayout) dialog.findViewById(R.id.btnOKPopupInputTicket);
				btnOK.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						prepareDataAddTicket();
					}

					private void prepareDataAddTicket() {
						progressDialog.show();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("category", kategori);
							jBody.put("description", isian.getText().toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlDataAddTicket, -1, new ApiVolley.VolleyCallback() {
							@Override
							public void onSuccess(String result) {
								if (progressDialog != null && progressDialog.isShowing())
									progressDialog.dismiss();
								dialog.dismiss();
								try {
									JSONObject object = new JSONObject(result);
									String status = object.getJSONObject("metadata").getString("status");
									String message = object.getJSONObject("metadata").getString("message");
									if (status.equals("200")) {
										Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
										prepareDataMenuRequest();
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
								if (progressDialog != null && progressDialog.isShowing())
									progressDialog.dismiss();
								dialog.dismiss();
							}
						});
					}
				});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		});
	}

	private void prepareDataMenuRequest() {
		isLoading = true;
		progressDialog.show();
//		dateFrom = iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate);
//		dateTo = iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate);
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("category", kategori);
			jBody.put("start_date", dateFromDbs);
			jBody.put("end_date", dateToDbs);
			jBody.put("start", start);
			jBody.put("limit", count);
			jBody.put("search", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley reequest = new ApiVolley(context, jBody, "POST", ServerUrl.UrlDataMenuComplaint, -1, new ApiVolley.VolleyCallback() {
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
						JSONObject response = object.getJSONObject("response");
						JSONArray ticket_list = response.getJSONArray("ticket_list");
						for (int i = 0; i < ticket_list.length(); i++) {
							JSONObject isi = ticket_list.getJSONObject(i);
							list.add(new ModelMenuRequest(
									isi.getString("created_at"),
									isi.getString("description"),
									isi.getString("status")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterMenuRequest(context, list);
						listView.setAdapter(adapter);
						listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								final Dialog dialog = new Dialog(context);
								dialog.setContentView(R.layout.popup_rating_bar);
								dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
								dialog.setCanceledOnTouchOutside(false);
								dialog.show();
							}
						});
						listView.setVisibility(View.VISIBLE);
						imageNoData.setVisibility(View.GONE);
						txtNoData.setVisibility(View.GONE);
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
					} else if (status.equals("401")) {
						Intent intent = new Intent(context, LoginScreen.class);
						session.logoutUser(intent);
					} else if (status.equals("404")) {
						listView.setVisibility(View.GONE);
						imageNoData.setVisibility(View.VISIBLE);
						txtNoData.setVisibility(View.VISIBLE);
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
			jBody.put("category", "request");
			jBody.put("start_date", dateFromDbs);
			jBody.put("end_date", dateToDbs);
			jBody.put("start", start);
			jBody.put("limit", count);
			jBody.put("search", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlDataMenuComplaint, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				moreList = new ArrayList<>();
				listView.removeFooterView(footerList);
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray ticket_list = response.getJSONArray("ticket_list");
						Log.d("panjang_list", "" + ticket_list.length());
						for (int i = 0; i < ticket_list.length(); i++) {
							JSONObject isi = ticket_list.getJSONObject(i);
							moreList.add(new ModelMenuRequest(
									isi.getString("created_at"),
									isi.getString("description"),
									isi.getString("status")
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
}
