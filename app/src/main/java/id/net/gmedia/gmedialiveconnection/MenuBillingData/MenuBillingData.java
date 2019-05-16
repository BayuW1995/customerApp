package id.net.gmedia.gmedialiveconnection.MenuBillingData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.FormatItem;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import id.net.gmedia.gmedialiveconnection.LoginScreen;
import id.net.gmedia.gmedialiveconnection.MenuHistoryBillingData.MenuHistoryBillingData;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MenuBillingData extends AppCompatActivity {
	private ProgressDialog progressDialog;
	private Context context;
	private ListView listView;
	private RelativeLayout btnHistory;
	private TextView tvTitle, txtNomorTagihan, txtJumlahTagihan, txtTotalBayar, txtTanggalJatuhTempo;
	private ImageView ivMenu;
	private ItemValidation iv = new ItemValidation();
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_billing_data);

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
//		listView = (ListView) findViewById(R.id.lvBillingData);
		txtNomorTagihan = (TextView) findViewById(R.id.txtNomorTagihanBillingData);
		txtJumlahTagihan = (TextView) findViewById(R.id.txtJumlahTagihanBillingData);
		txtTotalBayar = (TextView) findViewById(R.id.txtTotalBayarBillingData);
		txtTanggalJatuhTempo = (TextView) findViewById(R.id.txtTanggalJatuhTempoBillingData);
		btnHistory = (RelativeLayout) findViewById(R.id.btnHistoryTagihan);

		tvTitle.setText("Tagihan");
	}

	private void initEvent() {
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		prepareDataBillingData();
		btnHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, MenuHistoryBillingData.class);
				startActivity(intent);
			}
		});
	}

	private void prepareDataBillingData() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlBillingData, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject tagihan = object.getJSONObject("response").getJSONObject("tagihan");
						txtNomorTagihan.setText(tagihan.getString("nomor_tagihan"));
						txtJumlahTagihan.setText(ChangeToRupiahFormat(tagihan.getString("jumlah_tagihan")));
						txtTotalBayar.setText(ChangeToRupiahFormat(tagihan.getString("total_bayar")));
						txtTanggalJatuhTempo.setText(iv.ChangeFormatDateString(tagihan.getString("tanggal_jatuh_tempo"), FormatItem.formatDate, FormatItem.formatDateDisplayStrip));
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

	public String doubleToStringFull(Double number) {
		return String.format("%s", number).replace(",", ".");
	}

	public Double parseNullDouble(String s) {
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

	public String ChangeToRupiahFormat(String number) {

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
