package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ServiceDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class NewHomeActivity extends SherlockFragmentActivity {
    Typeface stylefont;
    ActionBar actionBar;
    
	TextView ticker;
	TextView timetable,postquery, notification, setup;
	int currentPage;
	boolean onlyOnce = false;
	String tickerText=null;
	final static int HOME = 1007;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboard);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Professional Academy");
		
		fontActionBar(actionBar.getTitle().toString());
		
		ticker = (TextView)findViewById(R.id.ticker);
		timetable = (TextView)findViewById(R.id.txttimetable);
		postquery = (TextView)findViewById(R.id.txthelp);
		notification = (TextView)findViewById(R.id.txtnotification);
		setup=(TextView)findViewById(R.id.txtsetup);
		
		ticker.setTypeface(stylefont);
		timetable.setTypeface(stylefont);
		postquery.setTypeface(stylefont);
		notification.setTypeface(stylefont);
		setup.setTypeface(stylefont);
		
		ticker.setSelected(true);
		fetchTickerData();
	}
	
	public void fontActionBar(String str)
	{
		try {
			int titleId = getResources().getIdentifier("action_bar_title","id", "android");
			TextView yourTextView = (TextView) findViewById(titleId);
			yourTextView.setText(str);
			yourTextView.setTypeface(stylefont);
		} catch (Exception e) {
			Log.e("ActionBar Style", e.toString());
		}
	}
	
	public void onTimeTable(View v)
	{
//		Intent i = new Intent(this, CourseActivity.class);
		if(CA.getPreferences().getBatch() ==null)
		{
			Intent i = new Intent(this,NewCourseActivity.class);
			i.putExtra("FROM", "Home");
			startActivityForResult(i, HOME);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		}
		else
		{
			Intent i = new Intent(this, TimeTableActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		}
	}
	
	public void onPostQuery(View v)
	{
		Intent i = new Intent(this, PostQueryActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	public void onNotification(View v)
	{
		Intent i = new Intent(this, NotificationActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	public void onSetUp(View v)
	{
		Intent i = new Intent(this, PrefsActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	public void onTicker(View v)
	{
		showDialogBox(TextUtils.isEmpty(tickerText)? "Please check your internet connection" : tickerText);
	}
	
	@SuppressWarnings("deprecation")
	public void showDialogBox(String strTicker)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("News Flash");
		alertDialog.setMessage(strTicker);
		alertDialog.setButton("CLOSE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	public void fetchTickerData()
	{
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		JSONObject jsonObject = RequestBuilder.getTicker(currentSIMImsi);
		Log.e("TICKER--------------->>>>>>>>>>", jsonObject.toString());
		GetTickerTask getTickerTask = new GetTickerTask();
		getTickerTask.execute(new JSONObject[]{jsonObject});
	}
	
	private class GetTickerTask extends AsyncTask<JSONObject, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			JSONObject dataToSend = params[0];
			boolean status = false;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.TICKER_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					if(status)
					{
						try {
		                    // Getting JSON Array node
							tickerText = jsonObject.getString("ticker");
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
					}
					}
				Log.e("TickerTask","");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ticker.setText(tickerText);
		}
	}
}
