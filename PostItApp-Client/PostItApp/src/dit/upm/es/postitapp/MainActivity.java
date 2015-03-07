package dit.upm.es.postitapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity  implements OnMapClickListener, OnMarkerClickListener{
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(this);
	}
	public void pressPost(){
		Intent i = new Intent(this,PostItUpload.class);
		i.putExtra("lat", map.getMyLocation().getLatitude());
		i.putExtra("long", map.getMyLocation().getLongitude());
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.postANote:
			pressPost();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void moveCamera(View view, LatLng latitudlongitud) {
		//Llamarlo para que mueva la camara hasta ahí
		map.moveCamera(CameraUpdateFactory.newLatLng(latitudlongitud));
    }



	public void animateCamera(View view) {
		if (map.getMyLocation() != null)
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng( map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude()), 15));
		}



	public void addMarker(View view) {
		//Para llamarlo desde fuera
		map.addMarker(new MarkerOptions().position(
				new LatLng(map.getCameraPosition().target.latitude,
						map.getCameraPosition().target.longitude)));

	}
	@Override
	public void onMapClick(LatLng puntoPulsado) {
		map.addMarker(new MarkerOptions().position(puntoPulsado).icon(BitmapDescriptorFactory.fromResource(R.drawable.postit)).anchor(0.5f, 0.5f));

	}
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// Lo que haga al tocar en un marker
		return false;
	}
}
