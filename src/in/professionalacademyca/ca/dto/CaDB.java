/* HISTORY
 * CATEGORY			 :- DATABASE
 * DEVELOPER		 :- VIKALP PATEL
 * AIM      		 :- CONTENT PROVIDER
 * NOTE: BANK TABLE(ALREADY EXISTS) :- EXPENSE CATEGORY     DEPOSITEDBANK(THT'S WHY MODIFIED NAME) TABLE :- BANK 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 10001      VIKALP PATEL     06/01/2014       				MODIFIED PRIMARY KEY AS UTC EPOCH TIME FOR BETTER SYNC AT SERVICE SIDE.
 * 10002      VIKALP PATEL     08/01/2014						ADDED PAYMENT TABLE
 * 10003   	  VIKALP PATEL     09/01/2014       				ADDED DEPOSITED BANK IN LOV.
 * 10004      VIKALP PATEL     09/01/2014						ADDED TO RESOLVED FOREIGN KEY CONSTRAINT
 * 10005      VIKALP PATEL     13/01/2014       				ADDED COLUMN 'COLUMN_NAME_SEARCHALGO' IN 'PATIENNAME' TABLE
 * 10005      VIKALP PATEL     14/01/2014       				BUG - CHANGED DEPOSITED BANK PRIMARY KEY TO AUTOINCREMENT ONLY.
 * 10006      VIKALP PATEL     17/01/2014                       ADDED PATIENT DETAILS TABLE
 * 10007      VIKALP PATEL     17/01/2014                       ADDED SERVICE TYPE IN PATIENT TEMP TABLE      
 * --------------------------------------------------------------------------------------------------------------------
 */


package in.professionalacademyca.ca.dto;

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

public class CaDB extends ContentProvider{
	
	public static final String AUTHORITY = "in.professionalacademyca.ca.dto.CaDB";
	
	private static final UriMatcher sUriMatcher;
	private static final int LOCATION = 1;
	private static final int PROCEDURE = 2;
	private static final int WARD = 3;
	private static final int TMEMBER = 4;

	private static final int REF = 5;
	private static final int STARTTIME = 6;

	
	private static final int LEVEL = 7;
	private static final int MODEOFPAYMENT = 8;

	private static final int BANK = 9;
	private static final int EXPESES = 10;

	private static final int EXPESESDETAILS = 11;
	private static final int PATIENT = 12; //PATIENT
	private static final int TYPES = 13;
	private static final int PATIENT_PER = 14; //PATIENT TEMP
	private static final int DISTINCT = 15;
	private static final int RECORDING = 16;
	private static final int PAYMENT = 17;//SA10002
	private static final int PAYMENT_TEMP = 18;//EA10002
	private static final int DEPOSITED_BANK = 19;//EA10002
	private static final int PATIENTDETAILS = 20;//SA 10006
	private static final int PATIENTPERDETAILS = 21;//EA 10006

	private static HashMap<String, String> locationProjectionMap;
	private static HashMap<String, String> procedureProjectionMap;

	private static HashMap<String, String> wardProjectionMap;
	private static HashMap<String, String> tMemberProjectionMap;

	private static HashMap<String, String> refProjectionMap;
	private static HashMap<String, String> startTimeProjectionMap;
	private static HashMap<String, String> levelProjectionMap;
	private static HashMap<String, String> modeOfPaymentProjectionMap;
	private static HashMap<String, String> bankProjectionMap;
	private static HashMap<String, String> expensesProjectionMap;
	private static HashMap<String, String> expensesDetailsProjectionMap;
	private static HashMap<String, String> patientProjectionMap;
	private static HashMap<String, String> typesProjectionMap;
	private static HashMap<String, String> patientTempProjectionMap;
	private static HashMap<String, String> nameProjectionMap;
	private static HashMap<String, String> recordingProjectionMap;
	private static HashMap<String, String> paymentProjectionMap; //SA10002
	private static HashMap<String, String> paymentTempProjectionMap; //EA10002
	private static HashMap<String, String> depositedBankProjectionMap;//ADDED 10003
	private static HashMap<String, String> patientDetailsProjectionMap;//SA 10006
	private static HashMap<String, String> patientTempDetailsProjectionMap;//EA 10006
	
	
	
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DBConstant.DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//location table
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_LOCATIO);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Location_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Location_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );//EU10001
			strBuilder.append(DBConstant.Location_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.Location_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// procedure table
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_PROCEDURE);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Procedure_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Procedure_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );//EU10001
			strBuilder.append(DBConstant.Procedure_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.Procedure_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// ward
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_WARD);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Ward_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Ward_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );//EU10001
			strBuilder.append(DBConstant.Ward_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.Ward_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			//team member
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_TMEMBER);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.TMEMBER_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.TMEMBER_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.TMEMBER_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.TMEMBER_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// ref
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_REF);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Ref_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Ref_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," );
			strBuilder.append(DBConstant.Ref_Columns.COLUMN_NAME +" TEXT UNIQUE," );
			strBuilder.append(DBConstant.Ref_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// Start Time
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_STARTTIME);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.StartTime_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.StartTime_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.StartTime_Columns.COLUMN_NAME +" TEXT UNIQUE," );
			strBuilder.append(DBConstant.StartTime_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// level
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_LEVEL);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Level_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Level_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Level_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.Level_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			
			// mode of payment
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_MODEOFPAYMENT);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.ModeOfPayment_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.ModeOfPayment_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.ModeOfPayment_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.ModeOfPayment_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// bank
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_BANK);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Bank_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Bank_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Bank_Columns.COLUMN_NAME +" TEXT UNIQUE, " );
			strBuilder.append(DBConstant.Bank_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// types
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_T);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Types_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL 
			strBuilder.append(DBConstant.Types_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Types_Columns.COLUMN_NAME +" TEXT UNIQUE," );
			strBuilder.append(DBConstant.Types_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			
			// expenses
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_EXPENSES);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Expeses_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU VIKALP PATEL
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_AMOUNT +" NUMBER ," );
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_PAYMENT_MODE +" TEXT ," ); 
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_DESCRIPTION +" TEXT ," );
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_CATEGORY +" TEXT, " );
			strBuilder.append(DBConstant.Expeses_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			// expenses details
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_EXPENSES_DETAILS);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Expeses_Details_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Expeses_Details_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Expeses_Details_Columns.COLUMN_NAME +" TEXT ," );
			strBuilder.append(DBConstant.Expeses_Details_Columns.COLUMN_EXP_ID +" TEXT ," );
			strBuilder.append(DBConstant.Expeses_Details_Columns.COLUMN_URL +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			
			//patient
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_PATIENT);
			strBuilder.append('(');
