/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- SELECT COURSE ACTIVITY
 * DESCRIPTION 		:- SAVE AREA AND BATCH IN PREFERENCES [USE IT LATER IN SENDING REQUEST]
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
import in.professionalacademyca.ca.ui.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class NewCourseActivity  extends SherlockFragmentActivity implements OnItemSelectedListener{

	Spinner spin_course,spin_city,spin_area,spin_batch;
	CheckBox chk_default;
	TextView header,txtcourse,txtbatch,txtsetdefault;
	Button go;
	ActionBar actionBar;
	String fromWhere;
	ProgressDialog pDialog;
	
	ArrayAdapter adap_course,adap_city,adap_area,adap_batch;
	
	JSONArray batcharray , areaarray;
	
	static Typeface stylefont;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_course_select);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Course");
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_set_as);
		try
		{
			fromWhere = getIntent().getExtras().getString("FROM");	
		}
		catch(Exception e)
		{
			Log.e("Traversal", e.toString());
		}
		
		
		spin_course = (Spinner)findViewById(R.id.spin_course);
		spin_batch = (Spinner)findViewById(R.id.spin_batch);
		
		chk_default = (CheckBox)findViewById(R.id.set_default);
		
		txtbatch = (TextView)findViewById(R.id.batch);
		txtcourse = (TextView)findViewById(R.id.course);
		txtsetdefault = (TextView)findViewById(R.id.txtsetdefault);
		go =(Button)findViewById(R.id.go);
				
		chk_default.setTypeface(stylefont);
		txtbatch.setTypeface(stylefont);
		txtcourse.setTypeface(stylefont);
		txtsetdefault.setTypeface(stylefont);
		go.setTypeface(stylefont);
		
		if(isNetworkAvailable())
		{
			getSpinnerData();	
		}
		
		
//		adap_course = ArrayAdapter.createFromResource(this, R.array.arr_course_level, android.R.layout.simple_spinner_dropdown_item);
		try {
			// List<String> list_course = getAllAreas();
			// String [] arr_course = list_course.toArray(new
			// String[list_course.size()]);
			// adap_course = new CustomArrayAdapter<CharSequence>(this,
			// arr_course);

			spin_course.setOnItemSelectedListener(this);
			// adap_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// spin_course.setAdapter(adap_course);

			spin_batch.setOnItemSelectedListener(this);

		} catch (Exception e) {

		}		 
		
//		String [] arr_course = getResources().getStringArray(R.array.arr_course_level);        
		
	}
	
	public List<String> getAllAreas(){
        List<String> labels = new ArrayList<String>();
        
        Cursor cursor = getContentResolver().query(DBConstant.Area_Columnns.CONTENT_URI, null, null, null, null);
      
        if (cursor!=null && cursor.getCount() > 0) {
            while (cursor.moveToNext())
            {
            	labels.add(cursor.getString(cursor.getColumnIndex(DBConstant.Area_Columnns.COLUMN_AREA_NAME)));
            }
        }
        cursor.close();
        return labels;
    }
	
	public List<String> getBatch(String area){
        List<String> labels = new ArrayList<String>();
        
        Cursor cursor = getContentResolver().query(DBConstant.Batch_Columns.CONTENT_URI, null, DBConstant.Batch_Columns.COLUMN_AREA_ID + "=?", new String[]{area}, null);
      
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(DBConstant.Batch_Columns.COLUMN_NAME)));
            } while (cursor.moveToNext());
        }
        return labels;
    }
	
//	D: CHANGE CUSTOM FONT OF ACTION BAR [FONT ACTIONBAR]
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
//	D: ANIMATION ON BACK PRESSED [ANIMATION BACK]
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
//	D: BACK ON HOME OPTION [ACTION BAR]
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
//	D: CALLING ON LINEAR LAYOUT OF SET DEFAULT CHECK BOX [TOGGLE CHECKBOX]
	public void onSetDefault(View v)
	{
		if(chk_default.isChecked())
		{
			chk_default.setChecked(false);
		}
		else
		{
		chk_default.setChecked(true);
		}
	}
