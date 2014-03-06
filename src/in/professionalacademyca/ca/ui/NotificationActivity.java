/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- NOTIFICATION SCREEN
 * DESCRIPTION 		:- FETCH LATEST NOTIFICATION , SWIPE TO DISMISS NOTIFICATION API > 12
 * SEARCH           :- D: ADAPTER BUTTON SPINNER ASYNCTASK JSON SERVICES API 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    04/03/2014       				
 * --------------------------------------------------------------------------------------------------------------------
 */

package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ServiceDelegate;
import in.professionalacademyca.ca.sql.DBConstant;
import in.professionalacademyca.ca.sql.NotificationSqlCursorAdapter;
import in.professionalacademyca.ca.ui.utils.CustomToast;
import in.professionalacademyca.ca.ui.utils.SwipeDismissListViewTouchListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class NotificationActivity extends SherlockFragmentActivity{

	ProgressBar progress;
	ListView listNotification;
	
	ActionBar actionBar;
	Typeface stylefont;
	
	JSONArray notification;
	
	CursorAdapter adapterQuery;
	final static int NOTIFICATION = 1001;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		
		listNotification = (ListView)findViewById(R.id.notificationlist);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Notification");
		progress = (ProgressBar)findViewById(R.id.progress);
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_myplaces);
		
		if(CA.getPreferences().getBatch() ==null)
		{
			Intent i = new Intent(this,NewCourseActivity.class);
			i.putExtra("FROM", "Notification");
			startActivityForResult(i, NOTIFICATION);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		}
		else
		{
			if(isNetworkAvailable())
			{
				uploadNotificationData();
			}
			else
			{
				CustomToast.showToastMessage(this, "Please check your internet connection");
			}
		}
		
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                		listNotification,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                	try
                                	{
                                	long _id  = adapterQuery.getItemId(position);
                                	getContentResolver().delete(DBConstant.Notification_Columnns.CONTENT_URI, "_id=?", new String[] { String.valueOf(_id) });
                                	CustomToast.showToastMessage(NotificationActivity.this, "Keep it Clean!");
                                	}
                                	catch(Exception e)
                                	{
                                		Log.e("Swipe to Dismiss", e.toString());
                                	}
                                }
                                adapterQuery.notifyDataSetChanged();
                            }
                        });
		
		if(Build.VERSION.SDK_INT > 12)
		{
		listNotification.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
		listNotification.setOnScrollListener(touchListener.makeScrollListener());
			}
	}
//	D: CHECK NETWORK AVAILABILITY [UTILITY].
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
//	D: APPLY FONT ON ACTION BAR [ACTIONBAR]
	public void fontActionBar(String str)
	{
		try {
			int titleId = getResources().getIdentifier("action_bar_title",
					"id", "android");
			TextView yourTextView = (TextView) findViewById(titleId);
			yourTextView.setText(str);
			yourTextView.setTypeface(stylefont);
		} catch (Exception e) {
			Log.e("ActionBar Style", e.toString());
		}
	}
//	D: APPLY ANIMATION ON BACK PRESSED [BACK ANIMATION]
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
//	D: APPLY ON OPTION SELECTED FUNCTIONALITY [OPTION MENU]
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
//	D: ON ACTIVITY RESULT [IF BATCH IS NOT SELECTED THEN IT ASK AND GET BACK TO HERE]. [ACTIVITY]
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK)
		{
			if(isNetworkAvailable())
			{
				uploadNotificationData();
			}
			else
			{
				CustomToast.showToastMessage(this, "Please check your internet connection");
			}
		}
	}
//	
	private static long strDateToUnixTimestamp(String dt) {
        DateFormat formatter;
        Date date = null;
        long unixtime;
        formatter = new SimpleDateFormat("dd/MM/yy");
        try {
            date = formatter.parse(dt);
        } catch (ParseException ex) {
 
            ex.printStackTrace();
        }
        unixtime = date.getTime() / 1000L;
        return unixtime;
    }
//	
	
