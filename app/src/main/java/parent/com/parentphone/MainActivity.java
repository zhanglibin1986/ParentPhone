package parent.com.parentphone;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ContactsActivityController mActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityController = createController();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityController.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityController.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityController.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityController.onDestroy();
    }

    private ContactsActivityController createController() {
        ContactsController controller = new ContactsController(this);
        return (ContactsActivityController) controller;
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

//    // This method uses APIs from newer OS versions than the minimum that this app supports. This
//    // annotation tells Android lint that they are properly guarded so they won't run on older OS
//    // versions and can be ignored by lint.
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mActivityController.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActivityController.onOptionsItemSelected(item);
    }
}

