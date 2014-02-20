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
	
	private static HashMap<String, String> queryProjectionMap;
	private static HashMap<String, String> timeTableProjectionMap;
	
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
			strBuilder.append(DBConstant.Query_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("TABLE_QUERY",strBuilder.toString());
			
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_TIME_TABLE);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_NAME+" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_DATA_ID+" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_URL +" TEXT , " );
			strBuilder.append(DBConstant.Time_Table_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("TABLE",strBuilder.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_QUERY);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TIME_TABLE);
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
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != QUERY && sUriMatcher.match(uri) != TIME_TABLE) 
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
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = openHelper.getReadableDatabase();
		//SQLiteDatabase db = openHelper.getWritableDatabase();
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

		queryProjectionMap = new HashMap<String, String>();
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_ID, DBConstant.Query_Columns.COLUMN_ID);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_QUERY, DBConstant.Query_Columns.COLUMN_QUERY);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_QUERY_DATE, DBConstant.Query_Columns.COLUMN_QUERY_DATE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE, DBConstant.Query_Columns.COLUMN_RESPONSE_DATE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_RESPONSE, DBConstant.Query_Columns.COLUMN_RESPONSE);
		queryProjectionMap.put(DBConstant.Query_Columns.COLUMN_SYNC_STATUS, DBConstant.Query_Columns.COLUMN_SYNC_STATUS);
		
		timeTableProjectionMap = new HashMap<String, String>();
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_ID, DBConstant.Time_Table_Columns.COLUMN_ID);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_NAME, DBConstant.Time_Table_Columns.COLUMN_NAME);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_DATA_ID, DBConstant.Time_Table_Columns.COLUMN_DATA_ID);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_URL, DBConstant.Time_Table_Columns.COLUMN_URL);
		timeTableProjectionMap.put(DBConstant.Time_Table_Columns.COLUMN_SYNC_STATUS, DBConstant.Time_Table_Columns.COLUMN_SYNC_STATUS);
	}
}