//	D: GATHERED NOTIFICATION INFORMATION SENT TO SERVICES [JSON SERVICE API]
	public void uploadNotificationData()
	{
		long  notificationID=0;
		try
		{
			Cursor c = getContentResolver().query(DBConstant.Notification_Temp_Columnns.CONTENT_URI, null, null, null, DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID + " DESC");
			if(c!=null && c.getCount()>0)
			{
				c.moveToFirst();
				notificationID = Long.parseLong(c.getString(c.getColumnIndex(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID)));
			}
			else
			{
				//VIKALP
				try
				{
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String _date = df.format(Calendar.getInstance().getTime());
					notificationID = strDateToUnixTimestamp(_date);
					Log.i("Notification ID", String.valueOf(notificationID) + ": "+ System.currentTimeMillis()/1000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			    //VIKALP
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("jSON - Query", e.toString());
		}		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		
		JSONObject jsonObject = RequestBuilder.getQueryNotificationData(currentSIMImsi,notificationID);
		Log.e("NOTIFICATION------>>>>>>>>>>", jsonObject.toString());
		UploadNotificationTask uploadNotificationTask = new UploadNotificationTask();
		uploadNotificationTask.execute(new JSONObject[]{jsonObject});
	}
//	D: FETCHES NOTIFICATION DATA AND INSERT INTO DATABASE FROM SERVIECS [ASYNCTASK JSON SERVICES API].
	private class UploadNotificationTask extends AsyncTask<JSONObject, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			JSONObject dataToSend = params[0];
			boolean status = false;
			String type =  null, notification_txt= null, notification_date=null,notification_id=null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.NOTIFICATION_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					if(status)
					{
						try {
		                    // Getting JSON Array node
		                    notification = jsonObject.getJSONArray("notification");
		 
		                    // looping through All Contacts
		                    for (int i = 0; i < notification.length(); i++) {
		                        JSONObject c = notification.getJSONObject(i);
		                         type="";
		                         notification_date="";
		                         notification_txt="";
		                         notification_id="";
		                        type = c.getString("notification_type");
		                        notification_date = c.getString("notification_date");
		                        notification_txt = c.getString("notification_text");
		                        notification_id = c.getString("created_on");

		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_TITLE, notification_txt);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE, notification_date);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_BATCH, type);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID, notification_id);
		    					getContentResolver().insert(DBConstant.Notification_Columnns.CONTENT_URI, contentValues);
		    					
		    					
		                        ContentValues tempValues = new ContentValues();
		    					tempValues.put(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID, notification_id);
		    					getContentResolver().insert(DBConstant.Notification_Temp_Columnns.CONTENT_URI, tempValues);
		    					
		    					
//		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("Notification","New Notifcation Added");
		                        // adding contact to contact list
		                    }
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
					}
					}
				Log.e("UploadTask","");
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
			new SelectDataTask().execute(DBConstant.Notification_Columnns.CONTENT_URI);
			progress.setVisibility(View.GONE);
		}
	}
//	D: SUPPLY DATA FROM DATABASE TO ADAPTER. [AYSNCTASK JSON DATABASE ADAPTER]
	private class SelectDataTask extends AsyncTask<Uri, Void ,Cursor> {

		Uri currentUri;
		@Override
		protected void onPreExecute() {
			// this.dialog.setMessage("Getting Names...");
			// this.dialog.show();
			progress.setVisibility(View.VISIBLE);
		}

		// can use UI thread here
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(final Cursor result) {

			startManagingCursor(result);
			int[] listFields = new int[] { R.id.question , R.id.answer};
			String[] dbColumns = new String[] { DBConstant.Notification_Columnns.COLUMN_ID, DBConstant.Notification_Columnns.COLUMN_TITLE , DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE};

			NotificationActivity.this.adapterQuery = new NotificationSqlCursorAdapter(NotificationActivity.this, R.layout.post_item,result, dbColumns, listFields,currentUri);
			NotificationActivity.this.listNotification.setAdapter(NotificationActivity.this.adapterQuery);
			progress.setVisibility(View.GONE);
		}

		@Override
		protected Cursor doInBackground(Uri... arg0) {
			// TODO Auto-generated method stub
			try {
				currentUri = arg0[0];
				return NotificationActivity.this.getContentResolver().query(currentUri, null, null, null, DBConstant.Query_Columns.COLUMN_ID+" DESC");
			} catch (SQLException sqle) {
				throw sqle;
			}
		}
	}

}
