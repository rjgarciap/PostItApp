package dit.upm.es.postitapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.internal.ra;
import com.google.gson.Gson;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class PostItShow extends Activity {

	TextView titleTextView;
	TextView contentTextView;
	
	HttpClient client;
	ProgressDialog progressBar;
	AlertDialog alertDialog;

	Button editButton;
	Button deleteButton;
	boolean buttonvisibility = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_show);
		titleTextView = (TextView) findViewById(R.id.titleNote);
		contentTextView = (TextView) findViewById(R.id.contentNote);
		editButton = (Button) findViewById(R.id.editButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		client = new DefaultHttpClient();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading...");
		new GetNote().execute();
		
		if (buttonvisibility = true){
			// DEPENDE DEL USERID -- RELACIONAR CON FACEBOOK Y ESO...
			// MIRAR EN GETNOTE ONPOSTEXECUTE
			editButton.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);
		}
		
		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				pressEdit();
			}
			
			
		} );
		
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new DeleteNote().execute();
				alertDialog.setTitle("Note deleted");
				alertDialog.setMessage("Note has been successfully deleted");
				alertDialog.setButton(RESULT_OK, "Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Finish activity
						finish();
					}
				});
			}
			
			
		} );
				
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_it_show, menu);
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

	


	public void pressEdit(){
		
		Bundle extras = getIntent().getExtras();
		Long idNote = extras.getLong("idNote");
		Note note = (Note) extras.get("Note");
		Intent i = new Intent(this,PostItEdit.class);
		i.putExtra("idNote",idNote);
		i.putExtra("Note", note);
		Log.i("Hemos pasado el id a edit",""+idNote);
		startActivity(i);
	}

	private class GetNote extends AsyncTask<Void, Void, Note> {

		@Override
		protected Note doInBackground(Void... params) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			
			Bundle extras = getIntent().getExtras();
			
			Long idNote = extras.getLong("idNote");
			
			pairs.add(new BasicNameValuePair("id", ""+idNote));
			
			Log.i("IDNOTE", ""+idNote);
			String paramsString = URLEncodedUtils.format(pairs, "UTF-8");
			HttpGet get = new HttpGet("http://1-dot-postitapp-server.appspot.com/getnote" + "?" + paramsString);

			Note note = null;

			try {
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"iso-8859-1"),8);
				
				String jsonResponse = reader.readLine();
				Log.i("sa",jsonResponse);
				Gson gson = new Gson();
				note = gson.fromJson(jsonResponse, Note.class);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return note;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setCancelable(false);
			progressBar.setMax(1);
			progressBar.setTitle("Loading Note");
			progressBar.setProgress(0);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(Note result) {
			progressBar.dismiss();
			titleTextView.setText(result.getTitle());
			contentTextView.setText(result.getText());
			// if(result.getuserId() == userId){buttonvisibility = true;}
			Intent i = getIntent();
			i.putExtra("Note", result);
			

		}

	}
	
	private class DeleteNote extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();


			Bundle extras = getIntent().getExtras();
			Long idNote = extras.getLong("idNote");
			
			pairs.add(new BasicNameValuePair("id", ""+idNote));
			
			Log.i("IDNOTE", ""+idNote);
			String paramsString = URLEncodedUtils.format(pairs, "UTF-8");
			HttpPost post = new HttpPost("http://1-dot-postitapp-server.appspot.com/deletenote");

			
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				HttpResponse response = client.execute(post);

				//Obtener respuesta
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setCancelable(false);
			progressBar.setMax(1);
			progressBar.setTitle("Deleting Note");
			progressBar.setProgress(0);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressBar.dismiss();
			//Aqui comprobamos el resultado
			alertDialog.show();
			
			

		}

	}

}
