package in.professionalacademyca.ca.ui.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static TestFragment newInstance(int content) {
        TestFragment fragment = new TestFragment();
        fragment.mContent = content;
        return fragment;
    }

    private int mContent = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView text = new ImageView(getActivity());
        text.setImageResource(mContent);
        text.setScaleType(ScaleType.FIT_XY);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, mContent);
    }
}
