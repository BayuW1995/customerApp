package id.net.gmedia.gmedialiveconnection.MenuBillingData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maulana.custommodul.CustomItem;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuBOD.ListAdapterMenuBOD;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ListAdapterMenuComplaint;
import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterBillingData extends ArrayAdapter {
	private Context context;
	private List<ModelBillingData> list;

	public ListAdapterBillingData(Context context, List<ModelBillingData> list) {
		super(context, R.layout.view_lv_billing_data, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView txtNomorTagihan, txtJumlahTagihan, txtTotalBayar, txtTanggalJatuhTempo;
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
			convertView = inflater.inflate(R.layout.view_lv_billing_data, null);
			holder.txtNomorTagihan = (TextView) convertView.findViewById(R.id.txtNomorTagihanBillingData);
			holder.txtJumlahTagihan = (TextView) convertView.findViewById(R.id.txtJumlahTagihanBillingData);
			holder.txtTotalBayar = (TextView) convertView.findViewById(R.id.txtTotalBayarBillingData);
			holder.txtTanggalJatuhTempo = (TextView) convertView.findViewById(R.id.txtTanggalJatuhTempoBillingData);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelBillingData model = list.get(position);
		holder.txtNomorTagihan.setText(model.getNomorTagihan());
		holder.txtJumlahTagihan.setText(model.getJumlahTagihan());
		holder.txtTotalBayar.setText(model.getTotalBayar());
		holder.txtTanggalJatuhTempo.setText(model.getTanggalJatuhTempo());
		return convertView;
	}
}
