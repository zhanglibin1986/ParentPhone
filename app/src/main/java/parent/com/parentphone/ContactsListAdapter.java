package parent.com.parentphone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.ImageLoader;
import parent.com.parentphone.util.Utils;


/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.parentphone
 * @date 15/10/19上午10:11
 * @Description
 */
public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<ContactBean> mData;
    private Activity mActivity;
    private ImageLoader mImageLoader;// Handles loading the contact image in a background thread
    private int mImageThumbSize;
    private ContactsUiController mController;

    public ContactsListAdapter(Activity activity, ContactsUiController controller, List<ContactBean> data) {
        mActivity = activity;
        mController = controller;
        inflater = LayoutInflater.from(activity);
        this.mData = data;
        mImageThumbSize = (int)1.0 * Utils.getScreenWidth(mActivity) / 2;

        mImageLoader = new ImageLoader(mActivity, getListPreferredItemHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                // This gets called in a background thread and passed the data from
                // ImageLoader.loadImage().
                return Utils.loadContactPhotoThumbnail(mActivity, (String) data, getImageSize());
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(inflater.inflate(R.layout.contacts_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        final ContactBean bean = mData.get(position);
        contactViewHolder.photo.getLayoutParams().height = mImageThumbSize;
        contactViewHolder.photo.getLayoutParams().width = mImageThumbSize;
        mImageLoader.loadImage(bean.getPhoto(), contactViewHolder.photo);
        contactViewHolder.name.setText(bean.getName());
        final View target = contactViewHolder.itemView;
        contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.onContactListItemClicked(bean, target);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
