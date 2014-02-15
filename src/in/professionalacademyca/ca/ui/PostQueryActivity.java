package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostQueryActivity extends SherlockFragmentActivity{

	Button post;
	EditText query;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_query);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		post= (Button)findViewById(R.id.post);
		query = (EditText)findViewById(R.id.query);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	       inflater.inflate(R.menu.main, menu);
		 return true;
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
        case R.id.notification: 
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void onPost(View v)
	{
      if(validate())
      {
    	  
      }
      else
      {
    	  query.setError("Please enter query");
      }
	}
	
	public boolean validate()
	{
		if(query.getText()!= null && query.getText().toString().length() > 0)
		{
			return true;
		}
		return false;
	}
}
