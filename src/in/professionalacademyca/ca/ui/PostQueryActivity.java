/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- POST QUERY SCREEN
 * DESCRIPTION 		:- SEND QUERY TO SERVER AND SHOW ANSWERED QUERY FROM SERVER.
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
import in.professionalacademyca.ca.dto.AnswerDTO;
import in.professionalacademyca.ca.dto.QueryDTO;
import in.professionalacademyca.ca.dto.UploadDataResponseDTO;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ResponseParser;
import in.professionalacademyca.ca.service.ServiceDelegate;
import in.professionalacademyca.ca.sql.CustomSqlCursorAdapter;
import in.professionalacademyca.ca.sql.DBConstant;
import in.professionalacademyca.ca.ui.utils.QustomQueryDialogBuilder;
import in.professionalacademyca.ca.ui.utils.SwipeDismissListViewTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("SimpleDateFormat")
public class PostQueryActivity extends SherlockFragmentActivity{

	Button post;
	EditText query;
	ProgressBar progress;
//	Spinner spin_subject;
	TextView subject;
	
	ActionBar actionBar;
	static Typeface stylefont;

	ListView listQuery;
	CursorAdapter adapterQuery;
	ArrayAdapter adap_subject;
	
	ArrayList<QueryDTO> queryDTOs;
	ArrayList<AnswerDTO> unAnsQueryDTOs;
	
	String dialogQuery=null,dialogResponse=null,dialogQDate=null,dialogRDate=null;
	
	JSONArray query_answer = null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_query);
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Post Query");
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_help);
		
		post= (Button)findViewById(R.id.post);
//		spin_subject = (Spinner)findViewById(R.id.spin_subject);
		subject = (TextView)findViewById(R.id.txtsubject);
		query = (EditText)findViewById(R.id.query);
		listQuery = (ListView)findViewById(R.id.list);
		progress = (ProgressBar)findViewById(R.id.progress);
		
		post.setTypeface(stylefont);
		query.setTypeface(stylefont);
		subject.setTypeface(stylefont);
//		spin_subject.setTypeface(stylefont);
		
//		String [] spin_arry = getResources().getStringArray(R.array.arr_subject);        
//		adap_subject = new CustomArrayAdapter<CharSequence>(this, spin_arry);


		
//		adap_subject = ArrayAdapter.createFromResource(this, R.array.arr_subject, android.R.layout.simple_spinner_dropdown_item);
//		adap_subject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spin_subject.setAdapter(adap_subject);
		
//		listQuery.setSelector(R.drawable.listselector);  
//		new SelectDataTask().execute(DBConstant.Query_Columns.CONTENT_URI);
		loadUnQueryData();
		uploadUnQueryData();
		
		listQuery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String query_id=view.getTag().toString();
				Log.e("ID", String.valueOf(position) + ":"+ query_id);
				showDialogBox(query_id);
			}
		});
		
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                		listQuery,
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
                                	getContentResolver().delete(DBConstant.Query_Columns.CONTENT_URI, "_id=?", new String[] { String.valueOf(_id) });
                                	Toast.makeText(PostQueryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
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
		listQuery.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
		listQuery.setOnScrollListener(touchListener.makeScrollListener());
		}
		
	}
// D: CUSTOM ARRAY ADAPTER FOR SUBJECT WHILE POSTING QUERY [ARRAYADAPTER CUSTOM FONT SUBJECT]
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
	    	v.setPadding(10, 10, 0, 10);
	    	return v;
	    	}
	    
	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent)
	    {
	        View view = super.getView(position, convertView, parent);

	            TextView text = (TextView)view.findViewById(android.R.id.text1);
	            text.setTypeface(stylefont);
	            text.setPadding(10, 10, 0, 10);
	        return view;
	    }
	}
//	D: SHOW DIALOG ON CLICK OF QUERY FROM THE POST QUERY SCREEN [DIALOG QUERY CLICK LISTVIEW]
	@SuppressWarnings("deprecation")
	public void showDialogBox(String query_id)
	{
		dialogRDate="";
		dialogResponse="";
		dialogQDate="";
		dialogQuery="";
//		final AlertDialog alertDialog = new AlertDialog.Builder(PostQueryActivity.this).create();
//		alertDialog.setTitle("Query");
		// Setting Dialog Message
		Cursor c = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, DBConstant.Query_Columns.COLUMN_ID +"=?", new String[]{query_id}, null);
		// Setting Dialog Title
		if(c!=null && c.getCount()>0)
		{
			c.moveToFirst();
//			dialogQDate = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY_DATE));
			dialogQuery= c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY));
			dialogResponse = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE)).equalsIgnoreCase("0")?
					"Not yet answered": c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE))
					;
			
