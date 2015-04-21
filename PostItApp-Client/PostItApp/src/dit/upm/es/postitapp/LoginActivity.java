package dit.upm.es.postitapp;

import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LoginActivity extends FragmentActivity {

	private MainFragment mainFragment;
	private LoginButton loginBtn;
	private UiLifecycleHelper uiHelper;
	private ProgressDialog progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(android.R.id.content, mainFragment)
			.commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
		loginBtn = (LoginButton) findViewById(R.id.authButton);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(progressBar!=null && progressBar.isShowing()){
			progressBar.dismiss();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(progressBar!=null && progressBar.isShowing()){
			progressBar.dismiss();
		}
		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(progressBar!=null && progressBar.isShowing()){
			progressBar.dismiss();
		}
		uiHelper.onDestroy();
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

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {


		@Override
		public void call(Session session, SessionState state,Exception exception) {
			// TODO Auto-generated method stub
			if (state.isOpened()) {
				String userId = session.getAccessToken();
				startMain(userId);
			} else if (state.isClosed()) {
				
			}

		}
	};

	public void startMain(String userId){
		
		Intent i = new Intent(this,MainActivity.class);
		i.putExtra("userID", userId);
		startActivity(i);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

}
