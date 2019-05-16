package id.net.gmedia.gmedialiveconnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class Profile extends AppCompatActivity {

	private Context context;
	private ProgressDialog progressDialog;
	private TextView tvTitle, txtCustomerID, txtServiceID, txtCustomerName, txtCustomerAddress, txtSiteName, txtAddress, txtServiceName;
	private ImageView ivMenu;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

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
		txtCustomerID = (TextView) findViewById(R.id.txtCustomerIDProfile);
		txtServiceID = (TextView) findViewById(R.id.txtServiceIDProfile);
		txtCustomerName = (TextView) findViewById(R.id.txtCustomerNameProfile);
		txtCustomerAddress = (TextView) findViewById(R.id.txtCustomerAddressProfile);
		txtSiteName = (TextView) findViewById(R.id.txtSiteNameProfile);
		txtAddress = (TextView) findViewById(R.id.txtAddressProfile);
		txtServiceName = (TextView) findViewById(R.id.txtServiceNameProfile);

		tvTitle.setText("Profile");
	}

	private void initEvent() {
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		prepareDataProfile();
	}

	private void prepareDataProfile() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlProfile, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						txtCustomerID.setText(response.getString("customer_id"));
						txtServiceID.setText(response.getString("service_id"));
						txtCustomerName.setText(response.getString("customer_name"));
						txtCustomerAddress.setText(response.getString("customer_address"));
						txtSiteName.setText(response.getString("site_name"));
						txtAddress.setText(response.getString("site_address"));
						txtServiceName.setText(response.getString("service_name"));
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
}