//			dialogRDate = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE)).equalsIgnoreCase("0")?
//					"": c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE))
//					;
		}
		
		
		final String HALLOWEEN_RED = "#B40404";
		QustomQueryDialogBuilder qustomDialogBuilder = new QustomQueryDialogBuilder(PostQueryActivity.this).
				setTitle("Query").
				setTitleColor(HALLOWEEN_RED).
				setDividerColor(HALLOWEEN_RED).
				setMessage("Q. "+dialogQuery).
				setAnswer("A. "+dialogResponse).
				setFontTitle(stylefont).
				setFontAnswer(stylefont).
				setFontMessage(stylefont);
		qustomDialogBuilder.show();
//	
//		
//		
//		String styledText = "<body><b><div style=\"color:#A7CA01;\">Q."+dialogQuery+"</div></b></br>"
//				+ "</br><div style=\"color:#EFEFEF;\">A. "+dialogResponse+" </div></body>";
//		
//		
////		alertDialog.setMessage(dialogQuery + "" +dialogQDate + "" +dialogResponse + "" +dialogRDate);
//		alertDialog.setMessage(Html.fromHtml(styledText));
//		alertDialog.setButton("CLOSE", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				alertDialog.dismiss();
//			}
//		});
//		alertDialog.show();
	}
//	D: APPLY FONT ON ACTIONBAR [FONT ACTIONBAR]
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
//	D: APPLY ANIMATION ON BACK PRESSED [ANIMATION BACK]
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
//	D: ON OPTION SELECTED [OPTION HOME]
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;
        case R.id.notification: 
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
//	D: POST QUERY TO SERVER [BUTTON CLICK EVENT]
	public void onPost(View v)
	{
      if(validate())
      {
    	  Log.e("Saving", "Saving");
          
          if (isNetworkAvailable()) {
//        	  saveQuery(query.getText().toString().trim(),spin_subject.getText().toString().trim());
//        	  saveQuery(query.getText().toString().trim(),spin_subject.getSelectedItem().toString());
//        	  
//              loadQueryData();
//              loadUnQueryData();
//              uploadQueryData();
//              uploadUnQueryData();
              showInputDialog();
  		}
  		else
  		{
  			Toast.makeText(PostQueryActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
  		}
      }
      else
      {
    	  if(TextUtils.isEmpty(query.getText().toString()))
    	  query.setError("Please enter query");
    	  
//    	  if(TextUtils.isEmpty(spin_subject.getText().toString()))
//    	  spin_subject.setError("Please enter subject");

      }
	}
	
	public void postQueryNow()
	{
//		  saveQuery(query.getText().toString().trim(),spin_subject.getSelectedItem().toString());
		  saveQuery(query.getText().toString().trim(),"General");
    	  
          loadQueryData();
          loadUnQueryData();
          uploadQueryData();
          uploadUnQueryData();
	}
	public void showInputDialog()
	{
		final Dialog dialog = new Dialog(PostQueryActivity.this);
		try {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		} catch (Exception e) {
			Log.e("Dialog", e.toString());
		}
		dialog.setContentView(R.layout.qustom_dialog_post_query_layout);
		
		try {
//			int y = Resources.getSystem().getIdentifier("title","id", "android");
//			TextView mTitle = (TextView)findViewById(y);
//			mTitle.setTypeface(stylefont);
//			mTitle.setTextColor(PostQueryActivity.this.getResources().getColor(R.color.red_halloween));
//			
//			int x = Resources.getSystem().getIdentifier("titleDivider","id", "android");
//			View titleDivider = findViewById(x);
//			titleDivider.setBackgroundColor(PostQueryActivity.this.getResources().getColor(R.color.red_halloween));
		} catch (Exception e) {
			Log.e("Dialog", e.toString());
		}
		
		dialog.setTitle(getString(R.string.captcha));
		
		TextView mNumber1 = (TextView)dialog.findViewById(R.id.number1);
		TextView mNumber2 = (TextView)dialog.findViewById(R.id.number2);
		
		TextView mAlertTitle = (TextView)dialog.findViewById(R.id.alertTitle);
		
		final EditText mAnswer = (EditText)dialog.findViewById(R.id.answer);
		
		Button mBtnOk = (Button)dialog.findViewById(R.id.btn_ok);
		Button mBtnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		
		Random r = new Random();
		int Low = 1;
		int High = 10;
		final int nNumber1 = r.nextInt(High-Low) + Low;
		final int nNumber2 = r.nextInt(High-Low) + Low;
		
		mNumber1.setText(String.valueOf(nNumber1));
		mNumber2.setText(String.valueOf(nNumber2));
		mAlertTitle.setText(getString(R.string.captcha));
		
		mNumber1.setTypeface(stylefont);
		mNumber2.setTypeface(stylefont);
		mAlertTitle.setTypeface(stylefont);
		
		mBtnOk.setTypeface(stylefont);
		mBtnCancel.setTypeface(stylefont);
		
		mBtnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mAnswer.getText().toString()))
				{
					if(nNumber1+nNumber2 == Integer.parseInt(mAnswer.getText().toString()))
					{
						postQueryNow();
						dialog.dismiss();
					}
					else
					{
						Toast.makeText(PostQueryActivity.this, "Please enter correct value", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					mAnswer.setError("Please enter input");
				}
			}
		});
		
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
//	D: CHECK NETWORK AVAILABILITY [UTILITY NETWORK INTERNET]
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
//	D: POST DATA OF QUERY DATA BUILDER IN TO JSON BUILDER [JSON QUERY POST BUILDER UPLOAD]
	public void uploadQueryData()
	{
		JSONObject tables = new JSONObject();
		try
		{
			if(queryDTOs != null && queryDTOs.size() > 0)
			{
				JSONArray exp = RequestBuilder.getQueryDetails(queryDTOs);
				tables.put("tables", exp);
			}
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
		UploadTask uploadTask = new UploadTask();
		uploadTask.execute(new JSONObject[]{jsonObject});
	}
//	D: POST DATA OF UNQUERY DATA BUILDER IN TO JSON BUILDER [JSON UNQUERY QUERY POST BUILDER UPLOAD]	
	public void uploadUnQueryData()
	{
		JSONObject tables = new JSONObject();
		try
		{
			if(unAnsQueryDTOs != null && unAnsQueryDTOs.size() > 0)
			{
				JSONArray exp = RequestBuilder.getUnAnsQueryDetails(unAnsQueryDTOs);
				tables.put("tables", exp);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("jSON - Query", e.toString());
		}		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		JSONObject jsonObject = RequestBuilder.getQueryData(currentSIMImsi, tables);
		Log.e("UNQUERY--------------->>>>>>>>>>", jsonObject.toString());
		Log.e("UNQUERY--------------->>>>>>>>>>", tables.toString());
		UploadUnAnswerTask uploadTask = new UploadUnAnswerTask();
		uploadTask.execute(new JSONObject[]{jsonObject});
	}
	
// D: CLEARED QUERY ON POSTING OF QUERY [CLEAR POST QUERY]
	public void clearQuery()
	{
		query.setText("");
//		spin_subject.setSelection(0);
	}
//	D: SAVE QUERY DATA IN DATABASE USED TO FETCH THE ANSWER OF IT LATER ON [DATABASE QUERY SAVE]
	public void saveQuery(String str,String sub)
	{
		Bundle b = new Bundle();
		b.putString("message", "Saving");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		if (date.contains("/")) {
			date = date.replace("/", "-");
		}
		
		ContentValues dataValues = new ContentValues();
		dataValues.put(DBConstant.Query_Columns.COLUMN_QUERY, str);
		dataValues.put(DBConstant.Query_Columns.COLUMN_QUERY_DATE, date);
		dataValues.put(DBConstant.Query_Columns.COLUMN_SYNC_STATUS, "0");
		dataValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE, "0");
		dataValues.put(DBConstant.Query_Columns.COLUMN_BATCH, "batch");
		dataValues.put(DBConstant.Query_Columns.COLUMN_SUBJECT, sub);
		dataValues.put(DBConstant.Query_Columns.COLUMN_LEVEL, "level");
//		dataValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE, "0");
		getContentResolver().insert(DBConstant.Query_Columns.CONTENT_URI,dataValues);		
		clearQuery();
	}
//	D: VALIDATE POST QUERY [VALIDATE QUERY DATA]
	public boolean validate()
	{
//		if(!TextUtils.isEmpty(query.getText().toString()) && !TextUtils.isEmpty(spin_subject.getText().toString()))
		if(!TextUtils.isEmpty(query.getText().toString()))
		{
			return true;
		}
		return false;
	}
//	D: BUNDLE QUERY DATA SENT TO SERVICES [CLASSES ARRAYLIST QUERY BUNDEL JSON SEND API SERVICES]
	public void loadQueryData()
	{
		String _id;
		String query;
		String date;
		String subject;
		String level;
		String batch;

		Cursor c = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, DBConstant.Query_Columns.COLUMN_SYNC_STATUS +"=?", new String[]{"0"}, null);
		if( c != null && c.getCount() > 0)
		{
			queryDTOs = new ArrayList<QueryDTO>();
			while(c.moveToNext())
			{
				_id = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_ID));
				query = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY));
				date = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY_DATE));
				level = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_LEVEL));
				batch = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_BATCH));
				subject = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_SUBJECT));
				queryDTOs.add(new QueryDTO(_id, query, null, date, null, level, batch, subject));
			}
			c.close();

		}
	}
