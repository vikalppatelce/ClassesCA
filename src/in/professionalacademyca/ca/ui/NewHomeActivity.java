/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- HOME SCREEN | DASHBOARD
 * DESCRIPTION 		:- HOME SCREEN WITH TICKER AND OPTION. USES TO REGISTERED WITH GCM AND SEND DATA TO SERVER [USE IT LATER IN SENDING REQUEST]
 * SEARCH           :- D: ADAPTER BUTTON SPINNER ASYNCTASK 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    04/03/2014       SPINNER         GET SPINNER DATA     
 * --------------------------------------------------------------------------------------------------------------------
 */
package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.BuildConfig;
import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ServiceDelegate;
import in.professionalacademyca.ca.ui.utils.QustomDialogBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class NewHomeActivity extends SherlockFragmentActivity {
    Typeface stylefont;
    ActionBar actionBar;
    
	TextView ticker;
	TextView timetable,postquery, notification, setup;
	int currentPage;
	boolean onlyOnce = false;
	String tickerText=null;
	final static int HOME = 1007;
	//SA GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCMDemo";
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
//    String SENDER_ID = "777045980104";
    String SENDER_ID = AppConstants.GCM_SENDER_ID;
    
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    JSONArray areaarray,batcharray;//ADDED 10001

    String regid;
	//EA GCM
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
		context = getApplicationContext();

		if (isNetworkAvailable()) {
			fetchTickerData();
			// SA GCM

			if (BuildConfig.DEBUG) {
				Log.i("REG_ID",CA.getSharedPreferences().getString("registration_id","Not yet Registered"));
				Log.i("VERSION", String.valueOf(getAppVersion(context)));
				Log.i("SENDER_ID", SENDER_ID);
			}

			if (checkPlayServices()) {
				gcm = GoogleCloudMessaging.getInstance(this);
				regid = getRegistrationId(context);
				Log.i("REG_ID", regid);
				if (TextUtils.isEmpty(regid)) {
					registerInBackground();
					CA.getPreferences().setFirstTime(false);
				}
			} else {
				Log.i(TAG, "No valid Google Play Services APK found.");
			}
			
//			try {
//				if (!CA.getPreferences().getFirstTime()) {
//					if (!CA.getSharedPreferences().getBoolean("isRegisteredToServer", false)) {
//						sendRegistrationIdToBackend();
//					}
//				}
//			} catch (Exception e) {
//			}
			
			// EA GCM
		} else {
			Toast.makeText(NewHomeActivity.this,"Please check your internet connection", Toast.LENGTH_SHORT).show();
		}
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}	
//	D: CHECK PLAY SERVICES ON RESUME [PLAYSERVICES GCM]
	//SA GCM
	@Override
	protected void onResume() {
	    super.onResume();
	    checkPlayServices();
	}
//	D: CHECK PLAY SERVICES EXIST ON THE DEVICE OR NOT [PLAYSERVICES GCM]	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (TextUtils.isEmpty(registrationId)) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
//	    return getSharedPreferences(NewHomeActivity.class.getSimpleName(),Context.MODE_PRIVATE);
		return CA.getSharedPreferences();
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	public void registerInBackground()
	{
		new GCMRegisterTask().execute();
	}
	
	private class GCMRegisterTask extends AsyncTask<Void, Void, String>
	{
		String msg = "";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(Void... params) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
                
                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                if(!CA.getSharedPreferences().getBoolean("isRegisteredToServer", false))
                {
                sendRegistrationIdToBackend();
                }
                else
                {
                	Log.i("AndroidToServer", "Already registered to server");
                }
            } 
            catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("GCM", msg);
		}
	}
	
	private class SendToServerTask extends AsyncTask<JSONObject, Void, Void>
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
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.REGISTERED_TO_SERVER_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					if(status)
					{
						try {
		                    // Getting JSON Array node
							//SERVERDEMO
							final SharedPreferences prefs = getGCMPreferences(context);
						    Log.i(TAG, "Saving regId on server ");
						    SharedPreferences.Editor editor = prefs.edit();
						    editor.putBoolean("isRegisteredToServer", true);
						    editor.commit();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
					}
					}
				Log.e("PushServer",jsonStr.toString());
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
		}
	}


	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
	    // Your implementation here.
		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		JSONObject jsonObject = RequestBuilder.getPushNotificationData(currentSIMImsi);
		Log.e("PUSH REGID SERVER---->>>>>>>>>>", jsonObject.toString());
		SendToServerTask sendTask = new SendToServerTask();
		sendTask.execute(new JSONObject[]{jsonObject});
	}
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	//EA GCM
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
//		showDialogBox(TextUtils.isEmpty(tickerText)? "Please check your internet connection" : tickerText);
		
		final String HALLOWEEN_RED = "#B40404";
		QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(v.getContext()).
				setTitle("News Flash").
				setTitleColor(HALLOWEEN_RED).
				setDividerColor(HALLOWEEN_RED).
				setMessage(TextUtils.isEmpty(tickerText)? "No data found" : tickerText).
				setFontTitle(stylefont).
				setFontMessage(stylefont);
				
//				setCustomView(R.layout.example_ip_address_layout, v.getContext()).
//				setIcon(getResources().getDrawable(R.drawable.ic_launcher));

		qustomDialogBuilder.show();
		
		
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
