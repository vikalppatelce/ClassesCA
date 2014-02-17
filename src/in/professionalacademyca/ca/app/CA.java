package in.professionalacademyca.ca.app;

import in.professionalacademyca.ca.dto.Preferences;
import in.professionalacademyca.ca.service.DataController;
import in.professionalacademyca.ca.stacktrace.ExceptionHandler;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
