package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuBOD;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ListAdapterMenuComplaint;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuRequest.ListAdapterMenuRequest;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuRequest.ModelMenuRequest;
import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterMenuBOD extends ArrayAdapter {
	private Context context;
	private List<ModelMenuBOD> list;

	public ListAdapterMenuBOD(Context context, List<ModelMenuBOD> list) {
		super(context, R.layout.view_lv_bod, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView tanggal, deskripsi;
		private String status;
		private ImageView imgStatus;
	}

	public void addMoreData(List<ModelMenuBOD> moreData) {

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
			convertView = inflater.inflate(R.layout.view_lv_bod, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.txtTglMenuBOD);
			holder.deskripsi = (TextView) convertView.findViewById(R.id.txtDeskripsiMenuBOD);
			holder.imgStatus = (ImageView) convertView.findViewById(R.id.imgStatusMenuBOD);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelMenuBOD model = list.get(position);
		holder.tanggal.setText(model.getTanggal());
		holder.deskripsi.setText(model.getDeskripsi());
		holder.status = model.getStatus();
		if (holder.status.equals("open")) {
			holder.imgStatus.setImageResource(R.drawable.ic_on_progres);
		} else {
			holder.imgStatus.setImageResource(R.drawable.ic_solve);
		}
		return convertView;
	}
}