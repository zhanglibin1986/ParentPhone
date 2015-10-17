package parent.com.parentphone;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.ImageLoader;
import parent.com.parentphone.util.Utils;
import parent.com.parentphone.util.ViewHolder;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package PACKAGE_NAME
 * @date 15/9/20下午7:24
 * @Description
 */
public class ContactsUi {
    private Activity mActivity;
    private ContactsUiController mController;
    private View mRootView;//根布局
    private ListView mListView;
    private List<ContactBean> mData;
    private ImageLoader mImageLoader; // Handles loading the contact image in a background thread


    public ContactsUi(Activity activity, ContactsUiController controller) {
        this.mActivity = activity;
        this.mController = controller;
        mActivity.setContentView(R.layout.activity_main);
        findView();
        mImageLoader = new ImageLoader(mActivity, getListPreferredItemHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                // This gets called in a background thread and passed the data from
                // ImageLoader.loadImage().
                return loadContactPhotoThumbnail((String) data, getImageSize());
            }
        };

        // Set a placeholder loading image for the image loader
        mImageLoader.setLoadingImage(R.drawable.ic_contact_picture_holo_light);

        // Add a cache to the image loader
        mImageLoader.addImageCache(0.1f);
    }

    private int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        mActivity.getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new DisplayMetrics object
        final DisplayMetrics metrics = new android.util.DisplayMetrics();

        // Populate the DisplayMetrics
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    public void findView() {
        mRootView = mActivity.getWindow().getDecorView();
        mListView = (ListView) mRootView.findViewById(android.R.id.list);
    }

    private Bitmap loadContactPhotoThumbnail(String photoData, int imageSize) {

        // Ensures the Fragment is still added to an activity. As this method is called in a
        // background thread, there's the possibility the Fragment is no longer attached and
        // added to an activity. If so, no need to spend resources loading the contact photo.
        if (mActivity == null) {
            return null;
        }

        // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
        // ContentResolver can return an AssetFileDescriptor for the file.
        AssetFileDescriptor afd = null;

        // This "try" block catches an Exception if the file descriptor returned from the Contacts
        // Provider doesn't point to an existing file.
        try {
            Uri thumbUri;
            // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
            if (Utils.hasHoneycomb()) {
                thumbUri = Uri.parse(photoData);
            } else {
                // For versions prior to Android 3.0, appends the string argument to the content
                // Uri for the Contacts table.
                final Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, photoData);

                // Appends the content Uri for the Contacts.Photo table to the previously
                // constructed contact Uri to yield a content URI for the thumbnail image
                thumbUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            }
            // Retrieves a file descriptor from the Contacts Provider. To learn more about this
            // feature, read the reference documentation for
            // ContentResolver#openAssetFileDescriptor.
            afd = mActivity.getContentResolver().openAssetFileDescriptor(thumbUri, "r");

            // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
            // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
            FileDescriptor fileDescriptor = afd.getFileDescriptor();

            if (fileDescriptor != null) {
                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                // to the specified width and height
                return ImageLoader.decodeSampledBitmapFromDescriptor(
                        fileDescriptor, imageSize, imageSize);
            }
        } catch (FileNotFoundException e) {
            // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
            // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
            // FileNotFoundException.
            if (BuildConfig.DEBUG) {
                Log.d("TAG", "Contact photo thumbnail not found for contact " + photoData
                        + ": " + e.toString());
            }
        } finally {
            // If an AssetFileDescriptor was returned, try to close it
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    // Closing a file descriptor might cause an IOException if the file is
                    // already closed. Nothing extra is needed to handle this.
                }
            }
        }

        // If the decoding failed, returns null
        return null;
    }

    /**
     *
     * @param data
     */
    public void setDataAndShow(@Nullable List<ContactBean> data) {
        mData = data;
        mListView.setAdapter(new MyAdapter(mActivity, mData));
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<ContactBean> mAdapterData;
        public MyAdapter(Context context, List<ContactBean> data) {
            mContext = context;
            mAdapterData = data;
        }

        @Override
        public int getCount() {
            return mAdapterData == null ? 0 : mAdapterData.size();
        }

        @Override
        public Object getItem(int position) {
            return (mAdapterData == null || mAdapterData.isEmpty()) ? null : mAdapterData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.contacts_list_item, parent, false);
            }

            ImageView photoImg = ViewHolder.get(convertView, R.id.photo);
            TextView textView = ViewHolder.get(convertView, R.id.text);

            ContactBean bean = mAdapterData.get(position);

            mImageLoader.loadImage(bean.getPhoto(), photoImg);
            textView.setText(bean.getName());
            return convertView;
        }
    }

}
