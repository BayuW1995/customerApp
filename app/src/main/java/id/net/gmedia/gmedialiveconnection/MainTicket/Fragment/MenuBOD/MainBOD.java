package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuBOD;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainBOD extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();

	private TextView tvDate1, tvDate2, txtNoData;
	private ImageView ivDate1, ivDate2;
	private String dateFrom = "", dateTo = "";
	private String dateFromDbs = "", dateToDbs = "";
	private String dateFromAddTicket = "", dateToAddTicket = "";
	private String dateFromDbsAddTicket = "", dateToDbsAddTicket = "";
	private Date dateStart = null, dateEnd = null;
	private List<ModelMenuBOD> list;
	private List<ModelMenuBOD> moreList;
	private ListAdapterMenuBOD adapter;
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private ListView listView;
	private ProgressDialog progressDialog;
	private FloatingActionButton btnAddTicket;
	private String kategori = "";
	private EditText isian;
	private SessionManager session;
	private ImageView layoutNoData;

	public MainBOD() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.fragment_main_bod, container, false);
		context = getContext();
		session = new SessionManager(context);

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		kategori = "bod";

		initUI();
		initEvent();

		return layout;
	}


	private void initUI() {

		tvDate1 = (TextView) layout.findViewById(R.id.tv_date1);
		tvDate2 = (TextView) layout.findViewById(R.id.tv_date2);
		ivDate1 = (ImageView) layout.findViewById(R.id.iv_date1);
		ivDate2 = (ImageView) layout.findViewById(R.id.iv_date2);
		txtNoData = (TextView) layout.findViewById(R.id.txtNoDataBOD);

		dateFrom = iv.sumDate(iv.getCurrentDate(FormatItem.formatDateDisplay), -1, FormatItem.formatDateDisplay);
		dateTo = iv.getCurrentDate(FormatItem.formatDateDisplay);

		tvDate1.setText(dateFrom);
		tvDate2.setText(dateTo);

		dateFromDbs = iv.ChangeFormatDateString(dateFrom, FormatItem.formatDateDisplay, FormatItem.formatDate);
		dateToDbs = iv.ChangeFormatDateString(dateTo, FormatItem.formatDateDisplay, FormatItem.formatDate);

		listView = (ListView) layout.findViewById(R.id.LvTicketBOD);

		layoutNoData = (ImageView) layout.findViewById(R.id.imageNoDataBOD);

		btnAddTicket = (FloatingActionButton) layout.findViewById(R.id.btnAddTicketBOD);
	}

	private void initEvent() {
		prepareDataMenuBOD();
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
						prepareDataMenuBOD();

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
						prepareDataMenuBOD();
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
				layoutTgl.setVisibility(View.VISIBLE);

				final TextView txtStartDate = (TextView) dialog.findViewById(R.id.txtStartDateTambahTiket);
				ImageView btnStartDate = (ImageView) dialog.findViewById(R.id.btnStartDateTambahTiket);
				btnStartDate.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						final Calendar customDate = Calendar.getInstance();
						final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int year, int month, int date) {

								customDate.set(Calendar.YEAR, year);
								customDate.set(Calendar.MONTH, month);
								customDate.set(Calendar.DATE, date);

								SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
								dateFromAddTicket = sdFormat.format(customDate.getTime());

								try {
									dateStart = sdFormat.parse(dateFromAddTicket);
								} catch (ParseException e) {
									e.printStackTrace();
								}

								txtStartDate.setText(dateFromAddTicket);
								txtStartDate.setAlpha(1);
								dateFromDbsAddTicket = iv.ChangeFormatDateString(dateFromAddTicket, FormatItem.formatDateDisplay, FormatItem.formatDateDbsIbnu);

							}
						};
						new DatePickerDialog(context, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();

					}
				});

				final TextView txtEndDate = (TextView) dialog.findViewById(R.id.txtEndDateTambahTiket);
				ImageView btnEndDate = (ImageView) dialog.findViewById(R.id.btnEndDateTambahTiket);
				btnEndDate.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						final Calendar customDate = Calendar.getInstance();
						final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int year, int month, int date) {

								customDate.set(Calendar.YEAR, year);
								customDate.set(Calendar.MONTH, month);
								customDate.set(Calendar.DATE, date);

								SimpleDateFormat sdFormat = new SimpleDateFormat(FormatItem.formatDateDisplay, Locale.US);
								dateToAddTicket = sdFormat.format(customDate.getTime());

								try {
									dateEnd = sdFormat.parse(dateToAddTicket);
								} catch (ParseException e) {
									e.printStackTrace();
								}

								if (dateEnd.getTime() < dateStart.getTime()) {
									Snackbar.make(((Activity) context).findViewById(android.R.id.content), "tanggal akhir tidak valid", Snackbar.LENGTH_LONG).show();
								} else {
									txtEndDate.setText(dateToAddTicket);
									txtEndDate.setAlpha(1);
									dateToDbsAddTicket = iv.ChangeFormatDateString(dateToAddTicket, FormatItem.formatDateDisplay, FormatItem.formatDateDbsIbnu);

								}
							}
						};
						new DatePickerDialog(context, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();

					}
				});

				isian = (EditText) dialog.findViewById(R.id.etKeteranganPopupInputTicket);

				RelativeLayout btnOK = (RelativeLayout) dialog.findViewById(R.id.btnOKPopupInputTicket);
				btnOK.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (dateFromDbsAddTicket.equals("")) {
							Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Silahkan isi tanggal mulai", Snackbar.LENGTH_LONG).show();
							return;
						} else if (dateToDbsAddTicket.equals("")) {
							Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Silahkan isi tanggal akhir", Snackbar.LENGTH_LONG).show();
							return;
						} else if (isian.getText().toString().equals("")) {
							isian.setError("silahkan isi keterangan terlebih dahulu");
							isian.requestFocus();
						} else {
							prepareDataAddTicket();
						}
					}

					private void prepareDataAddTicket() {
						progressDialog.show();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("category", kategori);
							jBody.put("description", isian.getText().toString());
							jBody.put("start_bod", dateFromDbsAddTicket);
							jBody.put("end_bod", dateToDbsAddTicket);

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
										prepareDataMenuBOD();
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


	private void prepareDataMenuBOD() {
		isLoading = true;
		progressDialog.show();
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
		ApiVolley request = new ApiVolley(context, jBody, "POST", ServerUrl.UrlDataMenuComplaint, -1, new ApiVolley.VolleyCallback() {
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
							list.add(new ModelMenuBOD(
									isi.getString("created_at"),
									isi.getString("description"),
									isi.getString("status")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterMenuBOD(context, list);
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
						layoutNoData.setVisibility(View.GONE);
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
						layoutNoData.setVisibility(View.VISIBLE);
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
			jBody.put("category", "bod");
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
							moreList.add(new ModelMenuBOD(
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
