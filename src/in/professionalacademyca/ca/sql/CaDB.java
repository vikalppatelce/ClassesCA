package in.professionalacademyca.ca.sql;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class CaDB extends ContentProvider {

	public static final String AUTHORITY = "in.professionalacademyca.ca.sql.CaDB";
	
	private static final UriMatcher sUriMatcher;
	private static final int QUERY = 1;
	private static final int TIME_TABLE = 2;
	private static final int NOTIFICATION = 3;
	private static final int NOTIFICATION_TEMP = 4;
	private static final int AREA = 5;
	private static final int BATCH = 6;
	
	private static HashMap<String, String> queryProjectionMap;
	private static HashMap<String, String> timeTableProjectionMap;
	private static HashMap<String, String> notificationProjectionMap;
	private static HashMap<String, String> notificationTempProjectionMap;
	private static HashMap<String, String> areaProjectionMap;
	private static HashMap<String, String> batchProjectionMap;
	
	private static final int DATABASE_VERSION = 1;
	
	OpenHelper openHelper;
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DBConstant.DB_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			//location table
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_QUERY);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Query_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );//EU10001
			strBuilder.append(DBConstant.Query_Columns.COLUMN_QUERY +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_RESPONSE +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_QUERY_DATE +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_BATCH +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_LEVEL +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_SUBJECT +" TEXT , " );
			strBuilder.append(DBConstant.Query_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("TABLE_QUERY",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_TIME_TABLE);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_AREA_NAME+" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_BATCH_NAME+" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_BATCH_REMARK+" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_END_TIME +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_LECTURE +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_PROFESSOR +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_REMARK +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_START_TIME +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_TIME_TABLE_DATE +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("TABLE",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_NOTIFICATION);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID +" INTEGER UNIQUE, " );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_BATCH+" TEXT , " );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_TITLE+" TEXT , " );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_DESCRIPTION +" TEXT , " );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE +" TEXT , " );
			strBuilder.append(DBConstant.Notification_Columnns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("NOTIFICATION",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_NOTIFICATION_TEMP);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Notification_Temp_Columnns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID +" INTEGER UNIQUE" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("NOTIFICATION TEMP",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_AREA);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Area_Columnns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Area_Columnns.COLUMN_AREA_ID +" INTEGER," );
			strBuilder.append(DBConstant.Area_Columnns.COLUMN_AREA_NAME +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("AREA",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_BATCH);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Batch_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilder.append(DBConstant.Batch_Columns.COLUMN_AREA_ID +" INTEGER," );
			strBuilder.append(DBConstant.Batch_Columns.COLUMN_BATCH_ID +" INTEGER," );
			strBuilder.append(DBConstant.Batch_Columns.COLUMN_AREA_NAME +" TEXT," );
			strBuilder.append(DBConstant.Batch_Columns.COLUMN_NAME +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("BATCH",strBuilder.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_QUERY);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TIME_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_NOTIFICATION);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_NOTIFICATION_TEMP);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_AREA);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_BATCH);
			onCreate(db);

		}
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case QUERY:
			count = db.delete(DBConstant.TABLE_QUERY, where, whereArgs);
			break;
		case TIME_TABLE:
			count = db.delete(DBConstant.TABLE_TIME_TABLE, where, whereArgs);
			break;
		case NOTIFICATION:
			count = db.delete(DBConstant.TABLE_NOTIFICATION, where, whereArgs);
			break;
		case NOTIFICATION_TEMP:
			count = db.delete(DBConstant.TABLE_NOTIFICATION_TEMP, where, whereArgs);
			break;
		case AREA:
			count = db.delete(DBConstant.TABLE_AREA, where, whereArgs);
			break;
		case BATCH:
			count = db.delete(DBConstant.TABLE_BATCH, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
		case QUERY:
			return DBConstant.Query_Columns.CONTENT_TYPE;
		case TIME_TABLE:
			return DBConstant.Time_Table_Columns.CONTENT_TYPE;
		case NOTIFICATION:
			return DBConstant.Notification_Columnns.CONTENT_TYPE;
		case NOTIFICATION_TEMP:
			return DBConstant.Notification_Temp_Columnns.CONTENT_TYPE;
		case AREA:
			return DBConstant.Area_Columnns.CONTENT_TYPE;
		case BATCH:
			return DBConstant.Batch_Columns.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != QUERY && sUriMatcher.match(uri) != TIME_TABLE
				&& sUriMatcher.match(uri) != BATCH && sUriMatcher.match(uri) != AREA
				&& sUriMatcher.match(uri) != NOTIFICATION && sUriMatcher.match(uri) != NOTIFICATION_TEMP) 
		{ 
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
		
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} 
		else {
			values = new ContentValues();
		}
		
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = 0;
		
		switch (sUriMatcher.match(uri)) 
		{
			case QUERY:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_QUERY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Query_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TIME_TABLE:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_TIME_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Time_Table_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case NOTIFICATION:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_NOTIFICATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Notification_Columnns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case NOTIFICATION_TEMP:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_NOTIFICATION_TEMP, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Notification_Temp_Columnns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case AREA:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_AREA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Area_Columnns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case BATCH:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_BATCH, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Batch_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
				
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		openHelper 		= new OpenHelper(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		case QUERY:
			qb.setTables(DBConstant.TABLE_QUERY);
			qb.setProjectionMap(queryProjectionMap);
			break;
		case TIME_TABLE:
			qb.setTables(DBConstant.TABLE_TIME_TABLE);
			qb.setProjectionMap(timeTableProjectionMap);
			break;
		case NOTIFICATION:
			qb.setTables(DBConstant.TABLE_NOTIFICATION);
			qb.setProjectionMap(notificationProjectionMap);
			break;
		case NOTIFICATION_TEMP:
			qb.setTables(DBConstant.TABLE_NOTIFICATION_TEMP);
			qb.setProjectionMap(notificationTempProjectionMap);
			break;
		case AREA:
			qb.setTables(DBConstant.TABLE_AREA);
			qb.setProjectionMap(areaProjectionMap);
			break;
		case BATCH:
			qb.setTables(DBConstant.TABLE_BATCH);
			qb.setProjectionMap(batchProjectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
//		SQLiteDatabase db = openHelper.getReadableDatabase();
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count = -1;
		switch (sUriMatcher.match(uri)) {
		case QUERY:
			count = db.update(DBConstant.TABLE_QUERY, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		case TIME_TABLE:
			count = db.update(DBConstant.TABLE_TIME_TABLE, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		case NOTIFICATION:
			count = db.update(DBConstant.TABLE_NOTIFICATION, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		case NOTIFICATION_TEMP:
			count = db.update(DBConstant.TABLE_NOTIFICATION_TEMP, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		case AREA:
			count = db.update(DBConstant.TABLE_AREA, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		case BATCH:
			count = db.update(DBConstant.TABLE_BATCH, values, where, whereArgs);
			Log.e("VIKALP.............", "UPDATING............");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

static {
		
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_QUERY, QUERY);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TIME_TABLE, TIME_TABLE);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NOTIFICATION, NOTIFICATION);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NOTIFICATION_TEMP, NOTIFICATION_TEMP);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_AREA, AREA);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_BATCH, BATCH);

		queryProjectionMap = new HashMap<String, String>();
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_ID, DBConstant.Query_Columns.COLUMN_ID);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_QUERY, DBConstant.Query_Columns.COLUMN_QUERY);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_QUERY_DATE, DBConstant.Query_Columns.COLUMN_QUERY_DATE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE, DBConstant.Query_Columns.COLUMN_RESPONSE_DATE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_RESPONSE, DBConstant.Query_Columns.COLUMN_RESPONSE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_BATCH, DBConstant.Query_Columns.COLUMN_BATCH);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_LEVEL, DBConstant.Query_Columns.COLUMN_LEVEL);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_SUBJECT, DBConstant.Query_Columns.COLUMN_SUBJECT);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_SYNC_STATUS, DBConstant.Query_Columns.COLUMN_SYNC_STATUS);
		
		timeTableProjectionMap = new HashMap<String, String>();
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_ID, DBConstant.Time_Table_Columns.COLUMN_ID);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_AREA_NAME, DBConstant.Time_Table_Columns.COLUMN_AREA_NAME);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_BATCH_NAME, DBConstant.Time_Table_Columns.COLUMN_BATCH_NAME);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_BATCH_REMARK, DBConstant.Time_Table_Columns.COLUMN_BATCH_REMARK);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_END_TIME, DBConstant.Time_Table_Columns.COLUMN_END_TIME);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_LECTURE, DBConstant.Time_Table_Columns.COLUMN_LECTURE);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_PROFESSOR, DBConstant.Time_Table_Columns.COLUMN_PROFESSOR);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_REMARK, DBConstant.Time_Table_Columns.COLUMN_REMARK);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_START_TIME, DBConstant.Time_Table_Columns.COLUMN_START_TIME);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_TIME_TABLE_DATE, DBConstant.Time_Table_Columns.COLUMN_TIME_TABLE_DATE);
		
		
		notificationProjectionMap = new HashMap<String, String>();
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_ID, DBConstant.Notification_Columnns.COLUMN_ID);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID, DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_ID);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_BATCH, DBConstant.Notification_Columnns.COLUMN_BATCH);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_TITLE, DBConstant.Notification_Columnns.COLUMN_TITLE);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_DESCRIPTION, DBConstant.Notification_Columnns.COLUMN_DESCRIPTION);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE, DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE);
		notificationProjectionMap.put(DBConstant.Notification_Columnns.COLUMN_SYNC_STATUS, DBConstant.Notification_Columnns.COLUMN_SYNC_STATUS);
		
		notificationTempProjectionMap = new HashMap<String, String>();
		notificationTempProjectionMap.put(DBConstant.Notification_Temp_Columnns.COLUMN_ID, DBConstant.Notification_Temp_Columnns.COLUMN_ID);
		notificationTempProjectionMap.put(DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID, DBConstant.Notification_Temp_Columnns.COLUMN_NOTIFICATION_ID);
		
		areaProjectionMap = new HashMap<String, String>();
		areaProjectionMap.put(DBConstant.Area_Columnns.COLUMN_ID, DBConstant.Area_Columnns.COLUMN_ID);
		areaProjectionMap.put(DBConstant.Area_Columnns.COLUMN_AREA_ID, DBConstant.Area_Columnns.COLUMN_AREA_ID);
		areaProjectionMap.put(DBConstant.Area_Columnns.COLUMN_AREA_NAME, DBConstant.Area_Columnns.COLUMN_AREA_NAME);
		
		batchProjectionMap = new HashMap<String, String>();
		batchProjectionMap.put(DBConstant.Batch_Columns.COLUMN_ID, DBConstant.Batch_Columns.COLUMN_ID);
		batchProjectionMap.put(DBConstant.Batch_Columns.COLUMN_AREA_ID, DBConstant.Batch_Columns.COLUMN_AREA_ID);
		batchProjectionMap.put(DBConstant.Batch_Columns.COLUMN_AREA_NAME, DBConstant.Batch_Columns.COLUMN_AREA_NAME);
		batchProjectionMap.put(DBConstant.Batch_Columns.COLUMN_BATCH_ID, DBConstant.Batch_Columns.COLUMN_BATCH_ID);
		batchProjectionMap.put(DBConstant.Batch_Columns.COLUMN_NAME, DBConstant.Batch_Columns.COLUMN_NAME);
	}
}
