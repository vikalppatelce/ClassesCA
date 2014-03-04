/* HISTORY
 * CATEGORY 		:- JSON HELPER | API HELPER | SERVICE HELPER
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- BUILD REQUEST DATA
 * DESCRIPTION 		:- BUILD REQUEST DATA SENT TO SERVICES [USE IT ON GATHERED SERVICES DATA]
 * SEARCH           :- D: ADAPTER BUTTON SPINNER ASYNCTASK JSON REQUEST BUILDER LOGIN TIMETABLE NOTIFICATION
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    04/03/2014       				
 * --------------------------------------------------------------------------------------------------------------------
 */

package in.professionalacademyca.ca.service;

import java.util.ArrayList;

import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.dto.AnswerDTO;
import in.professionalacademyca.ca.dto.QueryDTO;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequestBuilder {
// D: REQUEST BUILDING FOR FETCHING UNANSWERED QUERY DATA [DEVICE ID & QUERY] [REQUEST BUILDER]
	public static JSONObject getQueryData(String imei, JSONObject tables)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
			stringBuffer.put("query", tables.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
	
	public static JSONObject getTimeTableData(String imei,String date,String area_id)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
			stringBuffer.put("act", "get_timetable");
			stringBuffer.put("batch_name", CA.getPreferences().getBatch());
			stringBuffer.put("batch_date", date);
			stringBuffer.put("area_id",area_id);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
	
	
//	D: REQUEST BUILDER FOR POSTING QUERY DATA TO SERVICES [REQUEST BUILDER]
	public static JSONObject getQueryNotificationData(String imei, long id)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
			stringBuffer.put("batch_name", CA.getPreferences().getBatch());
			stringBuffer.put("level_name", CA.getPreferences().getLevel());
			stringBuffer.put("date_time", id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
//	D: 	REQUEST BUILDER FOR SPINNER DATA [TIMETABLE BATCH AREA][REQUEST BUILDER].
	public static JSONObject getSpinnerData(String imei)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
			stringBuffer.put("act", "get_data");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
	
//	D: REQUEST BUILDER POST AT TIME OF FETCHING TICKER INFORMATION FROM SERVICES [REQUEST BUILDER ]
	public static JSONObject getTicker(String imei)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
//	D: REQUEST BUILDER  SENT TO SERVER ONCE GCM IS REGISTERED[GCM] [PUSH NOTIFICATION] [REQUEST BUILDER]
	public static JSONObject getPushNotificationData(String imei)
	{
		JSONObject stringBuffer = new JSONObject();
		
		//JSONObject ParentBuffer = new JSONObject();
		try
		{
			stringBuffer.put("device_id", imei);
			stringBuffer.put("registration_id", CA.getSharedPreferences().getString("registration_id", "Not yet Registered"));
			stringBuffer.put("app_version", CA.getSharedPreferences().getInt("appVersion", 0));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stringBuffer;//ParentBuffer;
	}
//	D: REQUEST BUILDER SENT AT TIME OF PUSHING QUERY ON SERVER  [REQUEST BUILDER QUERY]
	public static JSONArray getQueryDetails(ArrayList<QueryDTO> queryDTO)
	{
		JSONArray jsonArray = new JSONArray();
		if(queryDTO != null && queryDTO.size() > 0)
		{
			for(int i = 0 ; i < queryDTO.size();i++)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("question_id", queryDTO.get(i).getId());
					jsonObject.put("user_query", queryDTO.get(i).getQuery());
					jsonObject.put("query_date", queryDTO.get(i).getDate());
					jsonObject.put("level_name", queryDTO.get(i).getLevel());
					jsonObject.put("batch_name", queryDTO.get(i).getBatch());
					jsonObject.put("subject_name", queryDTO.get(i).getSubject());
					jsonArray.put(jsonObject);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return jsonArray;
	}
//	D: REQUEST BUILDER TO FETCH NOTIFICATION FROM SERVER [REQUEST BUILDER NOTIFICATION]
	public static JSONArray getNotificationData()
	{
		JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("batch_name", CA.getPreferences().getBatch());
					jsonObject.put("level_name", CA.getPreferences().getLevel());
					jsonArray.put(jsonObject);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return jsonArray;
		}

//	D: REQUEST BUILDER WITH UNANSWERED QUERY TO SERVER [REQUEST BUILDER QUERY ANSWERED]
	public static JSONArray getUnAnsQueryDetails(ArrayList<AnswerDTO> answerDTO)
	{
		JSONArray jsonArray = new JSONArray();
		if(answerDTO != null && answerDTO.size() > 0)
		{
			for(int i = 0 ; i < answerDTO.size();i++)
			{
				JSONObject jsonObject = new JSONObject();
				try
				{
					jsonObject.put("question_id", answerDTO.get(i).get_id());
//					jsonObject.put("user_query", answerDTO.get(i).getQuery());
//					jsonObject.put("query_date", answerDTO.get(i).getDate());
					jsonArray.put(jsonObject);
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return jsonArray;
	}
}
