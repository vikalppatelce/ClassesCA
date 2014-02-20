package in.professionalacademyca.ca.service;

import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.dto.UploadDataResponseDTO;
import in.professionalacademyca.ca.dto.UserLoginDTO;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser {
	
	public static UserLoginDTO getLoginResponse(String params)
	{
		UserLoginDTO loginDTO = null;
		JSONObject object;
		String username = null;
		boolean status;
		String userid = null;
		try 
		{
			object = new JSONObject(new String(params));
			status = object.getBoolean(AppConstants.RESPONSES.LoginResponse.STATUS);
			
			JSONObject childObject = object.getJSONObject("data");
			if(childObject.has(AppConstants.RESPONSES.LoginResponse.USERNAME))
			{
				username = childObject.getString(AppConstants.RESPONSES.LoginResponse.USERNAME);
			}
			if(childObject.has(AppConstants.RESPONSES.LoginResponse.VID))
			{
				userid = childObject.getString(AppConstants.RESPONSES.LoginResponse.VID);
			}
			
			loginDTO = new UserLoginDTO(status, username, userid);
			
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loginDTO;
	}

	public static UploadDataResponseDTO getUploadDataResponse(String s)
	{
		boolean status = false;
		UploadDataResponseDTO responseDTO = new UploadDataResponseDTO();
		
		try
		{
			if(s != null && s.length() > 0 )
			{
				s = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(s);
			}
			
			JSONObject jsonObject = new JSONObject(new String(s));
			status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
			
			if(status)
			{
				if(jsonObject.has(AppConstants.RESPONSES.QueryResponse.QUID))
				{
					responseDTO.setQuery(jsonObject.getString(AppConstants.RESPONSES.QueryResponse.QUID));
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return responseDTO;
	}

	/*public static MediaUploadResponse getMediaUploadResponse(String s)
	{
		MediaUploadResponse uploadResponse = new MediaUploadResponse();
		try {
			JSONObject jsonObject = new JSONObject(new String(s));
			
			if(jsonObject.has("success"))
			{
				uploadResponse.setSuccess(jsonObject.getBoolean("success"));
			}
			if(jsonObject.has("user_id"))
			{
				uploadResponse.setdataId(jsonObject.getString("data_id"));
			}
			if(jsonObject.has("file_name"))
			{
				uploadResponse.setFile_name(jsonObject.getString("file_name"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadResponse;
	}*/
}