//			strBuilder.append(DBConstant.Patient_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );  SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); // EU10001 
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_CUSTOM_ID +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_NAME +" TEXT ," );
//			strBuilder.append(DBConstant.Patient_Columns.COLUMN_ADDRESS +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_AGE +" NUMBER ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_TOTALCOUNT +" NUMBER ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_DIAGNOSIS +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_TYPE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_SERVICE_TYPE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_REF +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_DURATION +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_STARTTIME +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_WARD +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_EMERGENCY +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_TEAM_MEMBER +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_PROCEDURE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_LEVEL +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_SEX +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_SX_WATCH +" TEXT ," );
			//strBuilder.append(DBConstant.Patient_Columns.COLUMN_FEES +" NUMBER ," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_NOTE +" TEXT, " );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			//patient Temp
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_TEMP);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_CUSTOM_ID +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_NAME +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_AGE +" NUMBER ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_DIAGNOSIS +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_TYPE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_SERVICE_TYPE +" TEXT ," );//ADDED 10007
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_REF +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_EMERGENCY +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_SEX +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_STARTTIME +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_TEAM_MEMBER +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_LEVEL +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_DURATION +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_PROCEDURE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_WARD +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_SYNC_STATUS +" NUMBER," );
			strBuilder.append(DBConstant.Patient_Columns.COLUMN_TOTALCOUNT +" NUMBER ," );
			strBuilder.append(DBConstant.Patient_Temp_Columns.COLUMN_NOTE +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			
			
			//patient Name
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_NAME);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			//strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_ID +" INTEGER(20) FOREIGN KEY("+ DBConstant.Patient_Name_Columns.COLUMN_ID +") REFERENCES "+ DBConstant.TABLE_TEMP +"("+DBConstant.Payment_Temp_Columns.COLUMN_ID+")," ); //ADDED 10004
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_NAME +" TEXT, " );
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_RED_ID +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_NAME_SEARCHALGO +" TEXT ," );//ADDED 10003
			strBuilder.append(DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID +" TEXT " );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			//Log.d("CREATE_TABLE", strBuilder.toString());
			
			// recording details
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_RECORDING);
			strBuilder.append('(');
			//strBuilder.append(DBConstant.Recoding_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," ); SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); //EU10001
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_TYPE +" NUMBER ," );
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_URL +" TEXT, " );
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_DATE +" TEXT, " );
			strBuilder.append(DBConstant.Recoding_Columns.COLUMN_SYNCKEDSTATUS +" TEXT" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());

			//payment SA10001
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_PAYMENT);
			strBuilder.append('(');
//			strBuilder.append(DBConstant.Patient_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );  SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); // EU10001 
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_CUSTOM_ID +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_RECEIVED_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_SERVICED_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_PAYMENT_SOURCE +" NUMBER ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_RECONCILE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_PAYMENT_MODE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_CHEQUE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_IN_HAND +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_TDS_PER +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_TDS_AMT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_AMOUNT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_BANK +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_TOTALCOUNT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_PY_WATCH +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.d("Payment", strBuilder.toString());
			
			
			//payment Temp
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_PAYMENT_TEMP);
			strBuilder.append('(');
