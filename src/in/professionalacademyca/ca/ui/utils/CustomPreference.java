package in.professionalacademyca.ca.ui.utils;

import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import android.content.Context;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomPreference extends Preference {
	Typeface fontStyle;

	public CustomPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public CustomPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomPreference(Context context) {
		super(context);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		
		fontStyle = Typeface.createFromAsset(CA.getApplication().getApplicationContext().getAssets(), AppConstants.fontStyle);
		TextView titleView = (TextView) view.findViewById(android.R.id.title);
		TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
		titleView.setTypeface(fontStyle);
		summaryView.setTypeface(fontStyle);
		
//		titleView.setTextColor(Color.RED);
	}

}
