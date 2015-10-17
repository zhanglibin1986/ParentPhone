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

import java.util.ArrayList;
import java.util.List;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.Utils;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.com.parent.parentphone
 * @date 15/10/4上午10:11
 * @Description
 */
public class ContactsModel implements LoaderManager.LoaderCallbacks<Cursor> {
    private Activity mActivity;
    private QueryContactsCallback mCallback;
    private List<ContactBean> mContactsBeans;
    private boolean isLoadding;
    // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
    // sort key allows for localization. In earlier versions. use the display name as the sort
    // key.
    @SuppressLint("InlinedApi")
    final static String SORT_ORDER =
            Utils.hasHoneycomb() ? Contacts.SORT_KEY_PRIMARY : Contacts.DISPLAY_NAME;

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

    // The selection clause for the CursorLoader query. The search criteria defined here
    // restrict results to contacts that have a display name and are linked to visible groups.
    // Notice that the search on the string provided by the user is implemented by appending
    // the search string to CONTENT_FILTER_URI.
    @SuppressLint("InlinedApi")
    final static String SELECTION =
            (Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME) +
                    "<>''" + " AND " + Contacts.IN_VISIBLE_GROUP + "=1";

    // The projection for the CursorLoader query. This is a list of columns that the Contacts
    // Provider should return in the Cursor.
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

    public ContactsModel(Activity activity) {
        this.mActivity = activity;

    }

    /**
     * 很重要的一个方法，获取联系人
     * @param callback
     */
    public void requestContactsBeans(QueryContactsCallback callback) {
        mCallback = callback;
        if(mContactsBeans != null) {
            callback.onResult(mContactsBeans);
        } else {
            if(!isLoadding) {
                mActivity.getLoaderManager().initLoader(0, null, this);
            } else {
                callback.onLoading("正在加载");
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        isLoadding = true;
        return new CursorLoader(mActivity, Contacts.CONTENT_URI, PROJECTION, SELECTION, null, SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        isLoadding = false;
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if(mContactsBeans == null) {
                mContactsBeans = new ArrayList<>();
            }
            while(!cursor.isAfterLast()) {
                final String photoUri = cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));
                final String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));
                ContactBean bean = new ContactBean();
                bean.setName(name);
                bean.setPhoto(photoUri);
                mContactsBeans.add(bean);
                cursor.moveToNext();
            }
            if(mCallback != null) {
                mCallback.onResult(mContactsBeans);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
    }

    public interface QueryContactsCallback {
        public void onResult(List<ContactBean> contactBeans);
        public void onLoading(String msg);
    }
}
