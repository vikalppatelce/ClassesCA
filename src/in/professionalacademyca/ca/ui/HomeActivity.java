package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

public class HomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onTimeTable(View v)
	{
		Intent i = new Intent(this, CourseActivity.class);
		startActivity(i);
	}
	
	public void onPostQuery(View v)
	{
		
	}
}
