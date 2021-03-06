package parent.com.parentphone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.LogUtil;
import parent.com.parentphone.util.Utils;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:11
 * @Description
 */
public class ContactsModel implements LoaderManager.LoaderCallbacks<Cursor> {
    // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
    // sort key allows for localization. In earlier versions. use the display name as the sort
    // key.
    @SuppressLint("InlinedApi")
    final static String SORT_ORDER =
            Utils.hasHoneycomb() ? Contacts.SORT_KEY_PRIMARY : Contacts.DISPLAY_NAME;
    // The selection clause for the CursorLoader query. The search criteria defined here
    // restrict results to contacts that have a display name and are linked to visible groups.
    // Notice that the search on the string provided by the user is implemented by appending
    // the search string to CONTENT_FILTER_URI.
    @SuppressLint("InlinedApi")
    final static String SELECTION =
            (Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME) +
                    "<>''" + " AND " + Contacts.IN_VISIBLE_GROUP + "=1";

    final static String SELECTION_NO_PHOTO = (Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME) +
            "<>''" + " AND " + Contacts.IN_VISIBLE_GROUP + "=1" + " AND " + Contacts.PHOTO_THUMBNAIL_URI + " NOT NULL";

    // The projection for the CursorLoader query. This is a list of columns that the Contacts
    // Provider should return in the Cursor.

    final static String BUNDLER_KEY_OPTION = "option";

    @SuppressLint("InlinedApi")
    final static String[] PROJECTION = {

            Contacts._ID,

            // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
            // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
            // a "permanent" contact URI.
            Contacts.LOOKUP_KEY,

            // In platform version 3.0 and later, the Contacts table contains
            // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
            // some other useful identifier such as an email address. This column isn't
            // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
            // instead.
            Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME,

            // In Android 3.0 and later, the thumbnail image is pointed to by
            // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
            // you generate the pointer from the contact's ID value and constants defined in
            // android.provider.ContactsContract.Contacts.
            Utils.hasHoneycomb() ? Contacts.PHOTO_THUMBNAIL_URI : Contacts._ID,

            // The sort order column for the returned Cursor, used by the AlphabetIndexer
            SORT_ORDER,
    };
    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    Contacts.DISPLAY_NAME_PRIMARY :
                    Contacts.DISPLAY_NAME
    };
    private Activity mActivity;
    private QueryContactsCallback mCallback;
    private ContactsListData mListData;
    private boolean isLoadding;//是否正在请求数据

    public ContactsModel(Activity activity) {
        this.mActivity = activity;
        mListData = new ContactsListData();
    }

    public void requestContactsBeans(QueryContactsCallback callback) {
        requestContactsBeans(callback, null);
    }

    /**
     * 很重要的一个方法，获取联系人
     *
     * @param callback
     */
    public void requestContactsBeans(QueryContactsCallback callback, ContactsOption option) {
        mCallback = callback;
        if(option == null) {
            option = ContactsOption.getInstance();
        }
        //如果上次请求和本次请求参数相同，且有缓存数据的情况，则使用缓存数据，不再请求
        if (mListData != null && mListData.getData() != null && mListData.getOption().equals(option)) {
            callback.onResult(mListData.getData());
        } else {
            Bundle bundle = null;
            if(!option.isEmpty()) {
                bundle = new Bundle();
                bundle.putSerializable(BUNDLER_KEY_OPTION, option);
            }

            if(mListData.getData() != null && !mListData.getData().isEmpty()) {//restart loader.
                LogUtil.logM("restartLoader");
                mActivity.getLoaderManager().restartLoader(0, bundle, this);
            }
            if (!isLoadding) {
                LogUtil.logM("initLoader");
                mActivity.getLoaderManager().initLoader(0, bundle, this);
            }
        }
    }

    /**
     *
     */
    public static final class ContactsOption implements Serializable {
        public static ContactsOption getInstance() {
            return new ContactsOption();
        }

        /**
         * 是否只请求有头像的数据
         */
        public boolean isOnlyContactWithPhoto = false;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ContactsOption that = (ContactsOption) o;
            return Objects.equals(isOnlyContactWithPhoto, that.isOnlyContactWithPhoto);
        }

        /**
         * 判断option是否有数据
         * @return
         */
        public boolean isEmpty() {
            return !isOnlyContactWithPhoto;
        }

        @Override
        public int hashCode() {
            return Objects.hash(isOnlyContactWithPhoto);
        }
    }



    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        LogUtil.logM("-----onCreateLoader args = " + args);
        isLoadding = true;
        if(mCallback != null) {
            mCallback.onLoading("");
        }
        String selection = null;
        if(args == null) {
            selection = SELECTION;
        } else {
            if(args.getSerializable(BUNDLER_KEY_OPTION) != null) {
                ContactsOption option = (ContactsOption) args.getSerializable(BUNDLER_KEY_OPTION);
                selection = option.isOnlyContactWithPhoto ? SELECTION_NO_PHOTO : SELECTION;
            } else {
                selection = SELECTION;
            }
        }

        return new CursorLoader(mActivity, Contacts.CONTENT_URI, PROJECTION, selection, null, SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        LogUtil.logM("-----onLoadFinished");
        if(cursor != null) {
            LogUtil.logM("cursor count = " + cursor.getCount());
        }
        isLoadding = false;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (mListData.getData() == null) {
                mListData.setData(new ArrayList<ContactBean>());
            }
            while (!cursor.isAfterLast()) {
                final String photoUri = cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));
                final String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));
                final String lookupKey = cursor.getString(cursor.getColumnIndex(Contacts.LOOKUP_KEY));
                final String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
                ContactBean bean = new ContactBean();
                bean.setName(name);
                bean.setPhoto(photoUri);
                bean.setLookupKey(lookupKey);
                bean.setId(id);
                LogUtil.logM("photoUri = " + photoUri);
                LogUtil.logM("id = " + id + " , name = " + name + " , lookupKey = " + lookupKey);
                mListData.getData().add(bean);
                cursor.moveToNext();
            }
            if (mCallback != null) {
                mCallback.onResult(mListData.getData());
            }
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
    }

    public interface QueryContactsCallback {
        /**
         * request data result callback
         * @param contactBeans
         */
        public void onResult(List<ContactBean> contactBeans);

        /**
         * loading data
         * @param msg
         */
        public void onLoading(String msg);
    }
}
