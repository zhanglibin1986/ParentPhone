package parent.com.parentphone;

import android.view.Menu;
import android.view.MenuItem;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:12
 * @Description
 */
public interface ContactsActivityController {
    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public boolean onCreateOptionsMenu(Menu menu);

    public boolean onOptionsItemSelected(MenuItem item);
}
