package dit.upm.es.postitapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListNotesActivity extends Activity {

	HttpClient client;
	ProgressDialog progressBar;
	String[] noteArray;
	List<Note> listUserNotes;
	ListView listNotes;
	String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_notes);
		
		Bundle extras = getIntent().getExtras();
		userId = extras.getString("userId");
		
		listNotes = (ListView)findViewById(R.id.listNotes);
		client = new DefaultHttpClient();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading...");
		listNotes.setEmptyView(findViewById(R.id.emptyList));
		
		
		listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				pressGet(listUserNotes.get(position).getId());
				
				
			}
		});
	}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new GetListNotes().execute();
	}
	public void fillList(){
		ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, noteArray);
		listNotes.setAdapter(itemsAdapter);
	}
	
	public void pressGet(Long id){
		Intent i = new Intent(this,PostItShow.class);
		i.putExtra("idNote",id);
		i.putExtra("userId", userId);
		startActivity(i);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_notes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	

	private class GetListNotes extends AsyncTask<Void, Note, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				//Debemos obtener la latitud y longitud

				
				pairs.add(new BasicNameValuePair("userId", userId));
				


				String paramsString = URLEncodedUtils.format(pairs, "UTF-8");
				HttpGet get = new HttpGet("http://1-dot-postitapp-server.appspot.com/getusernotes" + "?" + paramsString);

				try {
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"iso-8859-1"),8);

					String jsonResponse = reader.readLine();
					Log.i("e",jsonResponse);
					Gson gson = new Gson();
					TypeToken<List<Note>> token = new TypeToken<List<Note>>(){};
					listUserNotes = gson.fromJson(jsonResponse, token.getType());
					noteArray = new String[listUserNotes.size()];
					
					for(int i=0;i<listUserNotes.size();i++){
						
						noteArray[i]=listUserNotes.get(i).getTitle();
					}

					//Obtener respuesta
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setCancelable(false);
			progressBar.setMax(1);
			progressBar.setTitle("Getting list notes...");
			progressBar.setProgress(0);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressBar.dismiss();
			fillList();
			
			
		}

		
	}
}
