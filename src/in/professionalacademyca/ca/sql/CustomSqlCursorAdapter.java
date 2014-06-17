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
 * B0001      VIKALP PATEL   17/06/2014           BUG           SWAPPING OF ANSWER IN WRONG QUERY
 * --------------------------------------------------------------------------------------------------------------------
 */
package in.professionalacademyca.ca.sql;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomSqlCursorAdapter extends SimpleCursorAdapter implements
		OnClickListener {

	private Context context;
	Typeface stylefont;

	private Cursor currentCursor;
	
	private Uri uri;
	@SuppressWarnings("deprecation")
	public CustomSqlCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, Uri uri) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		this.context = context;
		this.uri = uri;
		stylefont = Typeface.createFromAsset(CA.getApplication().getApplicationContext().getAssets(), AppConstants.fontStyle);

	}

	@Override
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.post_item, null);
		}

		this.currentCursor.moveToPosition(pos);

		long id = getCursor().getLong(getCursor().getColumnIndex(DBConstant.Query_Columns.COLUMN_ID));
		TextView txtQuery = (TextView) v.findViewById(R.id.question);
		
		TextView txtQueryDate = (TextView) v.findViewById(R.id.questiondate);
		TextView txtAnswerDate = (TextView) v.findViewById(R.id.answerdate);
		
		txtQuery.setText(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY)));
		txtQuery.setTypeface(stylefont);

		TextView txtAnswer = (TextView) v.findViewById(R.id.answer);
		
		txtAnswer.setVisibility(View.GONE); //SA B0001
		txtAnswerDate.setVisibility(View.GONE); //EA B0001
		
		if(!this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE)).equalsIgnoreCase("0"))
		{
			txtAnswer.setVisibility(View.VISIBLE);
			txtAnswer.setText(
					this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE)).equalsIgnoreCase("0") 
					? "" :
					this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE))		
					);
		}
		
		
		txtAnswer.setTypeface(stylefont);
		
		
		txtQueryDate.setText(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_QUERY_DATE)));
		
		if(!TextUtils.isEmpty(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE))))
		{
			txtAnswerDate.setVisibility(View.VISIBLE);
			txtAnswerDate.setText(this.currentCursor.getString(currentCursor.getColumnIndex(DBConstant.Query_Columns.COLUMN_RESPONSE_DATE)));
		}
		
		txtAnswerDate.setTypeface(stylefont);
		txtQueryDate.setTypeface(stylefont);
		
		v.setTag(String.valueOf(id));
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
