package id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.MainTroubleAnalytic;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainPingTest extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();
	private Button btnTestPing;
	private TextView txtPing;
	private ProgressDialog progressDialog;

	public MainPingTest() {
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
		layout = inflater.inflate(R.layout.fragment_main_ping_test, container, false);
		context = getContext();

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		initUI();
		initEvent();

		return layout;
	}

	private void initUI() {
		txtPing = (TextView) layout.findViewById(R.id.txtPing);
		btnTestPing = (Button) layout.findViewById(R.id.btnTestPing);
	}

	private void initEvent() {
		btnTestPing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				prepareDataTesPing();
			}
		});
	}

	private void prepareDataTesPing() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlPingTest + MainTroubleAnalytic.idListRouter, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						txtPing.setText(response.getString("ping_time"));
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
