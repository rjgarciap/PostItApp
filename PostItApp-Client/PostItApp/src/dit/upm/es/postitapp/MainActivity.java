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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity  implements OnMapClickListener, OnMarkerClickListener, OnItemClickListener{

	final Session session = Session.getActiveSession();
	private String user_ID;
	private String profileName;

	private GoogleMap map;

	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	ArrayList<ItemNavigationDrawer> items;

	private ProfilePictureView profilePicture;
	private TextView userNameView;

	private Double lat;
	private Double lon;

	private boolean first = true;
	
	private Map<Marker,Long> allMarkersMap;
	ProgressDialog progressBar;
	GetNearNotes getNearNotesthread;
	HttpClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(session==null){
			finish();
		}
		
		setContentView(R.layout.activity_main);

		View header = getLayoutInflater().inflate(R.layout.header, null);
		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(this);
		map.setOnMyLocationChangeListener(myLocationChangeListener);

		
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading your position...");
		
		if(first){
			progressBar.show();
		}
		items = new ArrayList<ItemNavigationDrawer>();

		allMarkersMap = new HashMap<Marker, Long>();

		client = new DefaultHttpClient();

		map.setOnMarkerClickListener(this);

		mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		items.add(new ItemNavigationDrawer("android:drawable/ic_menu_archive", "Mis notas"));
		items.add(new ItemNavigationDrawer("drawable/exit_icon", "Salir"));

		mDrawerOptions.addHeaderView(header);

		mDrawerOptions.setAdapter(new ItemCustomAdapter(this, items));
		mDrawerOptions.setOnItemClickListener(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		profilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
		userNameView = (TextView) findViewById(R.id.userNameView);
		
		getUserData();
	}

	public void getUserData(){
		if (session != null && session.isOpened()) {
			// If the session is open, make an API call to get user data
			// and define a new callback to handle the response
			Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					// TODO Auto-generated method stub
					if (session == Session.getActiveSession()) {
						if (user != null) {
							user_ID = user.getId();//user id
							profileName = user.getName();//user's profile name
							userNameView.setText(user.getName());
							profilePicture.setProfileId(user_ID);
						}   
					} 
				}   
			}); 
			Request.executeBatchAsync(request);
		}  
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
		i.putExtra("userId", user_ID);
		i.putExtra("session", session);
		startActivity(i);
	}


	public void pressGet(Long id){
		Intent i = new Intent(this,PostItShow.class);
		i.putExtra("idNote",id);
		i.putExtra("userId", user_ID);
		startActivity(i);
	}

	public void pressListNotes(){
		Intent i = new Intent(this,ListNotesActivity.class);
		i.putExtra("userId", user_ID);
		startActivity(i);
	}

	public void pressLogout(){
		Intent i = new Intent(this,LoginActivity.class);
		startActivity(i);
		finish();
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
		case android.R.id.home:
			if (mDrawer.isDrawerOpen(mDrawerOptions)){
				mDrawer.closeDrawers();
			}else{
				mDrawer.openDrawer(mDrawerOptions);
			}
			return true;
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
				pairs.add(new BasicNameValuePair("userId", user_ID));
				

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
					Thread.sleep(5000);         
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
				MarkerOptions addedMarkerOptions;
				switch (values[0].getColorNote()) {
				case RED:
					addedMarkerOptions= new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_note)).anchor(0.3f, 0.3f);
					break;
				case YELLOW:
					addedMarkerOptions = new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_note)).anchor(0.3f, 0.3f);
					break;
				case BLUE:
					addedMarkerOptions = new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_note)).anchor(0.3f, 0.3f);
					break;
				case GREEN:
					addedMarkerOptions = new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_note)).anchor(0.3f, 0.3f);
					break;
				default:
					addedMarkerOptions = new MarkerOptions().position(new LatLng(values[0].getLat(), values[0].getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_note)).anchor(0.3f, 0.3f);
					break;
				}
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
			if(first){
				progressBar.dismiss();
			}
			LatLng loc = new LatLng(lat, lon);
			if(map != null ){
				//16f
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
			}
		}
	};

	@Override
	public boolean onMarkerClick(Marker markerPressed) {
		// TODO Auto-generated method stub
		Long idNotePressed = allMarkersMap.get(markerPressed);
		pressGet(idNotePressed);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 1:
			pressListNotes();
			break;
		case 2:
			session.closeAndClearTokenInformation();
			finish();
			break;

		default:
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(Intent.ACTION_MAIN);
	   setIntent.addCategory(Intent.CATEGORY_HOME);
	   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   startActivity(setIntent);
	}
	
	/**
	 * This method check mobile is connected to network.
	 * @param context
	 * @return true if connected otherwise false.
	 */
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
	        return true;
	    else
	        return false;
	}
	
	
	
}
