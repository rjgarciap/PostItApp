package dit.upm.es.postitapp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class PostItUpload extends Activity{

	HttpClient client;
	ProgressDialog progressBar;

	EditText titleEditText;
	EditText contentEditText;
	Button sendButton;
	AlertDialog alertDialog;
	RadioGroup radioGroupColors;
	ImageView imageCamera;
	Cloudinary cloudinary;
	String imageCloudinaryURL="";
	
	ImageButton deleteImageButton;
	ImageButton changeImageButton;
	Bitmap bp;

	TextView resultList;
	ByteArrayInputStream bs;
	private TextView tvDisplayDate;
	private ImageButton btnChangeDate;
	private ImageButton btnDeleteDate;
	private DatePickerDialog dateDialog;
 
	private final Calendar c = Calendar.getInstance();
	
	private int year;
	private int month;
	private int day;

	int idxColor;
	ColorNote colorNoteSelected;
	final int CAMERA_ACT = 0 ;
    private static final int PICK_FRIENDS_ACTIVITY = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_upload);

		cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(this));
		client = new DefaultHttpClient();
		alertDialog = new AlertDialog.Builder(this).create();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Uploading...");

		titleEditText = (EditText) findViewById(R.id.titleText);
		contentEditText = (EditText) findViewById(R.id.contentText);
		imageCamera = (ImageView) findViewById(R.id.imageCamera);
		sendButton = (Button) findViewById(R.id.sendButton);

		deleteImageButton = (ImageButton) findViewById(R.id.deleteImage);
		changeImageButton = (ImageButton) findViewById(R.id.changeImage);
		
		radioGroupColors = (RadioGroup) findViewById(R.id.radioGroupColorNotes);

		resultList=(TextView) findViewById(R.id.resultList);
		
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



				
				alertDialog.setButton(RESULT_OK, "Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Finish activity
						finish();
					}
				});


			}
		});


		deleteImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bs = null;
				imageCamera.setImageBitmap(null);
				deleteImageButton.setVisibility(View.GONE);
				changeImageButton.setVisibility(View.GONE);
			}
		});

		changeImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openCamera();
			}
		});
		
		
		setCurrentDateOnView();
		btnChangeDate = (ImageButton) findViewById(R.id.btnChangeDate);
		btnDeleteDate = (ImageButton) findViewById(R.id.btnDeleteDate);
		dateDialog = new DatePickerDialog(this, datePickerListener, 
                year, month,day);
		
		dateDialog.getDatePicker().setMinDate(c.getTimeInMillis());
		
		btnChangeDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dateDialog.show();
				
			}
		});
		
		btnDeleteDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				
				dateDialog.updateDate(year, month, day);
				
				// set current date into textview
				tvDisplayDate.setText("No date selected");
				
			}
		});
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(imageCamera.getDrawable() != null){
			deleteImageButton.setVisibility(View.VISIBLE);
			changeImageButton.setVisibility(View.VISIBLE);
			imageCamera.setVisibility(View.VISIBLE);
		}else{
			deleteImageButton.setVisibility(View.GONE);
			changeImageButton.setVisibility(View.GONE);
			imageCamera.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bp != null){
			bp.recycle();
			bp=null;
		}
	}
	
	public void openCamera(){
		 // Check if there is a camera.
	    Context context = this;
	    PackageManager packageManager = context.getPackageManager();
	    if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
	        Toast.makeText(this, "This device does not have a camera.", Toast.LENGTH_SHORT)
	                .show();
	        return;
	    }
		
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_ACT);
	}
	
	public void openFriendList(){
		Intent intent = new Intent(this, PickFriendsActivity.class);
        // Note: The following line is optional, as multi-select behavior is the default for
        // FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
        // friend picker if single-select functionality was desired, or if a different user ID was
        // desired (for instance, to see friends of a friend).
        PickFriendsActivity.populateParameters(intent, null, true, true);
        startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
	}


	@Override    
	protected void onPause() {
		super.onPause();  
		if(imageCamera.getDrawable() != null){
			deleteImageButton.setVisibility(View.VISIBLE);
			changeImageButton.setVisibility(View.VISIBLE);
			imageCamera.setVisibility(View.VISIBLE);
		}else{
			deleteImageButton.setVisibility(View.GONE);
			changeImageButton.setVisibility(View.GONE);
			imageCamera.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_it_upload, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		InputStream stream = null;
		if (requestCode==CAMERA_ACT && resultCode==RESULT_OK) {
			
			 // recyle unused bitmaps
	        if (bp != null) {
	          bp.recycle();
	        }
	        try {
	        	
				stream = getContentResolver().openInputStream(data.getData());
				 bp = BitmapFactory.decodeStream(stream);
				 ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				 bp.compress(CompressFormat.JPEG,30, bos); 
				 byte[] bitmapdata = bos.toByteArray();
				 bs = new ByteArrayInputStream(bitmapdata);
				 
				 imageCamera.setImageBitmap(bp);
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
		            if (stream != null)
		              try {
		                stream.close();
		              } catch (IOException e) {
		                e.printStackTrace();
		              }
		          }
		      
		
	       
		}else{
			if(requestCode==PICK_FRIENDS_ACTIVITY && resultCode==RESULT_OK){
				String results = "";
		        FriendPickerApplication application = (FriendPickerApplication) getApplication();

		        Collection<GraphUser> selection = application.getSelectedUsers();
		        if (selection != null && selection.size() > 0) {
		            ArrayList<String> names = new ArrayList<String>();
		            for (GraphUser user : selection) {
		                names.add(user.getName());
		            }
		            results = TextUtils.join(", ", names);
		        } else {
		            results = "<No friends selected>";
		        }
                resultList.setText(results);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.camera) {
			openCamera();
			return true;
		}else{
			if(id==R.id.pickFriends){
				openFriendList();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
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

	private class PostNote extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			HttpPost post = new HttpPost("http://1-dot-postitapp-server.appspot.com/postnote");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			pairs.add(new BasicNameValuePair("title", titleEditText.getText().toString()));
			pairs.add(new BasicNameValuePair("content", contentEditText.getText().toString()));

			String ttlString="";
	 		if(!tvDisplayDate.getText().equals("No date selected")){
				
				StringBuilder ttlStringBuilder = new StringBuilder();
				
				ttlStringBuilder.append(year).append("-");
				
				if(month < 10){
					ttlStringBuilder.append("0").append(month + 1).append("-");
				}else{
					ttlStringBuilder.append(month + 1).append("-");
				}
				
				if(day < 10){
					ttlStringBuilder.append("0").append(day).append("-");
				}else{
					ttlStringBuilder.append(day);
				}
				ttlString = ttlStringBuilder.toString();
	 		}
			
	 		pairs.add(new BasicNameValuePair("ttl", ttlString));
			
			InputStream in;
			try {
				
				if(bs != null){
					Map param = new HashMap<String, String>();
					Map uploadResult= cloudinary.uploader().upload(bs, param);
					imageCloudinaryURL = (String) uploadResult.get("public_id");
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Bundle extras = getIntent().getExtras();
			Double lat = extras.getDouble("lat");
			Double lon = extras.getDouble("long");
			String userId = extras.getString("userId");
			pairs.add(new BasicNameValuePair("lat", ""+lat));
			pairs.add(new BasicNameValuePair("long", ""+lon));
			pairs.add(new BasicNameValuePair("userId", ""+userId));
			pairs.add(new BasicNameValuePair("colorNote", ""+colorNoteSelected.toString()));
			pairs.add(new BasicNameValuePair("imageId", imageCloudinaryURL));
			
			
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
				//Obtener respuesta
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
			progressBar.setTitle("Uploading a new Note");
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

	private DatePickerDialog.OnDateSetListener datePickerListener 
	= new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			StringBuilder dateString = new StringBuilder();
			
			dateString.append(year).append("-");
			
			if(month < 10){
				dateString.append("0").append(month + 1).append("-");
			}else{
				dateString.append(month + 1).append("-");
			}
			
			if(day < 10){
				dateString.append("0").append(day).append("-");
			}else{
				dateString.append(day);
			}
			tvDisplayDate.setText(dateString);

		}
	}; 
 
	// display current date
	public void setCurrentDateOnView() {
 
		tvDisplayDate = (TextView) findViewById(R.id.tvDate);
 
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
 
		// set current date into textview
		tvDisplayDate.setText("No date selected");
		
	}


}
