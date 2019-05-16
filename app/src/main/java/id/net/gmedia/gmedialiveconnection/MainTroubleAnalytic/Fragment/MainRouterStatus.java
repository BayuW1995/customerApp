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
import android.widget.TextView;

import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.MainTroubleAnalytic.MainTroubleAnalytic;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainRouterStatus extends Fragment {

	private Context context;
	private View layout;
	private ItemValidation iv = new ItemValidation();
	private TextView txtUptime, txtVersion, txtBuildTime, txtFactorySoftware, txtFreeMemory, txtTotalMemory, txtCPU, txtCPUCount,
			txtCPUFrequency, txtCPULoad, txtFreeHDDSpace, txtTotalHDDSpace, txtArchitecture, txtBoardName, txtPlatform;
	private ProgressDialog progressDialog;

	public MainRouterStatus() {
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
		layout = inflater.inflate(R.layout.fragment_main_router_status, container, false);
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
		txtUptime = (TextView) layout.findViewById(R.id.txtUpTimeRouterStatus);
		txtVersion = (TextView) layout.findViewById(R.id.txtVersionRouterStatus);
//		txtBuildTime = (TextView) layout.findViewById(R.id.txtBuildTimeRouterStatus);
//		txtFactorySoftware = (TextView) layout.findViewById(R.id.txtFactorySoftwareRouterStatus);
		txtFreeMemory = (TextView) layout.findViewById(R.id.txtFreeMemoryRouterStatus);
		txtTotalMemory = (TextView) layout.findViewById(R.id.txtTotalMemoryRouterStatus);
		txtCPU = (TextView) layout.findViewById(R.id.txtCPURouterStatus);
		txtCPUCount = (TextView) layout.findViewById(R.id.txtCPUCountRouterStatus);
		txtCPUFrequency = (TextView) layout.findViewById(R.id.txtCPUFrequencyRouterStatus);
		txtCPULoad = (TextView) layout.findViewById(R.id.txtCPULoadRouterStatus);
		txtFreeHDDSpace = (TextView) layout.findViewById(R.id.txtFreeHDDSpaceRouterStatus);
		txtTotalHDDSpace = (TextView) layout.findViewById(R.id.txtTotalHDDSpaceRouterStatus);
		txtArchitecture = (TextView) layout.findViewById(R.id.txtArchitectureNameRouterStatus);
		txtBoardName = (TextView) layout.findViewById(R.id.txtBoardNameRouterStatus);
		txtPlatform = (TextView) layout.findViewById(R.id.txtPlatformRouterStatus);
	}

	private void initEvent() {
		prepareDataRouterStatus();
	}

	private void prepareDataRouterStatus() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlRouterStatus + MainTroubleAnalytic.idListRouter, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						txtUptime.setText(response.getString("uptime"));
						txtVersion.setText(response.getString("os_version"));
						txtFreeMemory.setText(response.getString("free_memory"));
						txtTotalMemory.setText(response.getString("total_memory"));
						txtCPU.setText(response.getString("cpu"));
						txtCPUCount.setText(response.getString("cpu_count"));
						txtCPUFrequency.setText(response.getString("cpu_frequency"));
						txtCPULoad.setText(response.getString("cpu_load"));
						txtFreeHDDSpace.setText(response.getString("free_hdd_space"));
						txtTotalHDDSpace.setText(response.getString("total_hdd_space"));
						txtArchitecture.setText(response.getString("architecture"));
						txtBoardName.setText(response.getString("board-name"));
						txtPlatform.setText(response.getString("platform"));

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