//			strBuilder.append(DBConstant.Patient_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );  SU10001 VIKALP PATEL
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_ID +" INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP))," ); // EU10001 
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_CUSTOM_ID +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_RECEIVED_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_SERVICED_DATE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_SOURCE +" NUMBER ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_RECONCILE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_MODE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_CHEQUE +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_IN_HAND +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_TDS_PER +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_TDS_AMT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_AMOUNT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_LOCATION +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_BANK +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_TOTALCOUNT +" TEXT ," );
			strBuilder.append(DBConstant.Payment_Temp_Columns.COLUMN_SYNC_STATUS +" NUMBER" );
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			//EA10002
			
			// SA10003
			// bank
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_DEPOSITED_BANK);
			strBuilder.append('(');
		    strBuilder.append(DBConstant.Bank_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );// ADDED SU10006 VIKALP PATEL
			//strBuilder.append(DBConstant.Deposited_Bank_Columns.COLUMN_ID + " INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),"); // EU10001
			strBuilder.append(DBConstant.Deposited_Bank_Columns.COLUMN_NAME+ " TEXT, ");
			strBuilder.append(DBConstant.Deposited_Bank_Columns.COLUMN_SYNC_STATUS+ " NUMBER");
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("Deposited_Bank", strBuilder.toString());
			// EA10003
			
			//SA 10006
			// patient details
			strBuilder = new StringBuilder();
			strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_PATIENT_DETAILS);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Patient_Details_Columns.COLUMN_ID+ " INTEGER(20) NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),"); 
			strBuilder.append(DBConstant.Patient_Details_Columns.COLUMN_PATIENT_ID+ " TEXT ,");
			strBuilder.append(DBConstant.Patient_Details_Columns.COLUMN_PATIENT_TYPE+ " TEXT ,");
			strBuilder.append(DBConstant.Patient_Details_Columns.COLUMN_SYNC_STATUS+ " NUMBER ,");
			strBuilder.append(DBConstant.Patient_Details_Columns.COLUMN_URL+ " TEXT");
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("Patient_details", strBuilder.toString());
			
			
			// patient temp details
			strBuilder = new StringBuilder();
		    strBuilder.append("CREATE TABLE ");
			strBuilder.append(DBConstant.TABLE_TEMP_DETAILS);
			strBuilder.append('(');
			strBuilder.append(DBConstant.Patient_Temp_Details_Columns.COLUMN_ID+ " INTEGER(20) NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),"); 
			strBuilder.append(DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_ID+ " TEXT ,");
			strBuilder.append(DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_TYPE+ " TEXT ,");
			strBuilder.append(DBConstant.Patient_Temp_Details_Columns.COLUMN_SYNC_STATUS+ " NUMBER ,");
			strBuilder.append(DBConstant.Patient_Temp_Details_Columns.COLUMN_URL+ " TEXT");
			strBuilder.append(')');
			db.execSQL(strBuilder.toString());
			Log.i("Patient_temp_details", strBuilder.toString());			
			//EA 10006
		}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_BANK);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_T);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_EXPENSES);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_EXPENSES_DETAILS);
			
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_LEVEL);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_LOCATIO);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_MODEOFPAYMENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_PATIENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TEMP);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_PROCEDURE);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_REF);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_STARTTIME);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TMEMBER);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_RECORDING);
			
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_WARD);
			//SA10002
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_PAYMENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_PAYMENT_TEMP);
			//db.execSQL("DROP TABLE " + DBConstant.TABLE_PAYMENT);
			//db.execSQL("DROP TABLE " + DBConstant.TABLE_PAYMENT);
			//EA10002
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_DEPOSITED_BANK);//ADDED 10003
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_PATIENT_DETAILS);//SA 10006
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TEMP_DETAILS);//EA 10006
			
			onCreate(db);
		}
	}

	private static final int DATABASE_VERSION = 1;
		
	OpenHelper openHelper;


	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case LOCATION:
			count = db.delete(DBConstant.TABLE_LOCATIO, where, whereArgs);
			break;
		case WARD:
			count = db.delete(DBConstant.TABLE_WARD, where, whereArgs);
			break;
		case PROCEDURE:
			count = db.delete(DBConstant.TABLE_PROCEDURE, where, whereArgs);
			break;
		case TMEMBER:
			count = db.delete(DBConstant.TABLE_TMEMBER, where, whereArgs);
			break;
		case REF:
			count = db.delete(DBConstant.TABLE_REF, where, whereArgs);
			break;
		case STARTTIME:
			count = db.delete(DBConstant.TABLE_STARTTIME, where, whereArgs);
			break;
		case LEVEL:
			count = db.delete(DBConstant.TABLE_LEVEL, where, whereArgs);
			break;
		case MODEOFPAYMENT:
			count = db.delete(DBConstant.TABLE_MODEOFPAYMENT, where, whereArgs);
			break;
		case BANK:
			count = db.delete(DBConstant.TABLE_BANK, where, whereArgs);
			break;
		case EXPESES:
			count = db.delete(DBConstant.TABLE_EXPENSES, where, whereArgs);
			break;
		case EXPESESDETAILS:
			count = db.delete(DBConstant.TABLE_EXPENSES_DETAILS, where, whereArgs);
			break;
		case PATIENT:
			count = db.delete(DBConstant.TABLE_PATIENT, where, whereArgs);
			break;
		case PATIENT_PER:
			count = db.delete(DBConstant.TABLE_TEMP, where, whereArgs);
			break;
		case DISTINCT:
			count = db.delete(DBConstant.TABLE_NAME, where, whereArgs);
			break;
		case RECORDING:
			count = db.delete(DBConstant.TABLE_RECORDING, where, whereArgs);
			break;
		case TYPES:
			count = db.delete(DBConstant.TABLE_T, where, whereArgs);
			break;
			//SA10002
		case PAYMENT:
			count = db.delete(DBConstant.TABLE_PAYMENT, where, whereArgs);
			break;
		case PAYMENT_TEMP:
			count = db.delete(DBConstant.TABLE_PAYMENT_TEMP, where, whereArgs);
			break;
			//EA10002
			//SA10003
		case DEPOSITED_BANK:
			count = db.delete(DBConstant.TABLE_DEPOSITED_BANK, where, whereArgs);
			break;	
			//EA10003
			//SA 10006
		case PATIENTDETAILS:
			count = db.delete(DBConstant.TABLE_PATIENT_DETAILS, where, whereArgs);
			break;			
		case PATIENTPERDETAILS:
			count = db.delete(DBConstant.TABLE_TEMP_DETAILS, where, whereArgs);
			break;				
			//EA 10006
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
		case LOCATION:
			return DBConstant.Location_Columns.CONTENT_TYPE;
		case WARD:
			return DBConstant.Ward_Columns.CONTENT_TYPE;
		case PROCEDURE:
			return DBConstant.Procedure_Columns.CONTENT_TYPE;
		case TMEMBER:
			return DBConstant.TMEMBER_Columns.CONTENT_TYPE;
		case REF:
			return DBConstant.Ref_Columns.CONTENT_TYPE;
		case STARTTIME:
			return DBConstant.StartTime_Columns.CONTENT_TYPE;
		case LEVEL:
			return DBConstant.Level_Columns.CONTENT_TYPE;
		case MODEOFPAYMENT:
			return DBConstant.ModeOfPayment_Columns.CONTENT_TYPE;
		case BANK:
			return DBConstant.Bank_Columns.CONTENT_TYPE;
		case EXPESES:
			return DBConstant.Expeses_Columns.CONTENT_TYPE;
		case EXPESESDETAILS:
			return DBConstant.Expeses_Details_Columns.CONTENT_TYPE;
		case DISTINCT:	
			return DBConstant.Patient_Name_Columns.CONTENT_TYPE;
		case RECORDING:	
			return DBConstant.Recoding_Columns.CONTENT_TYPE;
		case PATIENT:
			return DBConstant.Patient_Columns.CONTENT_TYPE;
		case PATIENT_PER:
			return DBConstant.Patient_Temp_Columns.CONTENT_TYPE;
		case TYPES:
			return DBConstant.Types_Columns.CONTENT_TYPE;
			//SA10002
		case PAYMENT:
			return DBConstant.Patient_Columns.CONTENT_TYPE;
		case PAYMENT_TEMP:
			return DBConstant.Patient_Columns.CONTENT_TYPE;
			//EA10002
			//SA10003
		case DEPOSITED_BANK:
			return DBConstant.Deposited_Bank_Columns.CONTENT_TYPE;	
			//EA10003
			//SA10006
		case PATIENTDETAILS:
			return DBConstant.Patient_Details_Columns.CONTENT_TYPE;
			//EA10006
		case PATIENTPERDETAILS:
			return DBConstant.Patient_Temp_Details_Columns.CONTENT_TYPE;
			//EA10006
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}


	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != LOCATION && sUriMatcher.match(uri) != PROCEDURE
			&& sUriMatcher.match(uri) != WARD && sUriMatcher.match(uri) != TMEMBER
			&& sUriMatcher.match(uri) != REF && sUriMatcher.match(uri) != STARTTIME
			&& sUriMatcher.match(uri) != LEVEL && sUriMatcher.match(uri) != MODEOFPAYMENT
			&& sUriMatcher.match(uri) != BANK && sUriMatcher.match(uri) != EXPESES
			&& sUriMatcher.match(uri) != EXPESESDETAILS && sUriMatcher.match(uri) != PATIENT
			&& sUriMatcher.match(uri) != TYPES && sUriMatcher.match(uri) != PATIENT_PER
			&& sUriMatcher.match(uri) != PAYMENT && sUriMatcher.match(uri) != PAYMENT_TEMP//ADDED 10002
					&& sUriMatcher.match(uri) != DEPOSITED_BANK//ADDED 10003
			&& sUriMatcher.match(uri) != PATIENTDETAILS && sUriMatcher.match(uri) != PATIENTPERDETAILS//ADDED 10006
			&& sUriMatcher.match(uri) != DISTINCT && sUriMatcher.match(uri) != RECORDING) 
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
			case LOCATION:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_LOCATIO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Location_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case PROCEDURE:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_PROCEDURE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Procedure_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case WARD:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_WARD, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Ward_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TMEMBER:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_TMEMBER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.TMEMBER_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case REF:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_REF, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Ref_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case STARTTIME:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_STARTTIME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.StartTime_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case LEVEL:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_LEVEL, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Level_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case MODEOFPAYMENT:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_MODEOFPAYMENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.ModeOfPayment_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case BANK:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_BANK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Bank_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case EXPESES:
				 rowId = db.insert(DBConstant.TABLE_EXPENSES, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Expeses_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case EXPESESDETAILS:
				 rowId = db.insert(DBConstant.TABLE_EXPENSES_DETAILS, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Expeses_Details_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case PATIENT:
				 rowId = db.insert(DBConstant.TABLE_PATIENT, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case PATIENT_PER:
				 rowId = db.insert(DBConstant.TABLE_TEMP, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Temp_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case DISTINCT:
				 rowId = db.insert(DBConstant.TABLE_NAME, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Name_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case RECORDING:
				 rowId = db.insert(DBConstant.TABLE_RECORDING, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Recoding_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TYPES:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_T, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Types_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
				//SA 10002
			case PAYMENT:
				 rowId = db.insert(DBConstant.TABLE_PAYMENT, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case PAYMENT_TEMP:
				 rowId = db.insert(DBConstant.TABLE_PAYMENT_TEMP, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
				//EA 10002
				//SA 10003
			case DEPOSITED_BANK:
				 rowId = db.insert(DBConstant.TABLE_DEPOSITED_BANK, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Deposited_Bank_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
				//EA 10003
				//SA 10006
			case PATIENTDETAILS:
				 rowId = db.insert(DBConstant.TABLE_PATIENT_DETAILS, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Details_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case PATIENTPERDETAILS:
				 rowId = db.insert(DBConstant.TABLE_TEMP_DETAILS, null, values);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Patient_Temp_Details_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
				//EA 10006
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
		case LOCATION:
			qb.setTables(DBConstant.TABLE_LOCATIO);
			qb.setProjectionMap(locationProjectionMap);
			break;
		case PROCEDURE:
			qb.setTables(DBConstant.TABLE_PROCEDURE);
			qb.setProjectionMap(procedureProjectionMap);
			break;
		case WARD:
			qb.setTables(DBConstant.TABLE_WARD);
			qb.setProjectionMap(wardProjectionMap);
			break;
		case TMEMBER:
			qb.setTables(DBConstant.TABLE_TMEMBER);
			qb.setProjectionMap(tMemberProjectionMap);
			break;
		case REF:
			qb.setTables(DBConstant.TABLE_REF);
			qb.setProjectionMap(refProjectionMap);
			break;
		case STARTTIME:
			qb.setTables(DBConstant.TABLE_STARTTIME);
			qb.setProjectionMap(startTimeProjectionMap);
			break;
		case LEVEL:
			qb.setTables(DBConstant.TABLE_LEVEL);
			qb.setProjectionMap(levelProjectionMap);
			break;
		case MODEOFPAYMENT:
			qb.setTables(DBConstant.TABLE_MODEOFPAYMENT);
			qb.setProjectionMap(modeOfPaymentProjectionMap);
			break;
		case BANK:
			qb.setTables(DBConstant.TABLE_BANK);
			qb.setProjectionMap(bankProjectionMap);
			break;
		case EXPESES:
			qb.setTables(DBConstant.TABLE_EXPENSES);
			qb.setProjectionMap(expensesProjectionMap);
			break;
		case EXPESESDETAILS:
			qb.setTables(DBConstant.TABLE_EXPENSES_DETAILS);
			qb.setProjectionMap(expensesDetailsProjectionMap);
			break;
		case DISTINCT:
			SQLiteDatabase db = openHelper.getReadableDatabase();
			//return db.rawQuery("SELECT " + DBConstant.Patient_Name_Columns.COLUMN_ID +" , DISTINCT " + DBConstant.Patient_Name_Columns.COLUMN_NAME + " FROM " + DBConstant.TABLE_NAME +" WHERE ", new String[]{selection});
			String query = "";
			if(sortOrder.equalsIgnoreCase("0"))
			{
				query = "SELECT " + DBConstant.Patient_Name_Columns.COLUMN_ID +" , " + DBConstant.Patient_Name_Columns.COLUMN_NAME + " , " + DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID+ " , " + DBConstant.Patient_Name_Columns.COLUMN_DATE+ " , " + DBConstant.Patient_Name_Columns.COLUMN_LOCATION  +" , " + DBConstant.Patient_Name_Columns.COLUMN_RED_ID + " FROM " + DBConstant.TABLE_NAME +" WHERE " + selection;// +" Group By " + DBConstant.Patient_Name_Columns.COLUMN_NAME;
			}
			else if(sortOrder.equalsIgnoreCase("1"))
			{
				query = "SELECT " + DBConstant.Patient_Name_Columns.COLUMN_ID +" , " + DBConstant.Patient_Name_Columns.COLUMN_NAME + " , " + DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID+ " , " + DBConstant.Patient_Name_Columns.COLUMN_DATE+ " , " + DBConstant.Patient_Name_Columns.COLUMN_LOCATION + " , " + DBConstant.Patient_Name_Columns.COLUMN_RED_ID+ " FROM " + DBConstant.TABLE_NAME +" WHERE " + selection;// +" Group By " + DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID;
			}
			
			return db.rawQuery(query, null);
		case PATIENT:
			qb.setTables(DBConstant.TABLE_PATIENT);
			qb.setProjectionMap(patientProjectionMap);
			break;
		case RECORDING:
			qb.setTables(DBConstant.TABLE_RECORDING);
			qb.setProjectionMap(recordingProjectionMap);
			break;
		case PATIENT_PER:
			qb.setTables(DBConstant.TABLE_TEMP);
			qb.setProjectionMap(patientTempProjectionMap);
			break;
		case TYPES:
			qb.setTables(DBConstant.TABLE_T);
			qb.setProjectionMap(typesProjectionMap);
			break;
			//SA 10002
		case PAYMENT:
			qb.setTables(DBConstant.TABLE_PAYMENT);
			qb.setProjectionMap(paymentProjectionMap);
			break;
		case PAYMENT_TEMP:
			qb.setTables(DBConstant.TABLE_PAYMENT_TEMP);
			qb.setProjectionMap(paymentTempProjectionMap);
			break;
			//EA 10002
			//SA 10003
		case DEPOSITED_BANK:
			qb.setTables(DBConstant.TABLE_DEPOSITED_BANK);
			qb.setProjectionMap(depositedBankProjectionMap);
			break;
			//EA 10003
			//SA 10006
		case PATIENTDETAILS:
			qb.setTables(DBConstant.TABLE_PATIENT_DETAILS);
			qb.setProjectionMap(patientDetailsProjectionMap);
			break;
		case PATIENTPERDETAILS:
			qb.setTables(DBConstant.TABLE_TEMP_DETAILS);
			qb.setProjectionMap(patientTempDetailsProjectionMap);
			break;
			//EA 10006
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = openHelper.getReadableDatabase(); //SA 10002
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
		case LOCATION:
			count = db.update(DBConstant.TABLE_LOCATIO, values, where, whereArgs);
			break;
		case PROCEDURE:
			count = db.update(DBConstant.TABLE_PROCEDURE, values, where, whereArgs);
			break;
		case WARD:
			count = db.update(DBConstant.TABLE_WARD, values, where, whereArgs);
			break;
		case TMEMBER:
			count = db.update(DBConstant.TABLE_TMEMBER, values, where, whereArgs);
			break;
		case REF:
			count = db.update(DBConstant.TABLE_REF, values, where, whereArgs);
			break;
		case STARTTIME:
			count = db.update(DBConstant.TABLE_STARTTIME, values, where, whereArgs);
			break;
		case LEVEL:
			count = db.update(DBConstant.TABLE_LEVEL, values, where, whereArgs);
			break;
		case MODEOFPAYMENT:
			count = db.update(DBConstant.TABLE_MODEOFPAYMENT, values, where, whereArgs);
			break;
		case BANK:
			count = db.update(DBConstant.TABLE_BANK, values, where, whereArgs);
			break;
		case EXPESES:
			count = db.update(DBConstant.TABLE_EXPENSES, values, where, whereArgs);
			break;
		case EXPESESDETAILS:
			count = db.update(DBConstant.TABLE_EXPENSES_DETAILS, values, where, whereArgs);
			break;
		case PATIENT:
			count = db.update(DBConstant.TABLE_PATIENT, values, where, whereArgs);
			break;
		case PATIENT_PER:
			
			Log.e("--------HEMANT----------", "UPDATING......................");
			count = db.update(DBConstant.TABLE_TEMP, values, where, whereArgs);
			Log.e("--------HEMANT----------", "UPDATED......................" + count);
			
			
			break;
		case DISTINCT:
			count = db.update(DBConstant.TABLE_NAME, values, where, whereArgs);
			break;
		case RECORDING:
			count = db.update(DBConstant.TABLE_RECORDING, values, where, whereArgs);
			break;
		case TYPES:
			count = db.update(DBConstant.TABLE_T, values, where, whereArgs);
			break;
			//SA10002
		case PAYMENT:
			count = db.update(DBConstant.TABLE_PAYMENT, values, where, whereArgs);
			break;
		case PAYMENT_TEMP:
			count = db.update(DBConstant.TABLE_PAYMENT_TEMP, values, where, whereArgs);
			break;
			//EA10002
			//SA10003
		case DEPOSITED_BANK:
			count = db.update(DBConstant.TABLE_DEPOSITED_BANK, values, where, whereArgs);
			break;
			//EA10003
			//SA 10006
		case PATIENTDETAILS:
			count = db.update(DBConstant.TABLE_PATIENT_DETAILS, values, where, whereArgs);
			break;
		case PATIENTPERDETAILS:
			count = db.update(DBConstant.TABLE_TEMP_DETAILS, values, where, whereArgs);
			break;			
			//EA 10006
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	static {
		
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_LOCATIO, LOCATION);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_PROCEDURE, PROCEDURE);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_WARD, WARD);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TMEMBER, TMEMBER);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_REF, REF);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_STARTTIME, STARTTIME);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_LEVEL, LEVEL);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_MODEOFPAYMENT, MODEOFPAYMENT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_BANK, BANK);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_T, TYPES);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_EXPENSES, EXPESES);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_EXPENSES_DETAILS, EXPESESDETAILS);

		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_PATIENT, PATIENT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TEMP, PATIENT_PER);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NAME, DISTINCT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_RECORDING, RECORDING);
		
		//SA10002
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_PAYMENT, PAYMENT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_PAYMENT_TEMP, PAYMENT_TEMP);
		//EA10002
		
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_DEPOSITED_BANK, DEPOSITED_BANK);		//ADDED 10003
		
		//SA 10006
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_PATIENT_DETAILS, PATIENTDETAILS);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TEMP_DETAILS, PATIENTPERDETAILS);
		//EA 10006
		
		locationProjectionMap = new HashMap<String, String>();
		locationProjectionMap.put(DBConstant.Location_Columns.COLUMN_ID, DBConstant.Location_Columns.COLUMN_ID);
		locationProjectionMap.put(DBConstant.Location_Columns.COLUMN_NAME, DBConstant.Location_Columns.COLUMN_NAME);
		locationProjectionMap.put(DBConstant.Location_Columns.COLUMN_SYNC_STATUS, DBConstant.Location_Columns.COLUMN_SYNC_STATUS);
		
		procedureProjectionMap = new HashMap<String, String>();
		procedureProjectionMap.put(DBConstant.Procedure_Columns.COLUMN_ID, DBConstant.Procedure_Columns.COLUMN_ID);
		procedureProjectionMap.put(DBConstant.Procedure_Columns.COLUMN_NAME, DBConstant.Procedure_Columns.COLUMN_NAME);
		procedureProjectionMap.put(DBConstant.Procedure_Columns.COLUMN_SYNC_STATUS, DBConstant.Procedure_Columns.COLUMN_SYNC_STATUS);

		wardProjectionMap = new HashMap<String, String>();
		wardProjectionMap.put(DBConstant.Ward_Columns.COLUMN_ID, DBConstant.Ward_Columns.COLUMN_ID);
		wardProjectionMap.put(DBConstant.Ward_Columns.COLUMN_NAME, DBConstant.Ward_Columns.COLUMN_NAME);
		wardProjectionMap.put(DBConstant.Ward_Columns.COLUMN_SYNC_STATUS, DBConstant.Ward_Columns.COLUMN_SYNC_STATUS);

		tMemberProjectionMap = new HashMap<String, String>();
		tMemberProjectionMap.put(DBConstant.TMEMBER_Columns.COLUMN_ID, DBConstant.TMEMBER_Columns.COLUMN_ID);
		tMemberProjectionMap.put(DBConstant.TMEMBER_Columns.COLUMN_NAME, DBConstant.TMEMBER_Columns.COLUMN_NAME);
		tMemberProjectionMap.put(DBConstant.TMEMBER_Columns.COLUMN_SYNC_STATUS, DBConstant.TMEMBER_Columns.COLUMN_SYNC_STATUS);

		refProjectionMap = new HashMap<String, String>();
		refProjectionMap.put(DBConstant.Ref_Columns.COLUMN_ID, DBConstant.Ref_Columns.COLUMN_ID);
		refProjectionMap.put(DBConstant.Ref_Columns.COLUMN_NAME, DBConstant.Ref_Columns.COLUMN_NAME);
		refProjectionMap.put(DBConstant.Ref_Columns.COLUMN_SYNC_STATUS, DBConstant.Ref_Columns.COLUMN_SYNC_STATUS);

		startTimeProjectionMap = new HashMap<String, String>();
		startTimeProjectionMap.put(DBConstant.StartTime_Columns.COLUMN_ID, DBConstant.StartTime_Columns.COLUMN_ID);
		startTimeProjectionMap.put(DBConstant.StartTime_Columns.COLUMN_NAME, DBConstant.StartTime_Columns.COLUMN_NAME);
		startTimeProjectionMap.put(DBConstant.StartTime_Columns.COLUMN_SYNC_STATUS, DBConstant.StartTime_Columns.COLUMN_SYNC_STATUS);

		levelProjectionMap = new HashMap<String, String>();
		levelProjectionMap.put(DBConstant.Level_Columns.COLUMN_ID, DBConstant.Level_Columns.COLUMN_ID);
		levelProjectionMap.put(DBConstant.Level_Columns.COLUMN_NAME, DBConstant.Level_Columns.COLUMN_NAME);
		levelProjectionMap.put(DBConstant.Level_Columns.COLUMN_SYNC_STATUS, DBConstant.Level_Columns.COLUMN_SYNC_STATUS);

		modeOfPaymentProjectionMap = new HashMap<String, String>();
		modeOfPaymentProjectionMap.put(DBConstant.ModeOfPayment_Columns.COLUMN_ID, DBConstant.ModeOfPayment_Columns.COLUMN_ID);
		modeOfPaymentProjectionMap.put(DBConstant.ModeOfPayment_Columns.COLUMN_NAME, DBConstant.ModeOfPayment_Columns.COLUMN_NAME);
		modeOfPaymentProjectionMap.put(DBConstant.ModeOfPayment_Columns.COLUMN_SYNC_STATUS, DBConstant.ModeOfPayment_Columns.COLUMN_SYNC_STATUS);

		bankProjectionMap = new HashMap<String, String>();
		bankProjectionMap.put(DBConstant.Bank_Columns.COLUMN_ID, DBConstant.Bank_Columns.COLUMN_ID);
		bankProjectionMap.put(DBConstant.Bank_Columns.COLUMN_NAME, DBConstant.Bank_Columns.COLUMN_NAME);
		bankProjectionMap.put(DBConstant.Bank_Columns.COLUMN_SYNC_STATUS, DBConstant.Bank_Columns.COLUMN_SYNC_STATUS);
		
		typesProjectionMap = new HashMap<String, String>();
		typesProjectionMap.put(DBConstant.Types_Columns.COLUMN_ID, DBConstant.Types_Columns.COLUMN_ID);
		typesProjectionMap.put(DBConstant.Types_Columns.COLUMN_NAME, DBConstant.Types_Columns.COLUMN_NAME);
		typesProjectionMap.put(DBConstant.Types_Columns.COLUMN_SYNC_STATUS, DBConstant.Types_Columns.COLUMN_SYNC_STATUS);

		expensesProjectionMap = new HashMap<String, String>();
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_ID, DBConstant.Expeses_Columns.COLUMN_ID);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_DATE, DBConstant.Expeses_Columns.COLUMN_DATE);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_AMOUNT, DBConstant.Expeses_Columns.COLUMN_AMOUNT);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_PAYMENT_MODE, DBConstant.Expeses_Columns.COLUMN_PAYMENT_MODE);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_DESCRIPTION, DBConstant.Expeses_Columns.COLUMN_DESCRIPTION);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_CATEGORY, DBConstant.Expeses_Columns.COLUMN_CATEGORY);
		expensesProjectionMap.put(DBConstant.Expeses_Columns.COLUMN_SYNC_STATUS, DBConstant.Expeses_Columns.COLUMN_SYNC_STATUS);
		

		expensesDetailsProjectionMap = new HashMap<String, String>();
		expensesDetailsProjectionMap.put(DBConstant.Expeses_Details_Columns.COLUMN_ID, DBConstant.Expeses_Details_Columns.COLUMN_ID);
		expensesDetailsProjectionMap.put(DBConstant.Expeses_Details_Columns.COLUMN_NAME, DBConstant.Expeses_Details_Columns.COLUMN_NAME);
		expensesDetailsProjectionMap.put(DBConstant.Expeses_Details_Columns.COLUMN_EXP_ID, DBConstant.Expeses_Details_Columns.COLUMN_EXP_ID);
		expensesDetailsProjectionMap.put(DBConstant.Expeses_Details_Columns.COLUMN_URL, DBConstant.Expeses_Details_Columns.COLUMN_URL);

		patientProjectionMap = new HashMap<String, String>();
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_ID, DBConstant.Patient_Columns.COLUMN_ID);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_CUSTOM_ID, DBConstant.Patient_Columns.COLUMN_CUSTOM_ID);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_NAME, DBConstant.Patient_Columns.COLUMN_NAME);
//		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_ADDRESS, DBConstant.Patient_Columns.COLUMN_ADDRESS);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_AGE, DBConstant.Patient_Columns.COLUMN_AGE);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_TOTALCOUNT, DBConstant.Patient_Columns.COLUMN_TOTALCOUNT);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_DIAGNOSIS, DBConstant.Patient_Columns.COLUMN_DIAGNOSIS);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_TYPE, DBConstant.Patient_Columns.COLUMN_TYPE);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_SERVICE_TYPE, DBConstant.Patient_Columns.COLUMN_SERVICE_TYPE);

		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_REF, DBConstant.Patient_Columns.COLUMN_REF);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_LOCATION, DBConstant.Patient_Columns.COLUMN_LOCATION);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_STARTTIME, DBConstant.Patient_Columns.COLUMN_STARTTIME);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_DURATION, DBConstant.Patient_Columns.COLUMN_DURATION);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_DATE, DBConstant.Patient_Columns.COLUMN_DATE);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_WARD, DBConstant.Patient_Columns.COLUMN_WARD);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_EMERGENCY, DBConstant.Patient_Columns.COLUMN_EMERGENCY);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_TEAM_MEMBER, DBConstant.Patient_Columns.COLUMN_TEAM_MEMBER);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_PROCEDURE, DBConstant.Patient_Columns.COLUMN_PROCEDURE);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_LEVEL, DBConstant.Patient_Columns.COLUMN_LEVEL);
