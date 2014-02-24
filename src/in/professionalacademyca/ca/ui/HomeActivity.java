package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.service.RequestBuilder;
import in.professionalacademyca.ca.service.ServiceDelegate;
import in.professionalacademyca.ca.ui.utils.TestFragmentAdapter;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class HomeActivity extends SherlockFragmentActivity {


	public TestFragmentAdapter mAdapter;
    public ViewPager mPager;
    public PageIndicator mIndicator;
	
    Typeface stylefont;
    ActionBar actionBar;
    
	TextView ticker;
	Button timetable,postquery;
	int currentPage;
	boolean onlyOnce = false;
	String tickerText=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		fontActionBar();
		
		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
		ticker = (TextView)findViewById(R.id.ticker);
		timetable = (Button)findViewById(R.id.btntimetable);
		postquery = (Button)findViewById(R.id.btnpostquery);
		
		ticker.setTypeface(stylefont);
		timetable.setTypeface(stylefont);
		postquery.setTypeface(stylefont);
		
		fetchTickerData();
		
		final Handler handler = new Handler();

		final Runnable Update= new Runnable() {
            @Override
			public void run() {
            	currentPage = mPager.getCurrentItem();
                if (currentPage == 3)
                {
                	onlyOnce = true;
                	currentPage = 0;
                	mPager.setCurrentItem(currentPage);
                }
                else
                {
                mPager.setCurrentItem(currentPage+1);
                }
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
                if(onlyOnce)
                	handler.removeCallbacks(Update);
            }
        }, 100, 2000);
	}
	
	public void fontActionBar()
	{
		try {
			int titleId = getResources().getIdentifier("action_bar_title",
					"id", "android");
			TextView yourTextView = (TextView) findViewById(titleId);
			yourTextView.setTypeface(stylefont);
		} catch (Exception e) {
			Log.e("ActionBar Style", e.toString());
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	       inflater.inflate(R.menu.main, menu);
		 return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.notification: 
        	return true;
        case R.id.settings:
        	Intent i = new Intent(HomeActivity.this, PrefsActivity.class);
        	startActivity(i);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }	public void onTimeTable(View v)
	{
//		Intent i = new Intent(this, CourseActivity.class);
    	Intent i = new Intent(this, NewCourseActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	public void onPostQuery(View v)
	{
		Intent i = new Intent(this, PostQueryActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
