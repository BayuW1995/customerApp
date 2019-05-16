package id.net.gmedia.gmedialiveconnection.MainCoverage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maulana.custommodul.ApiVolley;
import com.maulana.custommodul.ItemValidation;
import com.maulana.custommodul.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import id.net.gmedia.gmedialiveconnection.CustomView.CustomMapView;
import id.net.gmedia.gmedialiveconnection.LoginScreen;
import id.net.gmedia.gmedialiveconnection.R;
import id.net.gmedia.gmedialiveconnection.utils.ServerUrl;

public class MainCoverage extends AppCompatActivity {

	private ItemValidation iv = new ItemValidation();
	private SessionManager session;
	private CustomMapView mvMap;
	private GoogleMap googleMap;
	private Context context;
	private TextView tvTitle;
	private ImageView ivMenu, imgMap;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_coverage);
		context = this;

		progressDialog = new ProgressDialog(context, R.style.AppTheme_Login_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);

		initUI();
		initEvent();
	}

	private void initUI() {

		session = new SessionManager(context);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivMenu = (ImageView) findViewById(R.id.iv_menu);
		imgMap = (ImageView) findViewById(R.id.imgMapCovArea);
		prepareDataCovArea();
        /*mvMap = (CustomMapView) findViewById(R.id.mv_map);
        mvMap.onCreate(null);
        mvMap.onResume();*/

		tvTitle.setText("Coverage Area");

        /*try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

		Bundle bundle = getIntent().getExtras();

//        setPointMap();

	}

	private void prepareDataCovArea() {
		progressDialog.show();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", ServerUrl.UrlCoverageArea, -1, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						String image = response.getString("image");
						Picasso.with(context).load(image).into(imgMap);
					} else if (status.equals("401")) {
						Intent intent = new Intent(context, LoginScreen.class);
						session.logoutUser(intent);
					}else {
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

	private void initEvent() {
		ivMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}

	private void setPointMap() {

		mvMap.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap mMap) {

				googleMap = mMap;
				googleMap.clear();

				Marker markerS = googleMap.addMarker(new MarkerOptions()
						.anchor(0.0f, 1.0f)
						.draggable(true)
						.position(new LatLng(iv.parseNullDouble("7.0051"), iv.parseNullDouble("110.4381"))));

				markerS.showInfoWindow();
				if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(context, "Please allow location access from your app permission", Toast.LENGTH_SHORT).show();
					return;
				}

				//googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				MapsInitializer.initialize(context);
				LatLng position = new LatLng(iv.parseNullDouble("7.0051"), iv.parseNullDouble("110.4381"));
				// For zooming automatically to the location of the marker
				CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).build();
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
		});
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

	}
}
