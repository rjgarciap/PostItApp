package dit.upm.es.postitapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PostItUpload extends Activity {

	HttpClient client;
	ProgressDialog progressBar;
	
	EditText titleEditText;
	EditText contentEditText;
	Button sendButton;
	AlertDialog alertDialog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_upload);
		
		client = new DefaultHttpClient();
		alertDialog = new AlertDialog.Builder(this).create();
        progressBar = new ProgressDialog(this);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("Uploading...");
		
		titleEditText = (EditText) findViewById(R.id.titleText);
		contentEditText = (EditText) findViewById(R.id.contentText);
		sendButton = (Button) findViewById(R.id.sendButton);
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				
				if (title.matches("") || content.matches("")) {
				    Toast.makeText(getApplicationContext(), "You did not enter some field.", Toast.LENGTH_SHORT).show();
				    return;
				}
				new PostNote().execute();
				
				
				
				alertDialog.setTitle("Subido correctamente");
				alertDialog.setMessage("subido perfecto");
				alertDialog.setButton(RESULT_OK, "Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// aquí puedes añadir funciones
							finish();
						}
				});
				alertDialog.show();
				
			}
		});
		
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_it_upload, menu);
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
	
	
	
	private class PostNote extends AsyncTask<Void, Void, Boolean> {
		 
	    @Override
	    protected Boolean doInBackground(Void... params) {
			
	    	HttpPost post = new HttpPost("http://1-dot-postitapp-server.appspot.com/postnote");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("title", titleEditText.getText().toString()));
			pairs.add(new BasicNameValuePair("content", contentEditText.getText().toString()));
			//Debemos obtener la latitud y longitud
			pairs.add(new BasicNameValuePair("lat", "0"));
			pairs.add(new BasicNameValuePair("lon", "0"));
			
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
			progressBar.setTitle("Uploading a new Note");
			progressBar.setProgress(0);
			progressBar.show();
	    }
	 
	    @Override
	    protected void onPostExecute(Boolean result) {
	        progressBar.dismiss();
	        //Aqui comprobamos el resultado
	    }
	 
	}
	
	
	
	
	
}
