package dit.upm.es.postitapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
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

	ImageView imageCamera;
	ImageButton deleteImageButton;
	ImageButton changeImageButton;
	Cloudinary cloudinary;

	boolean remImage=false;
	Bitmap bmp;
	boolean newImage=false;

	Uri imageUri;

	String imageId;

	final int CAMERA_ACT = 0 ;

	private TextView tvDisplayDate;
	private ImageButton btnChangeDate;
	private ImageButton btnDeleteDate;
	private DatePickerDialog dateDialog;

	private final Calendar c = Calendar.getInstance();

	private int year;
	private int month;
	private int day;
	private String ttlGuardado;
	private LinearLayout myLayout;

	private TextView lineColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_it_edit);
		titleEditText = (EditText) findViewById(R.id.titleNote);
		contentEditText = (EditText) findViewById(R.id.contentNote);
		Button sendButton = (Button) findViewById(R.id.sendButton);
		radioGroupColors = (RadioGroup) findViewById(R.id.radioGroupColorNotes);

		imageCamera = (ImageView) findViewById(R.id.imageCamera);
		deleteImageButton = (ImageButton) findViewById(R.id.deleteImage);
		changeImageButton = (ImageButton) findViewById(R.id.changeImage);

		myLayout = (LinearLayout) findViewById(R.id.layoutEdit);
		lineColor =(TextView) findViewById(R.id.lineNote);
		
		
		client = new DefaultHttpClient();
		alertDialog = new AlertDialog.Builder(this).create();
		progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setMessage("Loading...");

		cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(this));

		Bundle extras = getIntent().getExtras();
		Note note = (Note)extras.get("Note");

		titleEditText.setText(note.getTitle());
		contentEditText.setText(note.getText());
		imageId = note.getImageId();
		switch(note.getColorNote()){
		case BLUE: {
			radioGroupColors.check(R.id.colorNoteBlue);
			myLayout.setBackgroundColor(Color.parseColor("#E8E8F8"));
			lineColor.setBackgroundColor(Color.parseColor("#6D8EDB"));
			break;						
		}
		case YELLOW: {
			radioGroupColors.check(R.id.colorNoteYellow);
			myLayout.setBackgroundColor(Color.parseColor("#F7F6E8"));
			lineColor.setBackgroundColor(Color.parseColor("#FFFF30"));
			break;						
		}
		case RED: {
			radioGroupColors.check(R.id.colorNoteRed);
			myLayout.setBackgroundColor(Color.parseColor("#F7E8E8"));
			lineColor.setBackgroundColor(Color.parseColor("#D13636"));			
			break;						
		}
		case GREEN: {
			radioGroupColors.check(R.id.colorNoteGreen);
			myLayout.setBackgroundColor(Color.parseColor("#E6F4E8"));
			lineColor.setBackgroundColor(Color.parseColor("#12EA21"));
			break;						
		}
		default: {
			radioGroupColors.check(R.id.colorNoteBlue);
			myLayout.setBackgroundColor(Color.parseColor("#E8E8F8"));
			lineColor.setBackgroundColor(Color.parseColor("#6D8EDB"));
			break;
		}

		}

		
