/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- SHOW TIME TABLE ACTIVITY
 * DESCRIPTION 		:- SHOW TIME TABLE FOR SELECTED BATCH.
 * SEARCH           :- D: ADAPTER BUTTON SPINNER ASYNCTASK 
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class TimeTableActivity extends SherlockFragmentActivity {

	TextView t1,t2;
	Button next;
	String _date =null;
	
	ProgressBar progress;
	
	ActionBar actionBar;
	Typeface stylefont;
	JSONArray timeTableArray;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
		_date = df.format(Calendar.getInstance().getTime());
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Time Table");
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_today);
		
		t1= (TextView)findViewById(R.id.timetable1);
		t2=(TextView)findViewById(R.id.timetable2);
		next = (Button)findViewById(R.id.next);
		progress = (ProgressBar)findViewById(R.id.progress);
		
		t1.setTypeface(stylefont);
		t2.setTypeface(stylefont);
		next.setTypeface(stylefont);
		
		t1.setText("Time Table for " + CA.getPreferences().getBatch());
		t2.setText("date on " + _date);
	}
//	D: APPLY FONT ON ACTION BAR [FONT ACTIONBAR]
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
//	D: ON OPTION SELECTED. ANIMATION  [OPTION ANIMATION HOME]
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
//	D: APPLY ANIMATION ON BACK [ANIMATION BACK]
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
	
	public void onNext(View v)
	{
		uploadTimeTableData("28-02-2014");
	}
	
	public void uploadTimeTableData(String dat)
	{
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		String _id = null;
		Cursor c  = getContentResolver().query(DBConstant.Area_Columnns.CONTENT_URI, null, DBConstant.Area_Columnns.COLUMN_AREA_NAME + "=?", new String[] {CA.getPreferences().getLevel()}, null);
		
		if(c!=null && c.getCount() > 0)
		{
			c.moveToFirst();
			_id = c.getString(c.getColumnIndex(DBConstant.Area_Columnns.COLUMN_AREA_ID));
		}
		
		c.close();
		
		JSONObject jsonObject = RequestBuilder.getTimeTableData(currentSIMImsi, dat ,_id);
		Log.e("TIMETABLE--------------->>>>>>>>>>", jsonObject.toString());
		getTimeTableTask getTask = new getTimeTableTask();
		getTask.execute(new JSONObject[]{jsonObject});
	}
	
	
	private class getTimeTableTask extends AsyncTask<JSONObject, Void, Void>
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
			String time_date = null, time_remark = null, batchname = null, lecture = null, start = null, end = null, faculty = null, batch_remark = null, areaname = null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.GET_DATA_URL, dataToSend);
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					try {
	                    // Getting JSON Array node
	                    timeTableArray = jsonObject.getJSONArray("timetables");
	 
	                    // looping through All Contacts
	                    for (int i = 0; i < timeTableArray.length(); i++) {
	                        JSONObject c = timeTableArray.getJSONObject(i);
	                        time_date = "";
	                        time_remark = "";
	                        batchname = "";
	                        lecture = ""; 
	                        start = ""; 
	                        end = ""; 
	                        faculty = ""; 
	                        batch_remark = "";
	                        areaname = "";
	                        
	                        
	                        time_date = c.getString("timetable_date");
	                        time_remark = c.getString("timetable_remark");
	                        batchname = c.getString("batchname");
	                        lecture = c.getString("lecture"); 
	                        start = c.getString("start"); 
	                        end = c.getString("end"); 
	                        faculty = c.getString("faculty"); 
	                        batch_remark = c.getString("batch_remark");
	                        areaname = c.getString("areaname");
	                        

	                        ContentValues contentValues = new ContentValues();
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_TIME_TABLE_DATE, time_date);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_REMARK, time_remark);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_BATCH_NAME, batchname);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_LECTURE, lecture);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_START_TIME, start);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_END_TIME, end);
	    					
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_PROFESSOR, faculty);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_BATCH_REMARK, batch_remark);
	    					contentValues.put(DBConstant.Time_Table_Columns.COLUMN_AREA_NAME, areaname);
	    					
	    					getContentResolver().insert(DBConstant.Time_Table_Columns.CONTENT_URI, contentValues);
	    					Log.e("TIME TABLE" , "UPDATED");
	                        // adding contact to contact list
	                    }
	                } catch (Exception e) { //JSONException
	                    e.printStackTrace();
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
//			new SelectDataTask().execute(DBConstant.Query_Columns.CONTENT_URI);
			progress.setVisibility(View.GONE);
		}
	}
//	D: FETCHES DATA FROM DATABASE AND SUPPLIE TO ADAPTER [ADAPTER DATABASE]
//	private class SelectDataTask extends AsyncTask<Uri, Void ,Cursor> {
//
//		Uri currentUri;
//		@Override
//		protected void onPreExecute() {
//			// this.dialog.setMessage("Getting Names...");
//			// this.dialog.show();
//			progress.setVisibility(View.VISIBLE);
//		}
//
//		// can use UI thread here
//		@SuppressWarnings("deprecation")
//		@Override
//		protected void onPostExecute(final Cursor result) {
//
//			startManagingCursor(result);
//			int[] listFields = new int[] { R.id.question , R.id.answer};
//			String[] dbColumns = new String[] { DBConstant.Query_Columns.COLUMN_ID, DBConstant.Query_Columns.COLUMN_QUERY , DBConstant.Query_Columns.COLUMN_RESPONSE};
//
//			TimeTableActivity.this.adapterQuery = new CustomSqlCursorAdapter(TimeTableActivity.this, R.layout.post_item,result, dbColumns, listFields,currentUri);
//			TimeTableActivity.this.listQuery.setAdapter(TimeTableActivity.this.adapterQuery);
//			progress.setVisibility(View.GONE);
//		}
//
//		@Override
//		protected Cursor doInBackground(Uri... arg0) {
//			// TODO Auto-generated method stub
//			try {
//				currentUri = arg0[0];
//				return TimeTableActivity.this.getContentResolver().query(currentUri, null, null, null, DBConstant.Query_Columns.COLUMN_ID+" DESC");
//			} catch (SQLException sqle) {
//				throw sqle;
//			}
//		}
//	}
}