package in.professionalacademyca.ca.service;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.dto.AnswerDTO;
import in.professionalacademyca.ca.sql.DBConstant;
import in.professionalacademyca.ca.ui.NotificationActivity;
import in.professionalacademyca.ca.ui.PostQueryActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    
    static final String TAG = "GCMService";
    static final String MESSAGE_TYPE_NOTIFICATION = "notification";
    static final String MESSAGE_TYPE_QUERY = "query";
    String notificationContent = null;
    String notificationTitle = null;
    
    JSONArray notification;

    ArrayList<AnswerDTO> unAnsQueryDTOs;
    JSONArray query_answer = null;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

//        sendNotification("Received: " + extras.toString());
        Log.i(TAG, "Received: " + extras.toString());
        
        
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) 
            {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) 
            {
                sendNotification("Deleted messages on server: " +extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
            {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)+ "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
            else if (MESSAGE_TYPE_NOTIFICATION.equalsIgnoreCase(messageType)) 
            {
                // This loop represents the service doing some work.
                
                    Log.i(TAG, "Working..." + SystemClock.elapsedRealtime());
				try {
					uploadNotificationData();
					} 
				catch (Exception e) 
				{
					
				}
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                
                // Post notification of received message.
//                sendNotification("New Notification", notificationContent, "notification");
                Log.i(TAG, "Received: " + extras.toString());
            }
            else if (MESSAGE_TYPE_QUERY.equalsIgnoreCase(messageType)) 
            {
                // This loop represents the service doing some work.
                
                    Log.i(TAG, "Working..." + SystemClock.elapsedRealtime());
				try {
					loadUnQueryData();
					uploadUnQueryData();
					} 
				catch (Exception e) 
				{
					
				}
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                
                // Post notification of received message.
//                sendNotification("New Notification", notificationContent, "notification");
                Log.i(TAG, "Received: " + extras.toString());
            }
            
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String title , String content , String where ) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent;
        if(where.equalsIgnoreCase("notification"))
        {
        	contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, NotificationActivity.class), 0);	
        }
        else
        {
        	contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, PostQueryActivity.class), 0);
        }
        

        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
//        .setContentTitle("GCM Notification")
        .setContentTitle(title)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(title))
        .setContentText(content);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, NotificationActivity.class), 0);	
        

        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
