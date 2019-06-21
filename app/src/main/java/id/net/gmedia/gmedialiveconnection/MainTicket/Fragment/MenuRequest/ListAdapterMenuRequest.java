package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuRequest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maulana.custommodul.FormatItem;
import com.maulana.custommodul.ItemValidation;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ListAdapterMenuComplaint;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ModelMenuComplaint;
import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterMenuRequest extends ArrayAdapter {
	private Context context;
	private List<ModelMenuRequest> list;
	private ItemValidation iv = new ItemValidation();

	public ListAdapterMenuRequest(Context context, List<ModelMenuRequest> list) {
		super(context, R.layout.view_lv_request, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView tanggal, deskripsi;
		private String status;
		private ImageView imgStatus;
		private LinearLayout layoutOpen, layoutClose;
	}

	public void addMoreData(List<ModelMenuRequest> moreData) {

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
			convertView = inflater.inflate(R.layout.view_lv_request, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.txtTglMenuRequest);
			holder.deskripsi = (TextView) convertView.findViewById(R.id.txtDeskripsiMenuRequest);
			holder.imgStatus = (ImageView) convertView.findViewById(R.id.imgStatusMenuRequest);
			holder.layoutOpen = (LinearLayout) convertView.findViewById(R.id.layout_open_lv_request);
			holder.layoutClose = (LinearLayout) convertView.findViewById(R.id.layout_close_lv_request);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelMenuRequest model = list.get(position);
		holder.tanggal.setText(iv.ChangeFormatDateString(model.getTanggal(), FormatItem.formatTimestamp, FormatItem.formatDateTime));
		holder.deskripsi.setText(model.getDeskripsi());
		holder.status = model.getStatus();
		if (holder.status.equals("open")) {
			holder.imgStatus.setImageResource(R.drawable.ic_on_progres);
			holder.layoutOpen.setVisibility(View.VISIBLE);
			holder.layoutClose.setVisibility(View.GONE);
		} else {
			holder.imgStatus.setImageResource(R.drawable.ic_solve);
			holder.layoutClose.setVisibility(View.VISIBLE);
			holder.layoutOpen.setVisibility(View.GONE);
		}
		return convertView;
	}
}
