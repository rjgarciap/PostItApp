package dit.upm.es.postitapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity  implements OnMapClickListener, OnMarkerClickListener{
	private GoogleMap map;
	
	private Double lat;
	private Double lon;
	
	private Map<Marker,Long> allMarkersMap;
	
	GetNearNotes getNearNotesthread;
	HttpClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(this);
		map.setOnMyLocationChangeListener(myLocationChangeListener);
		

		
		allMarkersMap = new HashMap<Marker, Long>();
		
		client = new DefaultHttpClient();
		
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker markerPressed) {
				// TODO Auto-generated method stub
				Long idNotePressed = allMarkersMap.get(markerPressed);
				pressGet(idNotePressed);
				return false;
			}
		});
    	
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getNearNotesthread = new GetNearNotes();
		getNearNotesthread.execute();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getNearNotesthread.cancel(true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getNearNotesthread.cancel(true);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		getNearNotesthread.cancel(true);
	}
	
	
	public void pressPost(){
		Intent i = new Intent(this,PostItUpload.class);
		i.putExtra("lat", map.getMyLocation().getLatitude());
		i.putExtra("long", map.getMyLocation().getLongitude());
		startActivity(i);
	}
	
	
	public void pressGet(Long id){
		Intent i = new Intent(this,PostItShow.class);
		i.putExtra("idNote",id);
		Log.i("Sdf",""+id);
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
	
	public void addMarker(Double lat, Double lon) {
		//Para llamarlo desde fuera
		map.addMarker(new MarkerOptions().position(
				new LatLng(lat,
						lon)));

	}
	
	@Override
	public void onMapClick(LatLng clickPoint) {

	}



	private class GetNearNotes extends AsyncTask<Void, Note, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			while(!isCancelled()){
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				//Debemos obtener la latitud y longitud


				pairs.add(new BasicNameValuePair("lat", ""+lat));
				pairs.add(new BasicNameValuePair("long", ""+lon));


				String paramsString = URLEncodedUtils.format(pairs, "UTF-8");
				HttpGet get = new HttpGet("http://1-dot-postitapp-server.appspot.com/getnearnotes" + "?" + paramsString);

				try {
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"iso-8859-1"),8);
					
					String jsonResponse = reader.readLine();
					Log.i("ey",jsonResponse);
					Gson gson = new Gson();
					TypeToken<List<Note>> token = new TypeToken<List<Note>>(){};
					List<Note> noteList = gson.fromJson(jsonResponse, token.getType());
					if(noteList != null){
						publishProgress(null);
						for(Note a: noteList){
						  publishProgress(a);
						  Log.i("ey",a.getTitle());
					    }
						
					}
					
					//Obtener respuesta
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    try {
			        Thread.sleep(10000);         
			    } catch (InterruptedException e) {
			       e.printStackTrace();
			    }

			}
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Boolean result) {

		}

		@Override
		protected void onProgressUpdate(Note... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(values == null){
				map.clear();
				allMarkersMap.clear();
			}else{
				MarkerOptions addedMarkerOptions = new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.postit)).anchor(0.3f, 0.3f);
				Marker addedMarker = map.addMarker(addedMarkerOptions);
				allMarkersMap.put(addedMarker, values[0].getId());
			}
		}
	}
	
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
		@Override
		public void onMyLocationChange(Location location) {
			// TODO Auto-generated method stub
			lat = location.getLatitude();
			lon = location.getLongitude();
			LatLng loc = new LatLng(lat, lon);
	        if(map != null ){
	        	//16f
	            map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 6.0f));
	        }
		}
	};

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