//		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_FEES, DBConstant.Patient_Columns.COLUMN_FEES);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_NOTE, DBConstant.Patient_Columns.COLUMN_NOTE);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_SEX, DBConstant.Patient_Columns.COLUMN_SEX);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_SX_WATCH, DBConstant.Patient_Columns.COLUMN_SX_WATCH);
		patientProjectionMap.put(DBConstant.Patient_Columns.COLUMN_SYNC_STATUS, DBConstant.Patient_Columns.COLUMN_SYNC_STATUS);
		
		
		
		patientTempProjectionMap = new HashMap<String, String>();
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_ID, DBConstant.Patient_Temp_Columns.COLUMN_ID);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_CUSTOM_ID, DBConstant.Patient_Temp_Columns.COLUMN_CUSTOM_ID);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_NAME, DBConstant.Patient_Temp_Columns.COLUMN_NAME);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_AGE, DBConstant.Patient_Temp_Columns.COLUMN_AGE);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_DIAGNOSIS, DBConstant.Patient_Temp_Columns.COLUMN_DIAGNOSIS);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_TYPE, DBConstant.Patient_Temp_Columns.COLUMN_TYPE);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_SERVICE_TYPE, DBConstant.Patient_Temp_Columns.COLUMN_SERVICE_TYPE);//ADDED 10007

		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_REF, DBConstant.Patient_Temp_Columns.COLUMN_REF);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_LOCATION, DBConstant.Patient_Temp_Columns.COLUMN_LOCATION);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_DATE, DBConstant.Patient_Temp_Columns.COLUMN_DATE);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_EMERGENCY, DBConstant.Patient_Temp_Columns.COLUMN_EMERGENCY);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_NOTE, DBConstant.Patient_Temp_Columns.COLUMN_NOTE);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_SEX, DBConstant.Patient_Temp_Columns.COLUMN_SEX);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_STARTTIME, DBConstant.Patient_Temp_Columns.COLUMN_STARTTIME);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_PROCEDURE, DBConstant.Patient_Temp_Columns.COLUMN_PROCEDURE);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_TEAM_MEMBER, DBConstant.Patient_Temp_Columns.COLUMN_TEAM_MEMBER);

		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_LEVEL, DBConstant.Patient_Temp_Columns.COLUMN_LEVEL);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_DURATION, DBConstant.Patient_Temp_Columns.COLUMN_DURATION);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_SYNC_STATUS, DBConstant.Patient_Temp_Columns.COLUMN_SYNC_STATUS);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_WARD, DBConstant.Patient_Temp_Columns.COLUMN_WARD);
		patientTempProjectionMap.put(DBConstant.Patient_Temp_Columns.COLUMN_TOTALCOUNT, DBConstant.Patient_Temp_Columns.COLUMN_TOTALCOUNT);
		
		
		nameProjectionMap = new HashMap<String, String>();
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_ID, DBConstant.Patient_Name_Columns.COLUMN_ID);
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_NAME, DBConstant.Patient_Name_Columns.COLUMN_NAME);
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID, DBConstant.Patient_Name_Columns.COLUMN_CUSTOM_ID);
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_LOCATION, DBConstant.Patient_Name_Columns.COLUMN_LOCATION);
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_RED_ID, DBConstant.Patient_Name_Columns.COLUMN_RED_ID);
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_NAME_SEARCHALGO, DBConstant.Patient_Name_Columns.COLUMN_NAME_SEARCHALGO);//ADDED 10003
		nameProjectionMap.put(DBConstant.Patient_Name_Columns.COLUMN_DATE, DBConstant.Patient_Name_Columns.COLUMN_DATE);

		
		
		recordingProjectionMap = new HashMap<String, String>();
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_ID, DBConstant.Recoding_Columns.COLUMN_ID);
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_LOCATION, DBConstant.Recoding_Columns.COLUMN_LOCATION);
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_DATE, DBConstant.Recoding_Columns.COLUMN_DATE);
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_URL, DBConstant.Recoding_Columns.COLUMN_URL);
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_SYNCKEDSTATUS, DBConstant.Recoding_Columns.COLUMN_SYNCKEDSTATUS);
		recordingProjectionMap.put(DBConstant.Recoding_Columns.COLUMN_TYPE, DBConstant.Recoding_Columns.COLUMN_TYPE);
		
		//SA10002
		paymentProjectionMap = new HashMap<String, String>();
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_ID, DBConstant.Payment_Columns.COLUMN_ID);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_CUSTOM_ID, DBConstant.Payment_Columns.COLUMN_CUSTOM_ID);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_RECEIVED_DATE, DBConstant.Payment_Columns.COLUMN_RECEIVED_DATE);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_SERVICED_DATE, DBConstant.Payment_Columns.COLUMN_SERVICED_DATE);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_TOTALCOUNT, DBConstant.Payment_Columns.COLUMN_TOTALCOUNT);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_PAYMENT_SOURCE, DBConstant.Payment_Columns.COLUMN_PAYMENT_SOURCE);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_RECONCILE, DBConstant.Payment_Columns.COLUMN_RECONCILE);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_PAYMENT_MODE, DBConstant.Payment_Columns.COLUMN_PAYMENT_MODE);

		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_CHEQUE, DBConstant.Payment_Columns.COLUMN_CHEQUE);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_IN_HAND, DBConstant.Payment_Columns.COLUMN_IN_HAND);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_TDS_PER, DBConstant.Payment_Columns.COLUMN_TDS_PER);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_TDS_AMT, DBConstant.Payment_Columns.COLUMN_TDS_AMT);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_AMOUNT, DBConstant.Payment_Columns.COLUMN_AMOUNT);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_LOCATION, DBConstant.Payment_Columns.COLUMN_LOCATION);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_BANK, DBConstant.Payment_Columns.COLUMN_BANK);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_PY_WATCH, DBConstant.Payment_Columns.COLUMN_PY_WATCH);
		paymentProjectionMap.put(DBConstant.Payment_Columns.COLUMN_SYNC_STATUS, DBConstant.Payment_Columns.COLUMN_SYNC_STATUS);
		

		paymentTempProjectionMap = new HashMap<String, String>();
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_ID, DBConstant.Payment_Temp_Columns.COLUMN_ID);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_CUSTOM_ID, DBConstant.Payment_Temp_Columns.COLUMN_CUSTOM_ID);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_RECEIVED_DATE, DBConstant.Payment_Temp_Columns.COLUMN_RECEIVED_DATE);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_SERVICED_DATE, DBConstant.Payment_Temp_Columns.COLUMN_SERVICED_DATE);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_TOTALCOUNT, DBConstant.Payment_Temp_Columns.COLUMN_TOTALCOUNT);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_SOURCE, DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_SOURCE);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_RECONCILE, DBConstant.Payment_Temp_Columns.COLUMN_RECONCILE);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_MODE, DBConstant.Payment_Temp_Columns.COLUMN_PAYMENT_MODE);

		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_CHEQUE, DBConstant.Payment_Temp_Columns.COLUMN_CHEQUE);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_IN_HAND, DBConstant.Payment_Temp_Columns.COLUMN_IN_HAND);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_TDS_PER, DBConstant.Payment_Temp_Columns.COLUMN_TDS_PER);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_TDS_AMT, DBConstant.Payment_Temp_Columns.COLUMN_TDS_AMT);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_AMOUNT, DBConstant.Payment_Temp_Columns.COLUMN_AMOUNT);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_LOCATION, DBConstant.Payment_Temp_Columns.COLUMN_LOCATION);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_BANK, DBConstant.Payment_Temp_Columns.COLUMN_BANK);
		paymentTempProjectionMap.put(DBConstant.Payment_Temp_Columns.COLUMN_SYNC_STATUS, DBConstant.Payment_Temp_Columns.COLUMN_SYNC_STATUS);

		//EA10002
		
		//SA 10003
		depositedBankProjectionMap= new HashMap<String, String>();
		depositedBankProjectionMap.put(DBConstant.Deposited_Bank_Columns.COLUMN_ID, DBConstant.Deposited_Bank_Columns.COLUMN_ID);
		depositedBankProjectionMap.put(DBConstant.Deposited_Bank_Columns.COLUMN_NAME, DBConstant.Deposited_Bank_Columns.COLUMN_NAME);
		depositedBankProjectionMap.put(DBConstant.Deposited_Bank_Columns.COLUMN_SYNC_STATUS, DBConstant.Deposited_Bank_Columns.COLUMN_SYNC_STATUS);
		//EA 10003

		//SA 10006
		patientDetailsProjectionMap = new HashMap<String, String>();
		patientDetailsProjectionMap.put(DBConstant.Patient_Details_Columns.COLUMN_ID, DBConstant.Patient_Details_Columns.COLUMN_ID);
		patientDetailsProjectionMap.put(DBConstant.Patient_Details_Columns.COLUMN_PATIENT_ID, DBConstant.Patient_Details_Columns.COLUMN_PATIENT_ID);
		patientDetailsProjectionMap.put(DBConstant.Patient_Details_Columns.COLUMN_PATIENT_TYPE, DBConstant.Patient_Details_Columns.COLUMN_PATIENT_TYPE);
		patientDetailsProjectionMap.put(DBConstant.Patient_Details_Columns.COLUMN_SYNC_STATUS, DBConstant.Patient_Details_Columns.COLUMN_SYNC_STATUS);
		patientDetailsProjectionMap.put(DBConstant.Patient_Details_Columns.COLUMN_URL, DBConstant.Patient_Details_Columns.COLUMN_URL);
		
		patientTempDetailsProjectionMap = new HashMap<String, String>();
		patientTempDetailsProjectionMap.put(DBConstant.Patient_Temp_Details_Columns.COLUMN_ID, DBConstant.Patient_Temp_Details_Columns.COLUMN_ID);
		patientTempDetailsProjectionMap.put(DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_ID, DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_ID);
		patientTempDetailsProjectionMap.put(DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_TYPE, DBConstant.Patient_Temp_Details_Columns.COLUMN_PATIENT_TYPE);
		patientTempDetailsProjectionMap.put(DBConstant.Patient_Temp_Details_Columns.COLUMN_SYNC_STATUS, DBConstant.Patient_Temp_Details_Columns.COLUMN_SYNC_STATUS);
		patientTempDetailsProjectionMap.put(DBConstant.Patient_Temp_Details_Columns.COLUMN_URL, DBConstant.Patient_Temp_Details_Columns.COLUMN_URL);
		//EA 10006
	}	
}
