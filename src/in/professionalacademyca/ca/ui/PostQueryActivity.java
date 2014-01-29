package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PostQueryActivity extends FragmentActivity{

	Button post;
	EditText query;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_query);
		post= (Button)findViewById(R.id.post);
		query = (EditText)findViewById(R.id.query);
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
