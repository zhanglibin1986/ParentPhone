package parent.com.parentphone;

import android.view.View;

import parent.com.parentphone.bean.ContactBean;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:12
 * @Description
 */
public interface ContactsUiController {
    /**
     * Click the contact list item.
     * @param bean
     */
    public void onContactListItemClicked(ContactBean bean, View target);
}
