package parent.com.parentphone;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.parentphone
 * @date 15/10/21下午8:39
 * @Description
 */
public class ContactsListData{
    private List<ContactBean> data;

    private ContactsModel.ContactsOption option;

    public List<ContactBean> getData() {
        return data;
    }

    public void setData(List<ContactBean> data) {
        this.data = data;
    }

    public ContactsModel.ContactsOption getOption() {
        return option;
    }

    public void setOption(ContactsModel.ContactsOption option) {
        this.option = option;
    }
}
