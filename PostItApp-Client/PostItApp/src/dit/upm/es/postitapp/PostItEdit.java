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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class PostItEdit extends Activity {

	EditText titleEditText;
	EditText contentEditText;
	int idxColor;
	ColorNote colorNoteSelected;
	HttpClient client;
	ProgressDialog progressBar;
	AlertDialog alertDialog;
	RadioGroup radioGroupColors;
	Button sendButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_edit);
		titleEditText = (EditText) findViewById(R.id.titleNote);
		contentEditText = (EditText) findViewById(R.id.contentNote);
		radioGroupColors = (RadioGroup) findViewById(R.id.radioGroupColorNotes);
		client = new DefaultHttpClient();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading...");
	
		
		Bundle extras = getIntent().getExtras();
		Note note = (Note)extras.get("Note");
		
		titleEditText.setText(note.getTitle());
		contentEditText.setText(note.getText());
		
		switch(note.getColorNote()){
		case BLUE: {
			radioGroupColors.check(R.id.colorNoteBlue);
			break;						
		}
		case YELLOW: {
			radioGroupColors.check(R.id.colorNoteYellow);
			break;						
		}
		case RED: {
			radioGroupColors.check(R.id.colorNoteRed);
			break;						
		}
		case GREEN: {
			radioGroupColors.check(R.id.colorNoteGreen);
			break;						
		}
		default: {
			radioGroupColors.check(R.id.colorNoteBlue);
			break;
		}
			
		}
		
		
		
		
		
		
		sendButton.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();

				if (title.matches("") || content.matches("")) {
					Toast.makeText(getApplicationContext(), "You did not enter some field.", Toast.LENGTH_SHORT).show();
					return;
				}

				View radioButton = radioGroupColors.findViewById(radioGroupColors.getCheckedRadioButtonId());
				int idx = radioGroupColors.indexOfChild(radioButton);
				colorNoteSelected = getColorNote(idx);
				new PostNote().execute();



				alertDialog.setTitle("Note updated");
				alertDialog.setMessage("Note has been successfully updated");
				alertDialog.setButton(RESULT_OK, "Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Finish activity
						finish();
					}
				});


			}
		});
		
		
	}
	
	private class PostNote extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			HttpPost post = new HttpPost("http://1-dot-postitapp-server.appspot.com/updatenote");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			Bundle extras = getIntent().getExtras();
			Long idNote = extras.getLong("idNote");
			pairs.add(new BasicNameValuePair("title", titleEditText.getText().toString()));
			pairs.add(new BasicNameValuePair("content", contentEditText.getText().toString()));	
			pairs.add(new BasicNameValuePair("colorNote", ""+colorNoteSelected.toString()));
			pairs.add (new BasicNameValuePair("id",""+ idNote));
			
			Log.i("ver color", colorNoteSelected.toString());
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
			progressBar.setTitle("Uploading an edited Note");
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



public ColorNote getColorNote(int idxColor){
		
		ColorNote result;
		switch (idxColor) {
		case 0:
			result = ColorNote.BLUE;
			break;
		case 1:
			result = ColorNote.YELLOW;
			break;
		case 2:
			result = ColorNote.RED;
			break;
		case 3:
			result = ColorNote.GREEN;
			break;

		default:
			result = ColorNote.BLUE;
			break;
		}
		return result;
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


}
