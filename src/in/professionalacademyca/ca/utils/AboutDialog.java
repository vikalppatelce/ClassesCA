/*
 * (c) 2012 Martin van Zuilekom (http://martin.cubeactive.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package in.professionalacademyca.ca.utils;

import in.professionalacademyca.ca.R;
import in.professionalacademyca.ca.app.AppConstants;
import in.professionalacademyca.ca.app.CA;
import in.professionalacademyca.ca.ui.utils.QustomDialogBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * Class to show a change log dialog
 */
public class AboutDialog {
    private static final String TAG = "AboutDialog";

    private final Context mContext;
    
    private String mStyle = "h1 { margin-left: 0px; font-size: 12pt; }"
            + "li { margin-left: 0px; font-size: 9pt; }"
            + "ul { padding-left: 30px; }"
            + ".summary { font-size: 9pt; color: #606060; display: block; clear: left; }"
            + ".date { font-size: 9pt; color: #606060;  display: block; }";


    protected DialogInterface.OnDismissListener mOnDismissListener;

    public AboutDialog(final Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    //Get the current app version
    private String getAppVersion() {
        String versionName = "";
        try {
            final PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return versionName;
    }

    //Parse a date string from the xml and format it using the local date format
    @SuppressLint("SimpleDateFormat")
    private String parseDate(final String dateString) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            final Date parsedDate = dateFormat.parse(dateString);
            return DateFormat.getDateFormat(getContext()).format(parsedDate);
        } catch (ParseException ignored) {
            //If there is a problem parsing the date just return the original string
            return dateString;
        }
    }

    //Parse a the release tag and appends it to the changelog builder
    private void parseReleaseTag(final StringBuilder changelogBuilder, final XmlPullParser resourceParser) throws XmlPullParserException, IOException {
        if (resourceParser.getAttributeValue(null, "summary") != null) {
            changelogBuilder.append("").append(resourceParser.getAttributeValue(null, "summary")).append("");
        }
    }

    public void setStyle(final String style) {
        mStyle = style;
    }

    public AboutDialog setOnDismissListener(final DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    //Get the changelog in html code, this will be shown in the dialog's webview
    private String getHTMLChangelog(final int resourceId, final Resources resources, final int version) {
        boolean releaseFound = false;
        final StringBuilder changelogBuilder = new StringBuilder();
        final XmlResourceParser xml = resources.getXml(resourceId);
        try {
            int eventType = xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && (xml.getName().equals("release"))) {
                    //Check if the version matches the release tag.
                    //When version is 0 every release tag is parsed.
                    final int versioncode = Integer.parseInt(xml.getAttributeValue(null, "versioncode"));
                    if ((version == 0) || (versioncode == version)) {
                        parseReleaseTag(changelogBuilder, xml);
                        releaseFound = true; //At lease one release tag has been parsed.
                    }
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        } finally {
            xml.close();
        }

        //Check if there was a release tag parsed, if not return an empty string.
        if (releaseFound) {
            return changelogBuilder.toString();
        } else {
            return "";
        }
    }

    //Returns change log in HTML format 
    public String getHTML() {
    	//TODO: Remove duplicate code with the method show()
        //Get resources
        final String packageName = mContext.getPackageName();
        final Resources resources;
        try {
            resources = mContext.getPackageManager().getResourcesForApplication(packageName);
        } catch (NameNotFoundException ignored) {
            return "";
        }

        //Create HTML change log
        return getHTMLChangelog(R.xml.aboutlog, resources, 0);    	
    }
//    protected void show(final int version) {
    	public void show(Context c) {
        //Get resources
        final String packageName = mContext.getPackageName();
        final Resources resources;
        try {
            resources = mContext.getPackageManager().getResourcesForApplication(packageName);
        } catch (NameNotFoundException ignored) {
            return;
        }

        //Get dialog title	        	
        String title = resources.getString(R.string.about_summary);
        title = String.format("%s v%s", title, getAppVersion());

        //Create html change log
        final String htmlChangelog = getHTMLChangelog(R.xml.aboutlog, resources, 0);

        //Check for empty change log
        if (htmlChangelog.length() == 0) {
            //It seems like there is nothing to show, just bail out.
            return;
        }
        /*
         * CUSTOM DIALOG
         */
        Typeface stylefont = Typeface.createFromAsset(CA.getApplication().getApplicationContext().getAssets(), AppConstants.fontStyle);
		final String HALLOWEEN_RED = "#B40404";
		QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(c).
				setTitle(title).
				setTitleColor(HALLOWEEN_RED).
				setDividerColor(HALLOWEEN_RED).
				setMessage(htmlChangelog).
				setFontTitle(stylefont).
				setFontSize(12f).
				setFontMessage(stylefont);
		qustomDialogBuilder.show();
        
    }
}

