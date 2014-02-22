package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ServiceDelegate;
import in.professionalacademyca.ca.sql.CustomSqlCursorAdapter;
import in.professionalacademyca.ca.sql.DBConstant;
import in.professionalacademyca.ca.ui.utils.CustomToast;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class NotificationActivity extends SherlockFragmentActivity{

	ProgressBar progress;
	ListView listNotification;
	
	ActionBar actionBar;
	Typeface stylefont;
	
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
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
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
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
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
	
	public void uploadNotificationData()
	{
		JSONObject finalJSON = new JSONObject();
		JSONObject tables = new JSONObject();
		try
		{
				JSONArray exp = RequestBuilder.getNotificationData();
				tables.put("tables", exp);
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("jSON - Query", e.toString());
		}		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		JSONObject jsonObject = RequestBuilder.getQueryData(currentSIMImsi, tables);
		Log.e("VIKALP--------------->>>>>>>>>>", jsonObject.toString());
		Log.e("VIKALP--------------->>>>>>>>>>", tables.toString());
		UploadNotificationTask uploadNotificationTask = new UploadNotificationTask();
		uploadNotificationTask.execute(new JSONObject[]{jsonObject});
	}
	
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
			String device_id=null,currentSIMImsi=null;
			String jsonString = dataToSend.toString();
			String query_reply =  null, query_id= null, reply_date=null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.ANSWER_URL, dataToSend);
				TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				currentSIMImsi = mTelephonyMgr.getDeviceId();
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					device_id=jsonObject.getString("device_id");
					/*if(status && currentSIMImsi.equalsIgnoreCase(device_id))
					{
						try {
		                    // Getting JSON Array node
		                    query_answer = jsonObject.getJSONArray("query_answer");
		 
		                    // looping through All Contacts
		                    for (int i = 0; i < query_answer.length(); i++) {
		                        JSONObject c = query_answer.getJSONObject(i);
		                         query_reply="";
		                         query_id="";
		                         reply_date="";
		                        query_reply = c.getString("query_replay");
		                        query_id = c.getString("question_id");
		                        reply_date = c.getString("replay_date");

		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE, query_reply);
		    					contentValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE, reply_date);
		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("ROWS UPDATED : data : ", col + "");
		                        // adding contact to contact list
		                    }
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
					}*/
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
			new SelectDataTask().execute(DBConstant.Query_Columns.CONTENT_URI);
			progress.setVisibility(View.GONE);
		}
	}
	
	private class SelectDataTask extends AsyncTask<Uri, Void ,Cursor> {

		Uri currentUri;
		@Override
		protected void onPreExecute() {
			// this.dialog.setMessage("Getting Names...");
			// this.dialog.show();
			progress.setVisibility(View.VISIBLE);
		}

		// can use UI thread here
		@Override
		protected void onPostExecute(final Cursor result) {

			startManagingCursor(result);
			int[] listFields = new int[] { R.id.question , R.id.answer};
			String[] dbColumns = new String[] { DBConstant.Query_Columns.COLUMN_ID, DBConstant.Query_Columns.COLUMN_QUERY , DBConstant.Query_Columns.COLUMN_RESPONSE};

			NotificationActivity.this.adapterQuery = new CustomSqlCursorAdapter(NotificationActivity.this, R.layout.post_item,result, dbColumns, listFields,currentUri);
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
