/* HISTORY
 * CATEGORY			 :- PREFERENCES
 * DEVELOPER		 :- VIKALP PATEL
 * AIM      		 :- SAVING SETTINGS | SETUP
 * DESCRITION 		 :-  SAVING SHARED PREFERENCES FOR APPLICATION
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    06/01/2014       				GETTER - SETTER FOR FULLSCREEN MODE OF APPLICATION
 * 10002       VIKALP PATEL    08/01/2014       				GETTER - SETTER FOR DEVICE ID & IMEI NO
 * --------------------------------------------------------------------------------------------------------------------
 */

package in.professionalacademyca.ca.dto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

	SharedPreferences sharedPreferences;
	Editor editor;
	
	public Preferences(Context context) {
		// TODO Auto-generated constructor stub
		sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
	}
	
	public void setUserLoginDTO(UserLoginDTO userLoginDTO)
	{
		editor = sharedPreferences.edit();
		editor.putString("userId", userLoginDTO.getSign_id());
		editor.putString("name", userLoginDTO.getUserName());
		editor.commit();
	}
	
	public UserLoginDTO getUserLoginDTO()
	{
		UserLoginDTO userLoginDTO = new UserLoginDTO();
		userLoginDTO.setSign_id(sharedPreferences.getString("userId", null));
		userLoginDTO.setUserName(sharedPreferences.getString("name", null));
		return userLoginDTO;
	}
	
	public boolean getDefault()
	{
		boolean flag = sharedPreferences.getBoolean("isDefault", false);
		return flag;
	}
	
	public void setDefault(boolean flag)
	{
		editor = sharedPreferences.edit();
		editor.putBoolean("isDefault", flag);
		editor.commit();
	}
	
	public String getLevel()
	{
		String area = sharedPreferences.getString("isLevel", null);
		return area;
	}
	
	public void setLevel(String area)
	{
		editor = sharedPreferences.edit();
		editor.putString("isLevel", area);
		editor.commit();
	}
	
	public String getBatch()
	{
		String area = sharedPreferences.getString("isBatch", null);
		return area;
	}
	
	public void setBatch(String area)
	{
		editor = sharedPreferences.edit();
		editor.putString("isBatch", area);
		editor.commit();
	}
	
	public String getChangeBatch()
	{
		String area = sharedPreferences.getString("isChangeBatch", null);
		return area;
	}
	
	public void setChangeBatch(String area)
	{
		editor = sharedPreferences.edit();
		editor.putString("isChangeBatch", area);
		editor.commit();
	}
	
	public void setIsLOVInserted(boolean bool)
	{
		editor = sharedPreferences.edit();
		editor.putBoolean("isLOVInserted", bool);
		editor.commit();
	}
	
	public boolean getIsLOVInserted()
	{
		boolean bool = sharedPreferences.getBoolean("isLOVInserted", false);
		return bool;
	}
	public void setDeviceId(String id)
	{
		editor = sharedPreferences.edit();
		editor.putString("deviceId", id);
		editor.commit();
	}
	public String getDeviceId()
	{
		String deviceId = sharedPreferences.getString("deviceId", "Device Id Not Found");
				return deviceId;
	}
	public void setIMEINo(String id)
	{
		editor = sharedPreferences.edit();
		editor.putString("iMEINo", id);
		editor.commit();
	}
	public String getIMEINo()
	{
		String phoneId = sharedPreferences.getString("iMEINo", "IMEI Not Found");
		return phoneId;
	}
	
	public void setFirstTime(boolean b)
	{
		editor = sharedPreferences.edit();
		editor.putBoolean("isFirstTime", b);
		editor.commit();
	}
	public boolean getFirstTime()
	{
		boolean phoneId = sharedPreferences.getBoolean("isFirstTime", false);
		return phoneId;
	}
}
