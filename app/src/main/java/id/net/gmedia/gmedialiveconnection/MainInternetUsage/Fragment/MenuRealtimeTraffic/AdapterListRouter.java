package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MenuRealtimeTraffic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainInternetUsage.ModelListRouter;
import id.net.gmedia.gmedialiveconnection.R;

public class AdapterListRouter extends ArrayAdapter {
	private Context context;
	private List<ModelListRouter> list;

	public AdapterListRouter(Context context, List<ModelListRouter> list) {
		super(context, R.layout.view_lv_router, list);
	}

	private static class ViewHolder {
		private TextView nama;
	}

	public void addMoreData(List<ModelListRouter> moreData) {

		list.addAll(moreData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_router, null);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNamaListRouter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListRouter model = list.get(position);
		holder.nama.setText(model.getNama());
		return convertView;
	}
}
