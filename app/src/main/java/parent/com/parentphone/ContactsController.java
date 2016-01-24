package parent.com.parentphone;

import android.app.Activity;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:10
 * @Description
 */
public class ContactsController implements ContactsActivityController, ContactsUiController, ContactsModel.QueryContactsCallback {
    private AppCompatActivity mActivity;
    private ContactsUi mUi;
    private ContactsModel mModel;

    public ContactsController(AppCompatActivity activity) {
        mActivity = activity;
        mUi = new ContactsUi(mActivity, this);
        mModel = new ContactsModel(mActivity);
        ContactsModel.ContactsOption option = ContactsModel.ContactsOption.getInstance();
        option.isOnlyContactWithPhoto = true;
        mModel.requestContactsBeans(this, option);
    }

    @Override
    public void onContactListItemClicked(ContactBean bean, View target) {
        final Uri contactUri = ContactsContract.Contacts.getLookupUri(
                Long.valueOf(bean.getId()),
                bean.getLookupKey());
        if (contactUri != null) {
            ContactsContract.QuickContact.showQuickContact(mActivity, target, contactUri,
                    ContactsContract.QuickContact.MODE_LARGE, null);
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResult(List<ContactBean> contactBeans) {
        mUi.setDataAndShow(contactBeans);
    }

    @Override
    public void onLoading(String msg) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(R.menu.main_menu, menu);
        // Need to use MenuItemCompat methods to call any action item related methods
//        MenuItemCompat.setShowAsAction(locationItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_all:
//
//                break;
//            case R.id.menu_has_photo:
//
//                break;
            case R.id.menu_settings:
                Toast.makeText(mActivity, "To be continue", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void showAll() {

    }

    private void showOnlyPhoto() {

    }
}
