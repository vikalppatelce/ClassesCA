package in.professionalacademyca.ca.sql;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {
	
	public static final String DB_NAME = "CaDB";
	
	public static final String TABLE_QUERY 							= "query";
	public static final String TABLE_TIME_TABLE 					= "timetable";
	
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
	
	public static class Time_Table_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/timetable");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/timetable";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_NAME 				= "name";
		public static final String COLUMN_DATA_ID 			= "data_id";
		public static final String COLUMN_URL 				= "url";
		public static final String COLUMN_SYNC_STATUS 		= "status";
	}

}
