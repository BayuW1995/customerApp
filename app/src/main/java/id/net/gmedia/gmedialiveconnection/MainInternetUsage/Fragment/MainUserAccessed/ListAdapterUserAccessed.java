package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MainUserAccessed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maulana.custommodul.CustomItem;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterUserAccessed extends ArrayAdapter {
	private Context context;
	private List<ModelUserAccessed> list;

	public ListAdapterUserAccessed(Context context, List<ModelUserAccessed> list) {
		super(context, R.layout.view_lv_user_accessed, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView txtIPPerangkat, txtIPTujuan, txtUpload, txtDownload, txtHostname;
	}

	public void addMoreData(List<ModelUserAccessed> moreData) {

		list.addAll(moreData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.view_lv_user_accessed, null);
			holder.txtIPPerangkat = (TextView) convertView.findViewById(R.id.tv_ip_perangkat_user_accessed);
			holder.txtIPTujuan = (TextView) convertView.findViewById(R.id.tv_ip_tujuan_user_accessed);
			holder.txtUpload = (TextView) convertView.findViewById(R.id.tv_traffic_upload_user_accessed);
			holder.txtDownload = (TextView) convertView.findViewById(R.id.tv_traffic_download_user_accessed);
			holder.txtHostname = (TextView) convertView.findViewById(R.id.tv_hostname_user_accessed);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ModelUserAccessed itemSelected = list.get(position);
		holder.txtIPPerangkat.setText(itemSelected.getIp_perangkat());
		holder.txtIPTujuan.setText(itemSelected.getIp_tujuan());
		holder.txtUpload.setText(itemSelected.getUpload());
		holder.txtDownload.setText(itemSelected.getDownload());
		holder.txtHostname.setText(itemSelected.getHostname());
		return convertView;
	}
}
