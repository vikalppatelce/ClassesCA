/*HISTORY
* CATEGORY 			:- 
* DEVELOPER			:- VIKALP PATEL
* AIM 				:- 
* DESCRIPTION 		:- 
* 
* S - START E- END C- COMMENTED U -EDITED A -ADDED
* --------------------------------------------------------------------------------------------------------------------
* INDEX 	DEVELOPER 		DATE 		FUNCTION		DESCRIPTION
* --------------------------------------------------------------------------------------------------------------------
* --------------------------------------------------------------------------------------------------------------------
*/

package in.professionalacademyca.ca.dto;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {

	public static final String DB_NAME = "PatientDB";
	
	public static final String TABLE_LOCATIO 					= "location";
	public static final String TABLE_PROCEDURE 					= "procedure";
	public static final String TABLE_WARD	 					= "ward";
	public static final String TABLE_TMEMBER   					= "tMember";
	public static final String TABLE_REF 						= "ref";
	public static final String TABLE_STARTTIME 					= "startTime";
	public static final String TABLE_LEVEL 						= "level";
	public static final String TABLE_MODEOFPAYMENT 				= "modeOfPayment";
	public static final String TABLE_BANK 						= "bank";
	public static final String TABLE_T 						    = "patientt";
	
	
	public static final String TABLE_EXPENSES 					= "expeses";
	public static final String TABLE_EXPENSES_DETAILS 			= "expesesDetails";
	
	public static final String TABLE_PATIENT 					= "patient";
	public static final String TABLE_TEMP 						= "patienTemp";
	public static final String TABLE_NAME 						= "patienName";
	public static final String TABLE_RECORDING					= "recording";
	
	public static final String TABLE_PAYMENT                    = "payment"; //SA10001
	public static final String TABLE_PAYMENT_TEMP               = "paymentTemp";//EA10001
	public static final String TABLE_DEPOSITED_BANK             = "depositedBank";//ADDED 10002
	public static final String TABLE_PATIENT_DETAILS 			= "patientDetails";//SA 10003
	public static final String TABLE_TEMP_DETAILS 				= "patientTempDetails";//EA 10003
	
	
	public static class Location_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/location");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/location";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Procedure_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/procedure");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/procedure";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Ward_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/ward");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/ward";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class TMEMBER_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/tMember");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tMember";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Ref_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/ref");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/ref";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class StartTime_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/startTime");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/startTime";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Level_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/level");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/level";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class ModeOfPayment_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/modeOfPayment");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/modeOfPayment";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Bank_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/bank");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bank";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	public static class Types_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patientt");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patientt";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	
	public static class Expeses_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/expeses");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/expeses";

		public static final String COLUMN_ID 				= "_id";
		public static final String COLUMN_DATE 				= "date";
		public static final String COLUMN_AMOUNT 			= "amount";
		public static final String COLUMN_PAYMENT_MODE 		= "paument_mode";
		public static final String COLUMN_DESCRIPTION 		= "description";
		public static final String COLUMN_CATEGORY 			= "category";
		public static final String COLUMN_SYNC_STATUS = "status";
	}
	
	public static class Expeses_Details_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/expesesDetails");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/expesesDetails";

		public static final String COLUMN_ID 					= "_id";
		public static final String COLUMN_NAME 					= "name";
		public static final String COLUMN_EXP_ID				= "exp_id";
		public static final String COLUMN_URL 					= "url";
		
	}
	
	public static class Recoding_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/recording");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recording";

		public static final String COLUMN_ID 					= "_id";
		public static final String COLUMN_TYPE					= "typ"; //0-sx, 1-ipd, 2-opd
		public static final String COLUMN_LOCATION				= "location";
		public static final String COLUMN_DATE					= "date";
		public static final String COLUMN_URL 					= "url";
		public static final String COLUMN_SYNCKEDSTATUS 		= "status";
		
	}
	
	public static final Uri DISTINCT_CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patient");
	public static class Patient_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patient");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patient";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_CUSTOM_ID 					= "_newId";
		public static final String COLUMN_NAME							= "name";
	//	public static final String COLUMN_ADDRESS 						= "address";
		public static final String COLUMN_AGE 							= "age";
		public static final String COLUMN_TOTALCOUNT 					= "totalCount";
		public static final String COLUMN_DIAGNOSIS 					= "diagnosis";
		public static final String COLUMN_TYPE 							= "type";
		public static final String COLUMN_SERVICE_TYPE 					= "service_type";
		public static final String COLUMN_REF 							= "ref";
		public static final String COLUMN_LOCATION 						= "location";
		public static final String COLUMN_STARTTIME						= "startTime";
		public static final String COLUMN_DURATION	 					= "time_spent";
		public static final String COLUMN_DATE 							= "date";
		public static final String COLUMN_WARD							= "ward";
		public static final String COLUMN_EMERGENCY 					= "emergency";
		public static final String COLUMN_TEAM_MEMBER 					= "teamMember";
		public static final String COLUMN_PROCEDURE 					= "procedure";
		public static final String COLUMN_LEVEL		 					= "level";
		//public static final String COLUMN_FEES 							= "fees";
		public static final String COLUMN_NOTE 							= "note";
		public static final String COLUMN_SEX 							= "sex";
		public static final String COLUMN_SX_WATCH 						= "sx_watch";
		public static final String COLUMN_SYNC_STATUS = "status";
		
		
	}
	
	public static class Patient_Temp_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patienTemp");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patienTemp";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_CUSTOM_ID 					= "_newId";
		public static final String COLUMN_NAME							= "name";
		public static final String COLUMN_AGE 							= "age";
		public static final String COLUMN_DIAGNOSIS 					= "diagnosis";
		public static final String COLUMN_TYPE 							= "type";
		public static final String COLUMN_SERVICE_TYPE 					= "service_type";//ADDED 10005 
		public static final String COLUMN_REF 							= "ref";
		public static final String COLUMN_LOCATION 						= "location";
		public static final String COLUMN_DATE 							= "date";
		public static final String COLUMN_EMERGENCY 					= "emergency";
		public static final String COLUMN_NOTE 							= "note";
		public static final String COLUMN_SEX 							= "sex";
		
		
		public static final String COLUMN_STARTTIME						= "startTime";
		public static final String COLUMN_WARD							= "ward";
		public static final String COLUMN_TEAM_MEMBER 					= "teamMember";
		public static final String COLUMN_PROCEDURE 					= "procedure";
		public static final String COLUMN_LEVEL		 					= "level";
		public static final String COLUMN_DURATION	 					= "time_spent";
		public static final String COLUMN_TOTALCOUNT 					= "totalCount";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	
	public static class Patient_Name_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patienName");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patienName";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_NAME							= "name";
		public static final String COLUMN_CUSTOM_ID 					= "_newId";
		public static final String COLUMN_DATE 							= "date";
		public static final String COLUMN_LOCATION 						= "location";
		public static final String COLUMN_RED_ID 						= "ref";
		public static final String COLUMN_NAME_SEARCHALGO 				= "name_search_algo";//ADDED 10003
	}
	
	//SA10001
	//public static final Uri DISTINCT_CONTENT_URI_PAYMENT = Uri.parse("content://"+ PatientDB.AUTHORITY + "/payment");
	public static class Payment_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/payment");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/payment";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_CUSTOM_ID 					= "imei";
		public static final String COLUMN_RECEIVED_DATE					= "received_date";
		public static final String COLUMN_SERVICED_DATE					= "serviced_date";
		public static final String COLUMN_PAYMENT_SOURCE				= "payment_src";
		public static final String COLUMN_RECONCILE						= "reconcile";
		public static final String COLUMN_PAYMENT_MODE					= "payment_mode";
		public static final String COLUMN_CHEQUE 						= "cheque";
		public static final String COLUMN_IN_HAND 						= "inhand";
		public static final String COLUMN_TDS_PER	 					= "tds_per";
		public static final String COLUMN_TDS_AMT 						= "tds_amt";
		public static final String COLUMN_AMOUNT 						= "amount";
		
		
		public static final String COLUMN_LOCATION	 					= "location";
		public static final String COLUMN_BANK		 					= "bank";
		public static final String COLUMN_TOTALCOUNT 					= "totalCount";
		public static final String COLUMN_PY_WATCH                      = "py_watch";
		public static final String COLUMN_SYNC_STATUS = "status";
		
	}
	
	public static class Payment_Temp_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/paymentTemp");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/paymentTemp";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_CUSTOM_ID 					= "imei";
		public static final String COLUMN_RECEIVED_DATE					= "received_date";
		public static final String COLUMN_SERVICED_DATE					= "serviced_date";
		public static final String COLUMN_PAYMENT_SOURCE				= "payment_src";
		public static final String COLUMN_RECONCILE						= "reconcile";
		public static final String COLUMN_PAYMENT_MODE					= "payment_mode";
		public static final String COLUMN_CHEQUE 						= "cheque";
		public static final String COLUMN_IN_HAND 						= "inhand";
		public static final String COLUMN_TDS_PER	 					= "tds_per";
		public static final String COLUMN_TDS_AMT 						= "tds_amt";
		public static final String COLUMN_AMOUNT 						= "amount";
		
		
		public static final String COLUMN_LOCATION	 					= "location";
		public static final String COLUMN_BANK		 					= "bank";
		public static final String COLUMN_TOTALCOUNT 					= "totalCount";
		public static final String COLUMN_SYNC_STATUS = "status";
			
	}
	//EA10001
	
	//SA10002
	public static class Deposited_Bank_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/depositedBank");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/depositedBank";
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SYNC_STATUS = "status";
	}
	//EA10002
	
	//SA 10003 - PATIENT DETAILS COLUMN - ONE WITH PICTURES IN IT
	public static class Patient_Details_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patientDetails");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patientDetails";

		public static final String COLUMN_ID 					= "_id";
		public static final String COLUMN_PATIENT_ID    		= "patient_id";
		public static final String COLUMN_PATIENT_TYPE 			= "patient_type";
		public static final String COLUMN_URL 					= "url";
		public static final String COLUMN_SYNC_STATUS 			= "status";
	}
	public static class Patient_Temp_Details_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ CaDB.AUTHORITY + "/patientTempDetails");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/patientTempDetails";

		public static final String COLUMN_ID 					= "_id";
		public static final String COLUMN_PATIENT_ID    		= "patient_id";
		public static final String COLUMN_PATIENT_TYPE 			= "patient_type";
		public static final String COLUMN_URL 					= "url";
		public static final String COLUMN_SYNC_STATUS 			= "status";
	}
	//EA 10003
}
