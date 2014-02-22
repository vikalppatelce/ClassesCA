package in.professionalacademyca.ca.service;

import java.util.ArrayList;

import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.dto.AnswerDTO;
import in.professionalacademyca.ca.dto.QueryDTO;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestBuilder {

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
