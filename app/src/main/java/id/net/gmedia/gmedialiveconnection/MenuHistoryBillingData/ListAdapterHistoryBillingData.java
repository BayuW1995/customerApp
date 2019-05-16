package id.net.gmedia.gmedialiveconnection.MenuHistoryBillingData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maulana.custommodul.FormatItem;
import com.maulana.custommodul.ItemValidation;

import java.util.List;

import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuBOD.ModelMenuBOD;
import id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuComplaint.ListAdapterMenuComplaint;
import id.net.gmedia.gmedialiveconnection.R;

public class ListAdapterHistoryBillingData extends ArrayAdapter {
	private Context context;
	private List<ModelHistoryBillingData> list;
	private ItemValidation iv = new ItemValidation();

	public ListAdapterHistoryBillingData(Context context, List<ModelHistoryBillingData> list) {
		super(context, R.layout.view_lv_history_billing_data, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView txtNomorTagihan, txtJumlahTagihan, txttanggalBayar, txtTotalBayar, txtTanggalJatuhTempo;
	}

	public void addMoreData(List<ModelHistoryBillingData> moreData) {

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
			convertView = inflater.inflate(R.layout.view_lv_history_billing_data, null);
			holder.txtNomorTagihan = (TextView) convertView.findViewById(R.id.txtNomorTagihanHistoryBillingData);
			holder.txtJumlahTagihan = (TextView) convertView.findViewById(R.id.txtJumlahTagihanHistoryBillingData);
			holder.txttanggalBayar = (TextView) convertView.findViewById(R.id.txtTanggalBayarHistoryBillingData);
			holder.txtTotalBayar = (TextView) convertView.findViewById(R.id.txtTotalBayarHistoryBillingData);
			holder.txtTanggalJatuhTempo = (TextView) convertView.findViewById(R.id.txtTanggalJatuhTempoHistoryBillingData);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelHistoryBillingData model = list.get(position);
		holder.txtNomorTagihan.setText(model.getNomorTagihan());
		holder.txtJumlahTagihan.setText(MenuHistoryBillingData.ChangeToRupiahFormat(model.getJumlahTagihan()));
		holder.txttanggalBayar.setText(iv.ChangeFormatDateString(model.getTanggalBayar(), FormatItem.formatDate, FormatItem.formatDateDisplayStrip));
		holder.txtTotalBayar.setText(MenuHistoryBillingData.ChangeToRupiahFormat(model.getTotalBayar()));
		holder.txtTanggalJatuhTempo.setText(iv.ChangeFormatDateString(model.getTanggalJatuhTempo(), FormatItem.formatDate, FormatItem.formatDateDisplayStrip));
		return convertView;
	}
}
