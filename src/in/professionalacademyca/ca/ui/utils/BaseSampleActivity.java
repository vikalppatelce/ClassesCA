package in.professionalacademyca.ca.ui.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.PageIndicator;

public abstract class BaseSampleActivity extends FragmentActivity {

    public TestFragmentAdapter mAdapter;
    public ViewPager mPager;
    public PageIndicator mIndicator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
