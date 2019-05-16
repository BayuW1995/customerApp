package id.net.gmedia.gmedialiveconnection.MenuHistoryBillingData;

public class ModelHistoryBillingData {
	private String nomorTagihan = "", jumlahTagihan = "", tanggalBayar = "", totalBayar = "", tanggalJatuhTempo = "";

	public ModelHistoryBillingData(String nomor_tagihan, String jumlah_tagihan, String tanggal, String total_bayar, String tanggal_jatuh_tempo) {
		this.nomorTagihan = nomor_tagihan;
		this.jumlahTagihan = jumlah_tagihan;
		this.tanggalBayar = tanggal;
		this.totalBayar = total_bayar;
		this.tanggalJatuhTempo = tanggal_jatuh_tempo;
	}

	public String getNomorTagihan() {
		return nomorTagihan;
	}

	public void setNomorTagihan(String nomorTagihan) {
		this.nomorTagihan = nomorTagihan;
	}

	public String getJumlahTagihan() {
		return jumlahTagihan;
	}

	public void setJumlahTagihan(String jumlahTagihan) {
		this.jumlahTagihan = jumlahTagihan;
	}

	public String getTotalBayar() {
		return totalBayar;
	}

	public void setTotalBayar(String totalBayar) {
		this.totalBayar = totalBayar;
	}

	public String getTanggalJatuhTempo() {
		return tanggalJatuhTempo;
	}

	public void setTanggalJatuhTempo(String tanggalJatuhTempo) {
		this.tanggalJatuhTempo = tanggalJatuhTempo;
	}

	public String getTanggalBayar() {
		return tanggalBayar;
	}

	public void setTanggalBayar(String tanggalBayar) {
		this.tanggalBayar = tanggalBayar;
	}
}