//	D: BUNDLE UN- ANSWERED QUERY DATA SENT TO SERVICES [CLASSES ARRAYLIST QUERY BUNDEL JSON SEND API SERVICES]	
	public void loadUnQueryData()
	{
		String _id;
		String query;
		String date;

		Cursor c = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, DBConstant.Query_Columns.COLUMN_RESPONSE +"=?", new String[]{"0"}, null);
		if( c != null && c.getCount() > 0)
		{
			unAnsQueryDTOs = new ArrayList<AnswerDTO>();
			while(c.moveToNext())
			{
				_id = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_ID));
				query = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY));
				date = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY_DATE));
				unAnsQueryDTOs.add(new AnswerDTO(_id, query, null, date,null));
			}
			c.close();
		}
	}
//	D: UPLOAD BUNDLE DATA TO SERVICES [QUERY JSON BUNDLE SEND SERVICES API]
	private class UploadTask extends AsyncTask<JSONObject, Void, Void>
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
			try {
				String str = ServiceDelegate.postData(AppConstants.URLS.BASE_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				UploadDataResponseDTO responseDTO = ResponseParser.getUploadDataResponse(str);
				
				if(responseDTO != null)
				{
					// update services
					
					String _data = responseDTO.getQuery();
					if(_data!= null && !_data.equals("[]") && !_data.equals(""))
					{
						_data = _data.substring(1, _data.length() - 1);
						{
							ContentValues contentValues = new ContentValues();
							contentValues.put(DBConstant.Query_Columns.COLUMN_SYNC_STATUS, 1);
							String[] data =  _data.split(",");
							if(data.length > 0)
							{
								for(int i = 0; i < data.length; i++)
								{
									String s = data[i];
									if(data[i].startsWith("\"") && data[i].endsWith("\""))
									{
										s = data[i].substring(1, data[i].length() - 1);
									}
									try
									{
										int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI, contentValues, DBConstant.Query_Columns.COLUMN_ID +"=?", new String[]{s});
										Log.e("ROWS UPDATED : data : ", col +"");
									}
									catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}
							}
							
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
			new SelectDataTask().execute(DBConstant.Query_Columns.CONTENT_URI);
			progress.setVisibility(View.GONE);
		}
	}
//	D: UPLOAD BUNDLE DATA OF UN ANSWERED QUERY DATA TO SERVICES [API SERVICES JSON SEND BUNDLE]
	private class UploadUnAnswerTask extends AsyncTask<JSONObject, Void, Void>
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
					if(status && currentSIMImsi.equalsIgnoreCase(device_id))
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
			new SelectDataTask().execute(DBConstant.Query_Columns.CONTENT_URI);
			progress.setVisibility(View.GONE);
		}
	}