//	D: ON CLICK OF GO, SAVE BATCH & AREA IN PREFERENCES. INTENT TO TIME TABLE SCREEN [BUTTON EVENT]
	public void onGo(View v)
	{
		if(validate())
		{
			if(chk_default.isChecked())
			{
				CA.getPreferences().setDefault(true);
				CA.getPreferences().setBatch(spin_batch.getSelectedItem().toString().trim());
				CA.getPreferences().setLevel(spin_course.getSelectedItem().toString().trim());
			}
			
			if(fromWhere.equalsIgnoreCase("Notification"))
			{
				setResult(RESULT_OK);
				finish();
				overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
			}
			else if(fromWhere.equalsIgnoreCase("Home"))
			{
			Intent timeTable = new Intent(this, TimeTableActivity.class);
			timeTable.putExtra("isBatch", spin_batch.getSelectedItem().toString().trim());
			startActivity(timeTable);
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
			else if(fromWhere.equalsIgnoreCase("Settings"))
			{
				Intent timeTable = new Intent(this, TimeTableActivity.class);
				timeTable.putExtra("isBatch", spin_batch.getSelectedItem().toString().trim());
				startActivity(timeTable);
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
		}
		else
		{
			CustomToast.showToastMessage(this,"Please select Batch");
		}
	}
//	D: VALIDATE SPINNER SELECT [SPINNER]
	public boolean validate()
	{
		if(spin_batch.getSelectedItem().toString()!=null && spin_batch.getSelectedItem().toString().length() > 0)
		{
		return true;	
		}
		return false;
	}
//	D: CUSTOM ARRAY ADAPTER. FILL DATA IN SPINNER [BATCH & AREA]. CREATED FOR CUSTOM FONT STYLE. [ARRAYADAPTER ADAPTER]
	static class CustomArrayAdapter<T> extends ArrayAdapter<T>
	{
	    public CustomArrayAdapter(Context ctx, T [] objects)
	    {
	        super(ctx, android.R.layout.simple_spinner_item, objects);
	    }

	    //other constructors

	    @Override
		public TextView getView(int position, View convertView, ViewGroup parent) {
	    	TextView v = (TextView) super.getView(position, convertView, parent);
	    	v.setTypeface(stylefont);
	    	v.setPadding(15, 15, 0, 15);
	    	return v;
	    	}
	    
	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent)
	    {
	        View view = super.getView(position, convertView, parent);

	            TextView text = (TextView)view.findViewById(android.R.id.text1);
	            text.setTypeface(stylefont);
	            text.setPadding(15, 15, 0, 15);
	        return view;
	    }
	}
//	D: CHANGE RESPECTIVE ARRAY ON SELECT OF SPINNER.[SPINNER ITEM SELECTED]
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int pos_course;
		String area_name;
		String area_id = null;
		int which_spinner;
		String [] arr_batch = null;
		
		pos_course = spin_course.getSelectedItemPosition();
		area_name = spin_course.getSelectedItem().toString();
		which_spinner = parent.getId();
		if(which_spinner == R.id.spin_course)
		{
			//VIKALP
			
			Cursor cursor = getContentResolver().query(DBConstant.Area_Columnns.CONTENT_URI, null, DBConstant.Area_Columnns.COLUMN_AREA_NAME + "=?", new String[]{area_name}, null);
		      
	        if (cursor!=null && cursor.getCount() > 0) {
	            {
	            	cursor.moveToFirst();
	                area_id = cursor.getString(cursor.getColumnIndex(DBConstant.Area_Columnns.COLUMN_AREA_ID));
	            } 
	        }
			
	        cursor.close();
			List<String> list_batch = getBatch(area_id);
			arr_batch = list_batch.toArray(new String[list_batch.size()]);
			
//			switch(pos_course)
//			{
//			case 0:
////				adap_batch = ArrayAdapter.createFromResource(this, R.array.arr_level_batch, android.R.layout.simple_spinner_dropdown_item);
//				arr_batch = getResources().getStringArray(R.array.arr_level_batch);        
//				
//				break;
//			case 1:
////				adap_batch = ArrayAdapter.createFromResource(this, R.array.arr_level_batch2, android.R.layout.simple_spinner_dropdown_item);
//				arr_batch = getResources().getStringArray(R.array.arr_level_batch2);
//				break;
//			}
			adap_batch = new CustomArrayAdapter<CharSequence>(this, arr_batch);
			adap_batch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin_batch.setAdapter(adap_batch);
		}
		else if(which_spinner == R.id.spin_batch)
		{
			
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
//	D: CHECKING IF NETWORK AVAILABLE OR NOT [UTILITY]
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
// D: FETCH  SPINNER DATA FROM SERVICE. [SPINNER]	
	public void getSpinnerData()
	{
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		JSONObject jsonObject = RequestBuilder.getSpinnerData(currentSIMImsi);
		Log.e("COURSE------>>>>>>>>>>", jsonObject.toString());
		SpinnerDataTask spinnerDataTask = new SpinnerDataTask(this);
		spinnerDataTask.execute(new JSONObject[]{jsonObject});
	}

	
//	D: FETCH DATA FROM SERVICES AND INSERT IN TO TABLE. [SPINNER - AYSNCTASK]
	private class SpinnerDataTask extends AsyncTask<JSONObject, Void, Void>
	{
		Context context;
		public SpinnerDataTask(Context context) {
			// TODO Auto-generated constructor stub
        	this.context = context;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
		}
		@Override
		protected Void doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			JSONObject dataToSend = params[0];
			boolean status = false;
			String area_id =  null, area_name= null, batchname=null, a_id=null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.GET_DATA_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
						try {
		                    // Getting JSON Array node
		                    batcharray = jsonObject.getJSONArray("batches");
		 
		                    getContentResolver().delete(DBConstant.Area_Columnns.CONTENT_URI, null, null);
		                    getContentResolver().delete(DBConstant.Batch_Columns.CONTENT_URI, null, null);
		                    
		                    // looping through All Contacts
		                    for (int i = 0; i < batcharray.length(); i++) {
		                        JSONObject c = batcharray.getJSONObject(i);
		                         batchname="";
		                         area_id="";
		                         area_name="";
		                        a_id = c.getString("id");
		                        area_id = c.getString("area_id");
		                        batchname = c.getString("batchname");

		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Batch_Columns.COLUMN_AREA_ID, area_id);
		    					contentValues.put(DBConstant.Batch_Columns.COLUMN_BATCH_ID, a_id);
		    					contentValues.put(DBConstant.Batch_Columns.COLUMN_NAME, batchname);
		    					getContentResolver().insert(DBConstant.Batch_Columns.CONTENT_URI, contentValues);
		    					
//		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("Batch","New Batch Added");
		                        // adding contact to contact list
		                    }
		                } catch (JSONException e) {
		                    e.printStackTrace();
					}
						
						
						try {
		                    // Getting JSON Array node
		                    areaarray = jsonObject.getJSONArray("areas");
		 
		                    // looping through All Contacts
		                    for (int i = 0; i < areaarray.length(); i++) {
		                        JSONObject c = areaarray.getJSONObject(i);
		                         area_id="";
		                         area_name="";
		                        area_id = c.getString("id");
		                        area_name = c.getString("areaname");

		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Area_Columnns.COLUMN_AREA_ID, area_id);
		    					contentValues.put(DBConstant.Area_Columnns.COLUMN_AREA_NAME, area_name);
		    					getContentResolver().insert(DBConstant.Area_Columnns.CONTENT_URI, contentValues);
		    					
//		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("Area","New Area Added");
		                        // adding contact to contact list
		                    }
		                } catch (JSONException e) {
		                    e.printStackTrace();
					}

						
					}
				Log.e("ResponseTask",jsonStr);
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
			
			List<String> list_course = getAllAreas();
			String [] arr_course = list_course.toArray(new String[list_course.size()]);
			adap_course = new CustomArrayAdapter<CharSequence>(NewCourseActivity.this, arr_course);
			
			adap_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin_course.setAdapter(adap_course);
			
			   if (pDialog.isShowing())
			   {
	                pDialog.dismiss();
			   }
	         

		}
	}
}
