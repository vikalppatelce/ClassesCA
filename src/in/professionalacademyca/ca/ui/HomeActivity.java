package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.ui.utils.TestFragmentAdapter;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
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
}
