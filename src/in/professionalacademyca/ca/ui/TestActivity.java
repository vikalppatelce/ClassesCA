package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TestActivity extends SherlockFragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
	}

}
