package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class TimeTableActivity extends SherlockFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_table);
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
    }
}
