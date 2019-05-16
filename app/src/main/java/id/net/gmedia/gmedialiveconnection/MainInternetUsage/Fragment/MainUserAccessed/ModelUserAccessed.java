package id.net.gmedia.gmedialiveconnection.MainInternetUsage.Fragment.MainUserAccessed;

public class ModelUserAccessed {
	private String ip_perangkat, ip_tujuan, upload, download, hostname;

	public ModelUserAccessed(String ip_device, String ip_dst, String tr_upload, String tr_download, String hostname) {
		this.ip_perangkat = ip_device;
		this.ip_tujuan = ip_dst;
		this.upload = tr_upload;
		this.download = tr_download;
		this.hostname = hostname;
	}

	public String getIp_perangkat() {
		return ip_perangkat;
	}

	public void setIp_perangkat(String ip_perangkat) {
		this.ip_perangkat = ip_perangkat;
	}

	public String getIp_tujuan() {
		return ip_tujuan;
	}

	public void setIp_tujuan(String ip_tujuan) {
		this.ip_tujuan = ip_tujuan;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}
