package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.ui.utils.CustomToast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class NewCourseActivity  extends SherlockFragmentActivity implements OnItemSelectedListener{

	Spinner spin_course,spin_city,spin_area,spin_batch;
	CheckBox chk_default;
	TextView header,txtcourse,txtbatch,txtsetdefault;
	Button go;
	ActionBar actionBar;
	String fromWhere;
	
	ArrayAdapter adap_course,adap_city,adap_area,adap_batch;
	
	static Typeface stylefont;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_course_select);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Course");
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_set_as);
		try
		{
			fromWhere = getIntent().getExtras().getString("FROM");	
		}
		catch(Exception e)
		{
			Log.e("Traversal", e.toString());
		}
		
		
		spin_course = (Spinner)findViewById(R.id.spin_course);
		spin_batch = (Spinner)findViewById(R.id.spin_batch);
		
		chk_default = (CheckBox)findViewById(R.id.set_default);
		
		header = (TextView)findViewById(R.id.headercourse);
		txtbatch = (TextView)findViewById(R.id.batch);
		txtcourse = (TextView)findViewById(R.id.course);
		txtsetdefault = (TextView)findViewById(R.id.txtsetdefault);
		go =(Button)findViewById(R.id.go);
				
		chk_default.setTypeface(stylefont);
		header.setTypeface(stylefont);
		txtbatch.setTypeface(stylefont);
		txtcourse.setTypeface(stylefont);
		txtsetdefault.setTypeface(stylefont);
		go.setTypeface(stylefont);
		
//		adap_course = ArrayAdapter.createFromResource(this, R.array.arr_course_level, android.R.layout.simple_spinner_dropdown_item);
		
		String [] arr_course = getResources().getStringArray(R.array.arr_course_level);        
		adap_course = new CustomArrayAdapter<CharSequence>(this, arr_course);
		
		spin_course.setOnItemSelectedListener(this);
		adap_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_course.setAdapter(adap_course);
		
		spin_batch.setOnItemSelectedListener(this);
		
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
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void onSetDefault(View v)
	{
		if(chk_default.isChecked())
		{
			chk_default.setChecked(false);
		}
		else
		{
		chk_default.setChecked(true);
		}
	}
	
	public void onGo(View v)
	{
		if(validate())
		{
			if(chk_default.isChecked())
			{
				CA.getPreferences().setDefault(true);
				CA.getPreferences().setBatch(spin_batch.getSelectedItem().toString().trim());
				CA.getPreferences().setLevel(spin_course.getSelectedItem().toString().trim());
			}
			
			if(fromWhere.equalsIgnoreCase("Notification"))
			{
				setResult(RESULT_OK);
				finish();
				overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
			}
			else if(fromWhere.equalsIgnoreCase("Home"))
			{
			Intent timeTable = new Intent(this, TimeTableActivity.class);
			timeTable.putExtra("isBatch", spin_batch.getSelectedItem().toString().trim());
			startActivity(timeTable);
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
			else if(fromWhere.equalsIgnoreCase("Settings"))
			{
				Intent timeTable = new Intent(this, TimeTableActivity.class);
				timeTable.putExtra("isBatch", spin_batch.getSelectedItem().toString().trim());
				startActivity(timeTable);
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
		}
		else
		{
			CustomToast.showToastMessage(this,"Please select Batch");
		}
	}
	
	public boolean validate()
	{
		if(spin_batch.getSelectedItem().toString()!=null && spin_batch.getSelectedItem().toString().length() > 0)
		{
		return true;	
		}
		return false;
	}
	
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
	    	v.setPadding(15, 15, 0, 15);
	    	return v;
	    	}
	    
	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent)
	    {
	        View view = super.getView(position, convertView, parent);

	            TextView text = (TextView)view.findViewById(android.R.id.text1);
	            text.setTypeface(stylefont);
	            text.setPadding(15, 15, 0, 15);
	        return view;
	    }
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int pos_course;
		int which_spinner;
		String [] arr_batch = null;
		
		pos_course = spin_course.getSelectedItemPosition();
		which_spinner = parent.getId();
		if(which_spinner == R.id.spin_course)
		{
			switch(pos_course)
			{
			case 0:
//				adap_batch = ArrayAdapter.createFromResource(this, R.array.arr_level_batch, android.R.layout.simple_spinner_dropdown_item);
				arr_batch = getResources().getStringArray(R.array.arr_level_batch);        
				
				break;
			case 1:
//				adap_batch = ArrayAdapter.createFromResource(this, R.array.arr_level_batch2, android.R.layout.simple_spinner_dropdown_item);
				arr_batch = getResources().getStringArray(R.array.arr_level_batch2);
				break;
			}
			adap_batch = new CustomArrayAdapter<CharSequence>(this, arr_batch);
			adap_batch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin_batch.setAdapter(adap_batch);
		}
		else if(which_spinner == R.id.spin_batch)
		{
			
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
