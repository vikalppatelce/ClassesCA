/* HISTORY
 * CATEGORY 		:- PREFERENCES
 * DEVELOPER		:- VIKALP PATEL
 * AIM      		:- EXPENSES
 * DESCRIPTION 		:- SETTINGS OF APPLICATION
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    10/02/2014       				FIXED PREFERENCE ON CHANGE LISTENER
 * 10002       VIKALP PATEL    13/02/2014      PREFERENCES      INITIALIZING SUMMARY OF PREFERENCES
 * --------------------------------------------------------------------------------------------------------------------
 */

package in.professionalacademyca.ca.ui;


import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.utils.AboutDialog;
import in.professionalacademyca.ca.utils.ChangeLogDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("SdCardPath")
public class PrefsActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener 
{
	ActionBar actionBar;
	Typeface stylefont;
	final static int SETTINGS = 1008;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		PreferenceManager prefMgr = getPreferenceManager();
		addPreferencesFromResource(R.xml.settings);
		
		stylefont = Typeface.createFromAsset(getAssets(), AppConstants.fontStyle);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Set Up");
		
		fontActionBar(actionBar.getTitle().toString());
		actionBar.setIcon(android.R.drawable.ic_menu_manage);
		
		//SA 10002
//		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
//            initSummary(getPreferenceScreen().getPreference(i));
//        }//EA 10002
		
		Preference batch = prefMgr.findPreference("prefBatch");
		if(batch!=null)
		{
			batch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					// TODO Auto-generated method stub
				//	showDialog(DEV);
					Intent i = new Intent(PrefsActivity.this, NewCourseActivity.class);
					i.putExtra("FROM", "Settings");
					startActivityForResult(i, SETTINGS);
					finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
					return false;
				}
			});
		}
		
		
		Preference release = prefMgr.findPreference("prefRelease");
		if(release!=null)
		{
			release.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					// TODO Auto-generated method stub
					ChangeLogDialog changeLogDialog = new ChangeLogDialog(PrefsActivity.this);
					changeLogDialog.show();
					return false;
				}
			});
		}
		
		Preference dev = prefMgr.findPreference("prefDev");
		if(dev!=null)
		{
			dev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					// TODO Auto-generated method stub
				//	showDialog(DEV);
					copyDatabase(); 
					return false;
				}
			});
		}
		
		Preference about = prefMgr.findPreference("prefAbout");
		if(about!=null)
		{
			about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					// TODO Auto-generated method stub
				//	showDialog(DEV);
					AboutDialog aboutDialog = new AboutDialog(PrefsActivity.this);
					aboutDialog.show();
					return false;
				}
			});
		}
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
	
	// SA 10001
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
    }
	// EA 10001
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
//		updatePrefSummary(findPreference(key));
		updatePreference(key);
	}
	@SuppressWarnings({ "resource", "unused" })
	public void copyDatabase()
	{
		try {
            File sd = Environment.getExternalStorageDirectory();
            File d1ata = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/CaDB";
                String backupDBPath = "CaDB_Dev.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(PrefsActivity.this, "Database Transfered!", Toast.LENGTH_SHORT).show();
                    Log.i("Developer", "Database Transfered");
                }
            }
        } catch (Exception e) {
        	Toast.makeText(PrefsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        	Log.e("Developer", e.toString());
        }

	}
	//SA 10002
	@SuppressWarnings("unused")
	private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                initSummary(pCat.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }
	
	private void updatePrefSummary(Preference p) {
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if(editTextPref.getKey().equalsIgnoreCase("prefPass"))
            {
            	p.setSummary("Will Keep it safe");
            }
            else
            {
            	Spannable summary = new SpannableString (editTextPref.getText().toString());
				summary.setSpan( new ForegroundColorSpan( Color.RED ), 0, summary.length(), 0 );
				p.setSummary(isEmailValid(editTextPref.getText().toString())? editTextPref.getText() : summary);
            }
        }
	}
	//EA 10002
	public static boolean isEmailValid(String nComingEmail) 
	{
	    boolean isValid = false;

	    String mExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence mInputEmail = nComingEmail;

	    Pattern mPattern = Pattern.compile(mExpression, Pattern.CASE_INSENSITIVE);
	    Matcher mMatcher = mPattern.matcher(mInputEmail);
	    if (mMatcher.matches()) 
	    {
	        isValid = true;
	    }
	    return isValid;
	}
	@SuppressWarnings("deprecation")
	private void updatePreference(String key) {
		if (key.equals("prefUser")) {
			Preference preference = findPreference(key);
			if (preference instanceof EditTextPreference) {
				EditTextPreference userPreference = (EditTextPreference) preference;
				if (userPreference.getEditText().length() > 0 && isEmailValid(userPreference.getEditText().getText().toString())) {
					userPreference.setSummary(userPreference.getEditText().getText().toString());
				} 
				else
				{
					//SA 10002
					Spannable summary = new SpannableString (userPreference.getEditText().getText().toString());
					summary.setSpan( new ForegroundColorSpan( Color.RED ), 0, summary.length(), 0 );
					
					userPreference.setSummary(summary);//EA 10002
					Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
				}
			}
		}
		
		if (key.equals("prefPass")) {
			Preference preference = findPreference(key);
			if (preference instanceof EditTextPreference) {
				EditTextPreference passPreference = (EditTextPreference) preference;
				if (passPreference.getEditText().length() > 0) {
					passPreference.setSummary("Will keep it safe");
				} 
			}
		}
		
		if (key.equals("prefSent")) {
			Preference preference = findPreference(key);
			if (preference instanceof EditTextPreference) {
				EditTextPreference sentPreference = (EditTextPreference) preference;
				if (sentPreference.getEditText().length() > 0 && isEmailValid(sentPreference.getEditText().getText().toString())) {
					sentPreference.setSummary("Mail will sent to:"+sentPreference.getEditText().getText().toString());
				} 
				else
				{
					//SA 10002
					Spannable summary = new SpannableString (sentPreference.getEditText().getText().toString());
					summary.setSpan( new ForegroundColorSpan( Color.RED ), 0, summary.length(), 0 );
					
					sentPreference.setSummary(summary);//EA 10002
					Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