//        .setContentTitle("GCM Notification")
        .setContentTitle(msg)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    /*
     * NOTIFICATION
     */
    
	public void uploadNotificationData()
	{
		long  notificationID=0;
		try
		{
			Cursor c = getContentResolver().query(DBConstant.Notification_Temp_Columnns.CONTENT_URI, null, null, null, DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID + " DESC");
			if(c!=null && c.getCount()>0)
			{
				c.moveToFirst();
				notificationID = Long.parseLong(c.getString(c.getColumnIndex(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID)));
			}
			else
			{
				try
				{
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String _date = df.format(Calendar.getInstance().getTime());
					notificationID = strDateToUnixTimestamp(_date);
					Log.i("Notification ID", String.valueOf(notificationID) + ": "+ System.currentTimeMillis()/1000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
//				notificationID = 0;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("jSON - Query", e.toString());
		}		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		
		JSONObject jsonObject = RequestBuilder.getQueryNotificationData(currentSIMImsi,notificationID);
		Log.e("NOTIFICATION------>>>>>>>>>>", jsonObject.toString());
		UploadNotificationTask uploadNotificationTask = new UploadNotificationTask();
		uploadNotificationTask.execute(new JSONObject[]{jsonObject});
	}
	
	private static long strDateToUnixTimestamp(String dt) {
        DateFormat formatter;
        Date date = null;
        long unixtime;
        formatter = new SimpleDateFormat("dd/MM/yy");
        try {
            date = formatter.parse(dt);
        } catch (ParseException ex) {
 
            ex.printStackTrace();
        }
        unixtime = date.getTime() / 1000L;
        return unixtime;
    }
	private class UploadNotificationTask extends AsyncTask<JSONObject, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			JSONObject dataToSend = params[0];
			boolean status = false;
			String type =  null, notification_txt= null, notification_date=null,notification_id=null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.NOTIFICATION_URL, dataToSend);
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					if(status)
					{
						try {
		                    // Getting JSON Array node
		                    notification = jsonObject.getJSONArray("notification");
		 
		                    // looping through All Contacts
		                    for (int i = 0; i < notification.length(); i++) {
		                        JSONObject c = notification.getJSONObject(i);
		                         type="";
		                         notification_date="";
		                         notification_txt="";
		                         notification_id="";
		                        type = c.getString("notification_type");
		                        notification_date = c.getString("notification_date");
		                        notification_txt = c.getString("notification_text");
		                        notification_id = c.getString("created_on");

		                        notificationContent = notification_txt;
		                        
		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_TITLE, notification_txt);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE, notification_date);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_BATCH, type);
		    					contentValues.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID, notification_id);
		    					getContentResolver().insert(DBConstant.Notification_Columnns.CONTENT_URI, contentValues);
		    					
		    					
		                        ContentValues tempValues = new ContentValues();
		    					tempValues.put(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID, notification_id);
		    					getContentResolver().insert(DBConstant.Notification_Temp_Columnns.CONTENT_URI, tempValues);
		    					
		    					
//		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("Notification","New Notifcation Added");
		                        // adding contact to contact list
		                    }
		                    
		                    if(notification.length() > 1)
		                    {
		                    	notificationContent = notification.length() + " new notification received";
		                    }
		                    
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
					}
					}
				Log.e("UploadTask","");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			sendNotification("New Notification", notificationContent, "notification");
		}
	}
	
	/*
	 * QUERY
	 */
	
	public void loadUnQueryData()
	{
		String _id;
		String query;
		String date;

		Cursor c = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, DBConstant.Query_Columns.COLUMN_RESPONSE +"=?", new String[]{"0"}, null);
		if( c != null && c.getCount() > 0)
		{
			unAnsQueryDTOs = new ArrayList<AnswerDTO>();
			while(c.moveToNext())
			{
				_id = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_ID));
				query = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY));
				date = c.getString(c.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY_DATE));
				unAnsQueryDTOs.add(new AnswerDTO(_id, query, null, date,null));
			}
			c.close();

		}
	}
	public void uploadUnQueryData()
	{
		JSONObject tables = new JSONObject();
		try
		{
			if(unAnsQueryDTOs != null && unAnsQueryDTOs.size() > 0)
			{
				JSONArray exp = RequestBuilder.getUnAnsQueryDetails(unAnsQueryDTOs);
				tables.put("tables", exp);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("jSON - Query", e.toString());
		}		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String currentSIMImsi = mTelephonyMgr.getDeviceId();
		
		JSONObject jsonObject = RequestBuilder.getQueryData(currentSIMImsi, tables);
		Log.e("UNQUERY--------------->>>>>>>>>>", jsonObject.toString());
		Log.e("UNQUERY--------------->>>>>>>>>>", tables.toString());
		UploadUnAnswerTask uploadTask = new UploadUnAnswerTask();
		uploadTask.execute(new JSONObject[]{jsonObject});
	}
	
	private class UploadUnAnswerTask extends AsyncTask<JSONObject, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(JSONObject... params) {
			// TODO Auto-generated method stub
			JSONObject dataToSend = params[0];
			boolean status = false;
			String device_id=null,currentSIMImsi=null;
			String query_reply =  null, query_id= null, reply_date=null;
			try {
				String jsonStr = ServiceDelegate.postData(AppConstants.URLS.ANSWER_URL, dataToSend);
				TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				currentSIMImsi = mTelephonyMgr.getDeviceId();
				
				//str = "{\"tables\":{\"service\":[]},\"lov\":{\"location\":[\"L 1\"],\"expense_category\":[],\"patient_type\":[\"OPD\",\"IPD\",\"SX\"],\"payment_mode\":[\"M2\",\"M1\"],\"diagnose_procedure\":[],\"referred_by\":[\"R2\",\"R1\"],\"start_time\":[],\"surgery_level\":[\"Level : 7\",\"Level : 6\",\"Level : 5\",\"Level : 4\",\"Level : 3\",\"Level : 2\",\"Level : 1\"],\"team_member\":[],\"ward\":[]}}";
				if(jsonStr != null)
				{
					JSONObject jsonObject = new JSONObject(new String(jsonStr));
					status = jsonObject.getBoolean(AppConstants.RESPONSES.QueryResponse.QSTATUS);
					device_id=jsonObject.getString("device_id");
					if(status && currentSIMImsi.equalsIgnoreCase(device_id))
					{
						try {
		                    // Getting JSON Array node
		                    query_answer = jsonObject.getJSONArray("query_answer");
		 
		                    // looping through All Contacts
		                    for (int i = 0; i < query_answer.length(); i++) {
		                        JSONObject c = query_answer.getJSONObject(i);
								query_reply = "";
								query_id = "";
								reply_date = "";
								query_reply = c.getString("query_replay");
								query_id = c.getString("question_id");
								reply_date = c.getString("replay_date");
								
								notificationContent = query_reply;

		                        ContentValues contentValues = new ContentValues();
		    					contentValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE, query_reply);
		    					contentValues.put(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE, reply_date);
		    					int col = getContentResolver().update(DBConstant.Query_Columns.CONTENT_URI,contentValues,DBConstant.Query_Columns.COLUMN_ID + "=?",new String[] { query_id});
		    					Log.e("ROWS UPDATED : data : ", col + "");
		    					
		    					try
								{
		    						Cursor cr = getContentResolver().query(DBConstant.Query_Columns.CONTENT_URI, null, DBConstant.Query_Columns.COLUMN_ID +"=?", new String[]{query_id}, null);
		    						// Setting Dialog Title
		    						if(cr!=null && cr.getCount()>0)
		    						{
		    							cr.moveToFirst();
		    							notificationTitle = cr.getString(cr.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY));
		    							
		    						}
								}
								catch(Exception e)
								{
									
								}
		                        // adding contact to contact list
		                    }
		                    
		                    if(query_answer.length() > 1)
		                    {
		                    	notificationTitle = "Query";
		                    	notificationContent = query_answer + " is Answered";
		                    }
		                    
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
					}
					}
				Log.e("UploadTask","");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			sendNotification(notificationTitle, notificationContent, "query");
		}
	}


	
}
