package parent.com.parentphone;

import android.app.Activity;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.QuickContactBadge;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:10
 * @Description
 */
public class ContactsController implements ContactsActivityController, ContactsUiController, ContactsModel.QueryContactsCallback{
    private Activity mActivity;
    private ContactsUi mUi;
    private ContactsModel mModel;

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

    public ContactsController(Activity activity) {
        mActivity = activity;
        mUi = new ContactsUi(mActivity, this);
        mModel = new ContactsModel(mActivity);
        mModel.requestContactsBeans(this);
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
}
