/* HISTORY
 * CATEGORY 		:- DATABASE
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- DATABASE CONSTANTS
 * DESCRIPTION 		:- SUPPLIES DATABASE CONSTANTS CREATING AT TIME OF OWN CONTENT PROVIDER [DATABASE CREATION]
 * SEARCH           :- D: ADAPTER BUTTON SPINNER ASYNCTASK 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001       VIKALP PATEL    04/03/2014       				
 * --------------------------------------------------------------------------------------------------------------------
 */

package in.professionalacademyca.ca.sql;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {
	
	public static final String DB_NAME = "CaDB";
	
	public static final String TABLE_QUERY 							= "query";
	public static final String TABLE_TIME_TABLE 					= "timetable";
	public static final String TABLE_BATCH 							= "batch";
	public static final String TABLE_AREA 							= "area";
	public static final String TABLE_NOTIFICATION 					= "notification";
	public static final String TABLE_NOTIFICATION_TEMP 				= "notificationTemp";
//	D: QUERY TABLE COLUMN NAME CONSTANTS [QUERY COLUMNS TABLE]
	public static class Query_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/query");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/query";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_QUERY 			= "_query";
		public static final String COLUMN_QUERY_DATE		= "query_date";
		public static final String COLUMN_RESPONSE_DATE		= "response_date";
		public static final String COLUMN_RESPONSE 			= "response";
		public static final String COLUMN_BATCH 			= "batch";
		public static final String COLUMN_LEVEL 			= "level";
		public static final String COLUMN_SUBJECT 			= "subject";
		public static final String COLUMN_SYNC_STATUS 		= "post";
	}
// D: BATCH TABLE COLUMN NAME CONSTANTS [BATCH COLUMN TABLE]
	public static class Batch_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/batch");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/batch";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_AREA_ID 			= "a_id";
		public static final String COLUMN_BATCH_ID 			= "batch_id";
		public static final String COLUMN_NAME 				= "name";
		public static final String COLUMN_AREA_NAME 			= "area_name";
	}
	
//	D: TIME TABLE COLUMN NAME CONSTANTS [TIMETABLE COLUMN]
	public static class Time_Table_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/timetable");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/timetable";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_TIME_TABLE_DATE	= "_date";
		public static final String COLUMN_REMARK 			= "_remark";
		public static final String COLUMN_BATCH_NAME 		= "batch_name";
		public static final String COLUMN_LECTURE 			= "lecture";
		public static final String COLUMN_START_TIME 		= "start";
		public static final String COLUMN_END_TIME 			= "end";
		public static final String COLUMN_PROFESSOR 		= "professor";
		public static final String COLUMN_BATCH_REMARK 		= "batch_remark";
		public static final String COLUMN_AREA_NAME 		= "area_name";
	}
//	D: NOTIFICATION COLUMN NAME CONSTANTS [NOTIFICATION TABLE COLUMN]
	public static class Notification_Columnns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/notification");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/notification";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_NOTIFICATION_ID	= "n_id";
		public static final String COLUMN_BATCH 			= "batch";
		public static final String COLUMN_TITLE 			= "title";
		public static final String COLUMN_DESCRIPTION		= "description";
		public static final String COLUMN_NOTIFICATION_DATE	= "ndate";
		public static final String COLUMN_SYNC_STATUS 		= "status";
	}
//	D: NOTIFICATION TEMP COLUMN NAME CONSTANT. USE TO SEND LATEST NOTIFICATION ON DEVICE [NOTIFICATION COLUMN TABLE].
	public static class Notification_Temp_Columnns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/notificationTemp");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/notificationTemp";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_NOTIFICATION_ID	= "n_id";
	}
	public static class Area_Columnns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/area");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/area";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_AREA_ID 			= "a_id";
		public static final String COLUMN_AREA_NAME 	    = "name";
	}

}
