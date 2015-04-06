package dit.upm.es.postitapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.internal.ra;
import com.google.gson.Gson;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class PostItShow extends Activity {

	final Session session = Session.getActiveSession();
	TextView titleTextView;
	TextView contentTextView;
	TextView authorNameNote;

	final int EDITNOTE_ACT = 123;

	String userNoteId;
	String authorNoteId;
	HttpClient client;
	ProgressDialog progressBar;

	private ProfilePictureView profileAuthorPicture;

	AlertDialog alertDialog;

	Button editButton;
	Button deleteButton;
	Cloudinary cloudinary;
	ImageView imageNote;

	Bitmap bmp;

	String imageId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_show);
		titleTextView = (TextView) findViewById(R.id.titleNote);
		contentTextView = (TextView) findViewById(R.id.contentNote);
		profileAuthorPicture = (ProfilePictureView)findViewById(R.id.profileAuthorPicture);
		authorNameNote = (TextView)findViewById(R.id.authorNameNote);

		editButton = (Button) findViewById(R.id.editButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);

		imageNote = (ImageView) findViewById(R.id.imageNote);

		cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(this));
		client = new DefaultHttpClient();
		alertDialog = new AlertDialog.Builder(this).create();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading...");
		Bundle extras = getIntent().getExtras();
		userNoteId = extras.getString("userId");


		



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

				alertDialog.setButton(RESULT_OK, "Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Finish activity
						finish();
					}
				});
			}
		} );

		new GetNote().execute();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bmp!=null){
			bmp.recycle();
			bmp=null;
			
		}
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
		finish();
		startActivityForResult(i, EDITNOTE_ACT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);


		if (requestCode==EDITNOTE_ACT && resultCode==RESULT_OK) {
			titleTextView.setText(data.getExtras().getString("title"));
			contentTextView.setText(data.getExtras().getString("content"));
			//data.getExtras().get("colorNote");
		}

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
				authorNoteId = note.getUserId();

				imageId =note.getImageId();
				
				if(imageId!= ""){
					URL urlImage =new URL(cloudinary.url().generate(note.getImageId()));
					bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
				}

				//Request.newGraphPathRequest(session, graphPath, callback)
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


			Bundle paramsF = new Bundle();
			paramsF.putString("fields", "name");
			Request request = new Request(session, authorNoteId,paramsF,HttpMethod.GET,new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub

					FacebookRequestError error = response.getError();
					if(error!=null){
						Log.e("Error", error.getErrorMessage());
						return;
					}

					authorNameNote.setText((String) response.getGraphObject().getProperty("name"));
					profileAuthorPicture.setProfileId(authorNoteId);
					if (authorNoteId.equals(userNoteId)){
						editButton.setVisibility(View.VISIBLE);
						deleteButton.setVisibility(View.VISIBLE);
					}else{
						editButton.setVisibility(View.INVISIBLE);
						deleteButton.setVisibility(View.INVISIBLE);
					}

					progressBar.dismiss();
				}
			});
			Request.executeBatchAsync(request);
			Intent i = getIntent();
			i.putExtra("Note", result);

			titleTextView.setText(result.getTitle());
			contentTextView.setText(result.getText());
			if(result.getImageId()!=""){
				imageNote.setImageBitmap(bmp);
				imageNote.setVisibility(View.VISIBLE);
			}



		}



	}
	private class DeleteNote extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();


			Bundle extras = getIntent().getExtras();
			Long idNote = extras.getLong("idNote");

			pairs.add(new BasicNameValuePair("id", ""+idNote));
			String paramsString = URLEncodedUtils.format(pairs, "UTF-8");
			HttpPost post = new HttpPost("http://1-dot-postitapp-server.appspot.com/deletenote");
			
			try {
				if(imageId!=""){
					Map probando = cloudinary.uploader().destroy(imageId, null);
					Log.i("ads",probando.toString());
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				HttpResponse response = client.execute(post);
				if(response.getStatusLine().getStatusCode() == 200){
					return true;
				}else{
					return false;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
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
			if(result){
				alertDialog.setTitle("Uploaded");
				alertDialog.setMessage("Note has been successfully uploaded");
				alertDialog.show();
			
			}else{
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Sorry, Note has not been able to upload, try again later.");
				alertDialog.show();
			}

		}

	}

}