//	D: FETCHES DATA FROM DATABASE AND SUPPLIE TO ADAPTER [ADAPTER DATABASE]
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
			int[] listFields = new int[] { R.id.question , R.id.answer,R.id.questiondate,R.id.answerdate};
			String[] dbColumns = new String[] { DBConstant.Query_Columns.COLUMN_ID, DBConstant.Query_Columns.COLUMN_QUERY , DBConstant.Query_Columns.COLUMN_RESPONSE,DBConstant.Query_Columns.COLUMN_QUERY_DATE,DBConstant.Query_Columns.COLUMN_RESPONSE_DATE};

			PostQueryActivity.this.adapterQuery = new CustomSqlCursorAdapter(PostQueryActivity.this, R.layout.post_item,result, dbColumns, listFields,currentUri);
			PostQueryActivity.this.listQuery.setAdapter(PostQueryActivity.this.adapterQuery);
			progress.setVisibility(View.GONE);
		}

		@Override
		protected Cursor doInBackground(Uri... arg0) {
			// TODO Auto-generated method stub
			try {
				currentUri = arg0[0];
				return PostQueryActivity.this.getContentResolver().query(currentUri, null, null, null, DBConstant.Query_Columns.COLUMN_ID+" DESC");
			} catch (SQLException sqle) {
				throw sqle;
			}
		}
	}
	
}
