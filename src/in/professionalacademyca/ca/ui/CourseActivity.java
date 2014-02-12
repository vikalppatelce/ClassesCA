package in.professionalacademyca.ca.ui;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.ui.utils.CustomToast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CourseActivity  extends SherlockFragmentActivity implements OnItemSelectedListener{

	Spinner spin_course,spin_city,spin_area,spin_batch;
	CheckBox chk_default;
	ArrayAdapter adap_course,adap_city,adap_area,adap_batch;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_select);
		spin_course = (Spinner)findViewById(R.id.spin_course);
		spin_city = (Spinner)findViewById(R.id.spin_city);
		spin_area = (Spinner)findViewById(R.id.spin_area);
		spin_batch = (Spinner)findViewById(R.id.spin_batch);
		
		chk_default = (CheckBox)findViewById(R.id.set_default);
		
		adap_course = ArrayAdapter.createFromResource(this, R.array.arr_course_level, android.R.layout.simple_spinner_dropdown_item);
		
		spin_course.setOnItemSelectedListener(this);
		adap_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_course.setAdapter(adap_course);
		
		spin_city.setOnItemSelectedListener(this);
		spin_area.setOnItemSelectedListener(this);
		
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
	
	public void onGo(View v)
	{
		if(validate())
		{
			if(chk_default.isChecked())
			{
				CA.getPreferences().setDefault(true);
				CA.getPreferences().setArea(spin_area.getSelectedItem().toString().trim());
			}
			Intent timeTable = new Intent(this, TimeTableActivity.class);
			timeTable.putExtra("isArea", spin_area.getSelectedItem().toString().trim());
			startActivity(timeTable);
		}
		else
		{
			CustomToast.showToastMessage(this,"Please select area");
		}
	}
	
	public boolean validate()
	{
		if(spin_area.getSelectedItem().toString()!=null && spin_area.getSelectedItem().toString().length() > 0)
		{
		return true;	
		}
		return false;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int pos_course,pos_city ,pos_area ,pos_batch;
		int which_spinner;
		
		pos_course = spin_course.getSelectedItemPosition();
		which_spinner = parent.getId();
		if(which_spinner == R.id.spin_course)
		{
			switch(pos_course)
			{
			case 0:
				adap_city = ArrayAdapter.createFromResource(this, R.array.arr_level_city, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 1:
				adap_city = ArrayAdapter.createFromResource(this, R.array.arr_level_city, android.R.layout.simple_spinner_dropdown_item);
				break;
			}
			adap_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin_city.setAdapter(adap_city);
			
			
		}
		else if(which_spinner == R.id.spin_city)
		{
			pos_city = spin_city.getSelectedItemPosition();
			switch(pos_city)
			{
			case 0:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_ahmd_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 1:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_banglore_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 2:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_chennai_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 3:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_delhi_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 4:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_mumbai_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			case 5:
				adap_area = ArrayAdapter.createFromResource(this, R.array.arr_kolkata_area, android.R.layout.simple_spinner_dropdown_item);
				break;
			}
			adap_area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin_area.setAdapter(adap_area);
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
