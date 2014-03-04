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
package in.professionalacademyca.ca.sql;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NotificationSqlCursorAdapter extends SimpleCursorAdapter implements
		OnClickListener {

	private Context context;
	Typeface stylefont;

	private Cursor currentCursor;
	private SparseBooleanArray mSelectedItemsIds;//VIKALP
	
	private Uri uri;
	@SuppressWarnings("deprecation")
	public NotificationSqlCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, Uri uri) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		mSelectedItemsIds = new SparseBooleanArray();//VIKALP
		this.context = context;
		this.uri = uri;
		stylefont = Typeface.createFromAsset(CA.getApplication().getApplicationContext().getAssets(), AppConstants.fontStyle);

	}

	@Override
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.noti_item, null);
		}

		this.currentCursor.moveToPosition(pos);

		long id = getCursor().getLong(getCursor().getColumnIndex(DBConstant.Notification_Columnns.COLUMN_ID));

		TextView txtQuery = (TextView) v.findViewById(R.id.question);
		
		Button notiBox = (Button) v.findViewById(R.id.box);
		notiBox.setTypeface(stylefont);
		
		txtQuery.setText(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Notification_Columnns.COLUMN_TITLE)));
		txtQuery.setTypeface(stylefont);

		TextView txtAnswer = (TextView) v.findViewById(R.id.answer);
		txtAnswer.setText(
				this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE)).equalsIgnoreCase("0") 
				? "" :
				this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Notification_Columnns.COLUMN_NOTIFICATION_DATE))		
				);
		txtAnswer.setTypeface(stylefont);
		
		v.setTag(String.valueOf(id));
		notiBox.setBackgroundColor(
				this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Notification_Columnns.COLUMN_BATCH)).equalsIgnoreCase("general") 
				? Color.GRAY :
					Color.parseColor("#E18700")
				);
		
		notiBox.setText(
				this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Notification_Columnns.COLUMN_BATCH)).equalsIgnoreCase("general") 
				? "G" :
					"B"
				);
//		Button removeLOV = (Button) v.findViewById(R.id.btnRemoveLOV);
//		removeLOV.setOnClickListener(this);
//		removeLOV.setTag(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Location_Columns.COLUMN_ID)));
		return (v);
	}

	@SuppressWarnings("deprecation")
	public void ClearSelections() {
		this.currentCursor.requery();

	}

	@Override
	public void onClick(View v) {
		Button b = (Button) v;
		String _id = (String) b.getTag();
		this.context.getContentResolver().delete(this.uri, "_id=?", new String[] { _id });
	}
}
