package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostQueryActivity extends SherlockFragmentActivity{

	Button post;
	EditText query;
	
	ActionBar actionBar;
	Typeface stylefont;
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
		query = (EditText)findViewById(R.id.query);
		
		post.setTypeface(stylefont);
		query.setTypeface(stylefont);
	}
	
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
