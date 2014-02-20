/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- ADD IPD ACTIVITY
 * DESCRIPTION 		:- SAVE IPD
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * --------------------------------------------------------------------------------------------------------------------
 */
package in.professionalacademyca.ca.service;

import in.professionalacademyca.ca.app.AppConstants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class ServiceDelegate {

	public static String postData(String url, JSONObject dataToSend) throws JSONException{  
        // Create a new HttpClient and Post Header
				
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
 
        String text = null;
        try {
 
            JSONArray postjson=new JSONArray();
            postjson.put(dataToSend);
             
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(dataToSend.toString());
            httppost.setEntity(se); 
            
                       
            // Execute HTTP Post Request
            System.out.print(dataToSend);
            HttpResponse response = httpclient.execute(httppost);
 
            // for JSON:
            if(response != null)
            {
                InputStream is = response.getEntity().getContent();
 
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
 
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                text = sb.toString();
            }
  
        }catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        }
        if(text != null)
        {
        	Log.e("----------> " , text);
        }
        
        return text;
    }
	
	public static boolean uploadProfileRecording(String fileName, File file) {
		boolean uploaded = false;
		String[] values = { fileName };
		String uploadProfilesURL = AppConstants.URLS.BASE_URL + AppConstants.URLS.UPLOAD_URL;

		Log.v("uploadProfileRecording", "The url for upload is " + uploadProfilesURL);
		try {
			uploaded = postRecordedFile(file, uploadProfilesURL);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return uploaded;
	}
	
	public static String postData1(String url, JSONObject dataToSend) throws JSONException{  
        // Create a new HttpClient and Post Header
				
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
 
        String text = null;
        try {
 
        	
        	List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        	nvp.add(new BasicNameValuePair("json", dataToSend.toString()));
            /*JSONArray postjson=new JSONArray();
            postjson.put(dataToSend);
             
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(dataToSend.toString());*/
        	
        	
            httppost.setEntity(new UrlEncodedFormEntity(nvp)); 
            
                       
            // Execute HTTP Post Request
            System.out.print(dataToSend);
            HttpResponse response = httpclient.execute(httppost);
 
            // for JSON:
            if(response != null)
            {
                InputStream is = response.getEntity().getContent();
 
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
 
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                text = sb.toString();
            }
  
        }catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        }
        if(text != null)
        {
        	Log.e("----------> " , text);
        }
        
        return text;
    }
	
	
	public static boolean postRecordedFile(File file, String url) throws UnsupportedEncodingException, ClientProtocolException, IOException{
		boolean fileSent = false;
		
		HttpClient httpclient = new DefaultHttpClient();

	    HttpPost httppost = new HttpPost(url);
	    	    
	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(file, "binary/octet-stream");
	    mpEntity.addPart("FILE", cbFile);
	    httppost.setEntity(mpEntity);

	    System.out.println("executing request " + httppost.getRequestLine());
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity resEntity = response.getEntity();

	    System.out.println(response.getStatusLine());
	    if (resEntity != null) {
	      System.out.println(EntityUtils.toString(resEntity));
	    }
	    
	    if (resEntity != null) {
	      resEntity.consumeContent();
	    }

	    httpclient.getConnectionManager().shutdown();
	    
		return fileSent;
	}
	
	public static String postRecordedFile1(String type, File file, JSONObject json, String url) throws UnsupportedEncodingException, ClientProtocolException, IOException{
		
		
		//-- String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/adwallz_wallz/addwallz_20130829013551.jpg";
		//String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/adwallz_wallz/song.mp3";
		//-- file = new File(existingFileName);
		
		String fileName = file.getName();
		
		//--FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
		FileInputStream fileInputStream = new FileInputStream(file);
		int bytesAvailable = fileInputStream.available();
		
        byte[] buffer = new byte[bytesAvailable];
        // read file and write it into form...
        long bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
        
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		ByteArrayBody bab = new ByteArrayBody(buffer, fileName);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		//reqEntity.addPart("image", bab);
		reqEntity.addPart(type, bab);
	    
		FormBodyPart bodyPart = new FormBodyPart("user_id", new StringBody("vikalp"));
		reqEntity.addPart(bodyPart);
		
		postRequest.setEntity(reqEntity);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = null;
		StringBuffer stringResponse = new StringBuffer();
		while((line = in.readLine()) != null) 
		{
			stringResponse.append(line);
			Log.e("postRecordedFile1", line);
		}
		response = null;
		in.close();
		in = null;
		reqEntity = null;
		bodyPart = null;
		postRequest = null;
		
		return stringResponse.toString();
	}
	
	public static void doFileUpload() {

	    HttpURLConnection conn = null;
	    DataOutputStream dos = null;
	    DataInputStream inStream = null;
	    String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/adwallz_wallz/song.mp3";
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1 * 1024 * 1024;
	    String responseFromServer = "";
	    String urlString = "http://mywebsite.com/directory/upload.php";

	    try {

	        //------------------ CLIENT REQUEST
	        FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
	        // open a URL connection to the Servlet
	        URL url = new URL(urlString);
	        // Open a HTTP connection to the URL
	        conn = (HttpURLConnection) url.openConnection();
	        // Allow Inputs
	        conn.setDoInput(true);
	        // Allow Outputs
	        conn.setDoOutput(true);
	        // Don't use a cached copy.
	        conn.setUseCaches(false);
	        // Use a post method.
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	        dos = new DataOutputStream(conn.getOutputStream());
	        dos.writeBytes(twoHyphens + boundary + lineEnd);
	        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
	        dos.writeBytes(lineEnd);
	        // create a buffer of maximum size
	        bytesAvailable = fileInputStream.available();
	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        buffer = new byte[bufferSize];
	        // read file and write it into form...
	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	        while (bytesRead > 0) {

	            dos.write(buffer, 0, bufferSize);
	            bytesAvailable = fileInputStream.available();
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	        }

	        // send multipart form data necesssary after file data...
	        dos.writeBytes(lineEnd);
	        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	        // close streams
	        Log.e("Debug", "File is written");
	        fileInputStream.close();
	        dos.flush();
	        dos.close();

	    } catch (MalformedURLException ex) {
	        Log.e("Debug", "error: " + ex.getMessage(), ex);
	    } catch (IOException ioe) {
	        Log.e("Debug", "error: " + ioe.getMessage(), ioe);
	    }

	    //------------------ read the SERVER RESPONSE
	    try {

	        inStream = new DataInputStream(conn.getInputStream());
	        String str;

	        while ((str = inStream.readLine()) != null) {

	            Log.e("Debug", "Server Response " + str);

	        }

	        inStream.close();

	    } catch (IOException ioex) {
	        Log.e("Debug", "error: " + ioex.getMessage(), ioex);
	    }
	}
}