radioGroupColors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Log.i("checked", checkedId+"");
				View radioButton = radioGroupColors.findViewById(checkedId);
				int idx = radioGroupColors.indexOfChild(radioButton);
				colorNoteSelected = getColorNote(idx);
			}
		});
		
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		ttlGuardado = note.getTTL();
		dateDialog = new DatePickerDialog(this, datePickerListener, 
				year, month,day);
		dateDialog.getDatePicker().setMinDate(c.getTimeInMillis());
		setCurrentDateOnView(ttlGuardado);
		btnChangeDate = (ImageButton) findViewById(R.id.btnChangeDate);
		btnDeleteDate = (ImageButton) findViewById(R.id.btnDeleteDate);


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
						String title = titleEditText.getText().toString();
						String content = contentEditText.getText().toString();
						Intent intent = new Intent();
						intent.putExtra("title",title);
						intent.putExtra("content",content);
						intent.putExtra("colorNote",colorNoteSelected);
						setResult(RESULT_OK, intent);
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
				remImage=true;
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

		new GetImageNote().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(imageCamera.getDrawable() != null){
			changeImageButton.setVisibility(View.VISIBLE);
			deleteImageButton.setVisibility(View.VISIBLE);
			imageCamera.setVisibility(View.VISIBLE);
		}else{
			changeImageButton.setVisibility(View.GONE);
			deleteImageButton.setVisibility(View.GONE);
			imageCamera.setVisibility(View.GONE);
		}
		View radioButton = radioGroupColors.findViewById(radioGroupColors.getCheckedRadioButtonId());
		int idx = radioGroupColors.indexOfChild(radioButton);
		colorNoteSelected = getColorNote(idx);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(imageCamera.getDrawable() != null){
			changeImageButton.setVisibility(View.VISIBLE);
			deleteImageButton.setVisibility(View.VISIBLE);
			imageCamera.setVisibility(View.VISIBLE);
		}else{
			changeImageButton.setVisibility(View.GONE);
			deleteImageButton.setVisibility(View.GONE);
			imageCamera.setVisibility(View.GONE);
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		InputStream stream = null;
		if (requestCode==CAMERA_ACT && resultCode==RESULT_OK) {

			// recyle unused bitmaps
			if (bmp != null) {
				bmp.recycle();
			}
			try {
				stream = getContentResolver().openInputStream(data.getData());
				bmp = BitmapFactory.decodeStream(stream);
				imageCamera.setImageBitmap(bmp);
				imageUri = data.getData();
				newImage = true;
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



		}
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

			if(remImage && !imageId.equals("")){
				pairs.add (new BasicNameValuePair("imageId",""));
				try {
					cloudinary.uploader().destroy(imageId, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				if(newImage){
					InputStream in;
					String imageCloudinaryURL = null;
					if(imageId.equals("")){
						//subirsolo
						try {
							in = getContentResolver().openInputStream(imageUri);

							Map param = new HashMap<String, String>();
							Map uploadResult;

							uploadResult = cloudinary.uploader().upload(in, param);
							imageCloudinaryURL = (String) uploadResult.get("public_id");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}else{
						try {
							cloudinary.uploader().destroy(imageId, null);
							in = getContentResolver().openInputStream(imageUri);

							Map param = new HashMap<String, String>();
							Map uploadResult;

							uploadResult = cloudinary.uploader().upload(in, param);
							imageCloudinaryURL = (String) uploadResult.get("public_id");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					pairs.add(new BasicNameValuePair("imageId", imageCloudinaryURL));
				}else{
					pairs.add (new BasicNameValuePair("imageId",""+ imageId));
				}


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
			progressBar.setTitle("Uploading an edited Note");
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


	public ColorNote getColorNote(int idxColor){

		ColorNote result;
		switch (idxColor) {
		case 0:
			result = ColorNote.BLUE;
			myLayout.setBackgroundColor(Color.parseColor("#E8E8F8"));
			lineColor.setBackgroundColor(Color.parseColor("#6D8EDB"));
			break;
		case 1:
			result = ColorNote.YELLOW;
			myLayout.setBackgroundColor(Color.parseColor("#F7F6E8"));
			lineColor.setBackgroundColor(Color.parseColor("#FFFF30"));
			break;
		case 2:
			result = ColorNote.RED;
			Log.i("checked", "red");
			myLayout.setBackgroundColor(Color.parseColor("#F7E8E8"));
			lineColor.setBackgroundColor(Color.parseColor("#D13636"));
			break;
		case 3:
			result = ColorNote.GREEN;
			myLayout.setBackgroundColor(Color.parseColor("#E6F4E8"));
			lineColor.setBackgroundColor(Color.parseColor("#12EA21"));
			break;

		default:
			result = ColorNote.BLUE;
			myLayout.setBackgroundColor(Color.parseColor("#E8E8F8"));
			lineColor.setBackgroundColor(Color.parseColor("#6D8EDB"));
			break;
		}
		return result;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_it_edit, menu);
		return true;
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
		}
		return super.onOptionsItemSelected(item);
	}



	private class GetImageNote extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			if(!imageId.equals("") ){
				try {
					URL urlImage;

					urlImage = new URL(cloudinary.url().generate(imageId));
					bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


			return bmp;
		}

		@Override
		protected void onPreExecute() {
			progressBar.setCancelable(false);
			progressBar.setMax(1);
			progressBar.setTitle("Loading...");
			progressBar.setProgress(0);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(Bitmap bmp) {

			if(!imageId.equals("")){
				imageCamera.setImageBitmap(bmp);
				imageCamera.setVisibility(View.VISIBLE);
				changeImageButton.setVisibility(View.VISIBLE);
				deleteImageButton.setVisibility(View.VISIBLE);
			}else{
				changeImageButton.setVisibility(View.GONE);
				imageCamera.setVisibility(View.GONE);
				deleteImageButton.setVisibility(View.GONE);
			}
			progressBar.dismiss();

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

	public void setCurrentDateOnView(String ttlGuardado) {

		tvDisplayDate = (TextView) findViewById(R.id.tvDate);

		if(!ttlGuardado.equals("")){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date expirationDateGuardada = formatter.parse(ttlGuardado);
				c.setTime(expirationDateGuardada);
				tvDisplayDate.setText(ttlGuardado);
				dateDialog.updateDate(year, month, day);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			tvDisplayDate.setText("No date selected");
		}

	}


}
