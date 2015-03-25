package dit.upm.es.postitapp;

import com.google.android.gms.internal.co;

public class ItemNavigationDrawer {

	private static long counItems = 0;
	private long id;
	private String pathImage;
	private String name;

	public ItemNavigationDrawer(String pathImage, String name) {
		// TODO Auto-generated constructor stub
		this.id = counItems;
		this.pathImage = pathImage;
		this.name = name;
		counItems++;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	public String getName() {
		return name;
	}

	public void setNombre(String name) {
		this.name = name;
	}

}
