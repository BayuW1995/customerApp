package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.maulana.custommodul.FormatItem;
import com.maulana.custommodul.ItemValidation;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterMenuComplaint extends ArrayAdapter {
	private Context context;
	private List<ModelMenuComplaint> list;
	private ItemValidation iv = new ItemValidation();

	public ListAdapterMenuComplaint(Context context, List<ModelMenuComplaint> list) {
		super(context, R.layout.view_lv_complaint, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView tanggal, deskripsi;
		private String status;
		private ImageView imgStatus;
	}

	public void addMoreData(List<ModelMenuComplaint> moreData) {

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
			convertView = inflater.inflate(R.layout.view_lv_complaint, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.txtTglMenuComplaint);
			holder.deskripsi = (TextView) convertView.findViewById(R.id.txtDeskripsiMenuComplaint);
			holder.imgStatus = (ImageView) convertView.findViewById(R.id.imgStatusMenuComplaint);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelMenuComplaint model = list.get(position);
		holder.tanggal.setText(iv.ChangeFormatDateString(model.getTanggal(), FormatItem.formatTimestamp,FormatItem.formatDateTime));
//		holder.tanggal.setText(model.getTanggal());
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
