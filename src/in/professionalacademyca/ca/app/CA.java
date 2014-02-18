package in.professionalacademyca.ca.app;

import in.professionalacademyca.ca.dto.Preferences;
import in.professionalacademyca.ca.service.DataController;
import in.professionalacademyca.ca.sql.DBConstant;
import in.professionalacademyca.ca.stacktrace.ExceptionHandler;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

public class CA extends Application {

	static CA ca;
	static DataController dataController;
	static SharedPreferences sharedPreferences;
	static Preferences preferences;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ca = this;
		dataController = new DataController();
		preferences = new Preferences(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		ExceptionHandler.register(ca);
		createDatabase();
	}
	
	public void createDatabase()
	{
		Cursor c = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, null, null, null);
		Log.i("Database", "Created");
		if( c!= null)
		{
			c.close();
			c = null;
		}
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	public static CA getApplication()
	{
		return ca;
	}

	public static DataController getDataController() {
		return dataController;
	}
	
	public static Preferences getPreferences() {
		return preferences;
	}
	
	public static SharedPreferences getSharedPreferences()
	{
		return sharedPreferences;
	}
}
