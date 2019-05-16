package id.net.gmedia.gmedialiveconnection.MainDetailEvent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class DetailEvent extends AppCompatActivity {
	private Context context;
	private ImageView ivMenu, imgEvent;
	private TextView tvTitle, txtJudulEvent, txtDeskripsiEvent;
	private ProgressDialog progressDialog;
	private String idListEvent = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_event);
		context = this;

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			idListEvent = bundle.getString("id");
		}

		initUI();
		initAction();
	}

	private void initUI() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivMenu = (ImageView) findViewById(R.id.iv_menu);
		imgEvent = (ImageView) findViewById(R.id.imgDetailEvent);
		txtJudulEvent = (TextView) findViewById(R.id.txtJudulDeskripsiDetailEvent);
		txtDeskripsiEvent = (TextView) findViewById(R.id.txtDeskripsiDetailEvent);
	}

	private void initAction() {
		tvTitle.setText("Detail Event");
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		prepareDataDetailEvent();
	}

	private void prepareDataDetailEvent() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlDetailEvent + idListEvent, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						Picasso.with(context).load(response.getString("image")).into(imgEvent);
						txtJudulEvent.setText(response.getString("name"));
						txtDeskripsiEvent.setText(response.getString("description"));
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
	public void onBackPressed() {

		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

	}
}
