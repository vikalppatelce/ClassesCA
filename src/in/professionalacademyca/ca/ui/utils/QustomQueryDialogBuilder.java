package in.professionalacademyca.ca.ui.utils;

import in.professionalacademyca.ca.R;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class QustomQueryDialogBuilder extends AlertDialog.Builder{

	/** The custom_body layout */
	private View mDialogView;

	/** optional dialog title layout */
	private TextView mTitle;
	/** optional alert dialog image */
	private ImageView mIcon;
	/** optional message displayed below title if title exists*/
	private TextView mMessage;
	
	TextView mAnswer;
	/** The colored holo divider. You can set its color with the setDividerColor method */
	private View mDivider;

    public QustomQueryDialogBuilder(Context context) {
        super(context);

        mDialogView = View.inflate(context, R.layout.qustom_dialog_query_layout, null);
        setView(mDialogView);

        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.message);
        mAnswer = (TextView) mDialogView.findViewById(R.id.answer);
        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
	}

    /** 
     * Use this method to color the divider between the title and content.
     * Will not display if no title is set.
     * 
     * @param colorString for passing "#ffffff"
     */
    public QustomQueryDialogBuilder setDividerColor(String colorString) {
    	mDivider.setBackgroundColor(Color.parseColor(colorString));
    	return this;
    }
 
    @Override
    public QustomQueryDialogBuilder setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }
    
    public QustomQueryDialogBuilder setFontTitle(Typeface style) {
        mTitle.setTypeface(style);
        return this;
    }

    public QustomQueryDialogBuilder setTitleColor(String colorString) {
    	mTitle.setTextColor(Color.parseColor(colorString));
    	return this;
    }

    @Override
    public QustomQueryDialogBuilder setMessage(int textResId) {
        mMessage.setText(textResId);
        return this;
    }

    @Override
    public QustomQueryDialogBuilder setMessage(CharSequence text) {
        mMessage.setText(text);
        return this;
    }
    
    public QustomQueryDialogBuilder setFontMessage(Typeface style) {
        mMessage.setTypeface(style);
        return this;
    }
    
    public QustomQueryDialogBuilder setMessageColor(String colorString) {
    	mMessage.setTextColor(Color.parseColor(colorString));
    	return this;
    }
    
    public QustomQueryDialogBuilder setAnswer(int textResId) {
        mAnswer.setText(textResId);
        return this;
    }

    public QustomQueryDialogBuilder setAnswer(CharSequence text) {
        mAnswer.setText(text);
        return this;
    }
    
    public QustomQueryDialogBuilder setFontAnswer(Typeface style) {
        mAnswer.setTypeface(style);
        return this;
    }
    
    public QustomQueryDialogBuilder setAnswerColor(String colorString) {
    	mAnswer.setTextColor(Color.parseColor(colorString));
    	return this;
    }

    @Override
    public QustomQueryDialogBuilder setIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    @Override
    public QustomQueryDialogBuilder setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }
    
    /**
     * This allows you to specify a custom layout for the area below the title divider bar
     * in the dialog. As an example you can look at example_ip_address_layout.xml and how
     * I added it in TestDialogActivity.java
     * 
     * @param resId  of the layout you would like to add
     * @param context
     */
    public QustomQueryDialogBuilder setCustomView(int resId, Context context) {
    	View customView = View.inflate(context, resId, null);
    	((FrameLayout)mDialogView.findViewById(R.id.customPanel)).addView(customView);
    	return this;
    }
    
    @Override
    public AlertDialog show() {
    	if (mTitle.getText().equals("")) mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
    	return super.show();
    }

}