package id.net.gmedia.gmedialiveconnection.MainInternetUsage;

public class ModelListRouter {
	private String id, nama;

	public ModelListRouter(String id, String name) {
		this.id = id;
		this.nama = name;
	}

	@Override
	public String toString() {
		return nama;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
}
