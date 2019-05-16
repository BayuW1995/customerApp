package id.net.gmedia.gmedialiveconnection.MainTicket.Fragment.MenuRequest;

public class ModelMenuRequest {
	private String tanggal = "", deskripsi = "", status = "";

	public ModelMenuRequest(String created_at, String description, String status) {
		this.tanggal = created_at;
		this.deskripsi = description;
		this.status = status;
	}

	public String getTanggal() {
		return tanggal;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
