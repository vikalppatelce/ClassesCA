package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.ui.utils.TestFragmentAdapter;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

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
	
	TextView ticker;
	int currentPage;
	boolean onlyOnce = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		
		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
		//ticker = (TextView)findViewById(R.id.ticker);
		//ticker.setSelected(true);
		
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
		Intent i = new Intent(this, CourseActivity.class);
		startActivity(i);
	}
	
	public void onPostQuery(View v)
	{
		Intent i = new Intent(this, PostQueryActivity.class);
		startActivity(i);
	}
}
