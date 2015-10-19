package parent.com.parentphone;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
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
}

