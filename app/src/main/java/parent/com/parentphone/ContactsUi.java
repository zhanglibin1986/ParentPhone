package parent.com.parentphone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.Utils;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package PACKAGE_NAME
 * @date 15/9/20下午7:24
 * @Description
 */
public class ContactsUi {
    /**
     * 每行的图片数量
     */
    private static final int ROW_COUNT = 2;
    /**
     * 图片之间的间距，单位是dp
     */
    private static final int IMAGE_PADDING = 4;
    private AppCompatActivity mActivity;
    private ContactsUiController mController;
    private View mRootView;//根布局
    private RecyclerView mRecyclerView;
    private List<ContactBean> mData;
    private ContactsListAdapter mAdapter;
    private Toolbar mToolbar;
    private ImageView titleBgPhoto;
//    private CollapsingToolbarLayout collapsingToolbar;

    public ContactsUi(AppCompatActivity activity, ContactsUiController controller) {
        this.mActivity = activity;
        this.mController = controller;
        mActivity.setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    public void findView() {
        mRootView = mActivity.getWindow().getDecorView();
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list);
        mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        titleBgPhoto = (ImageView) mRootView.findViewById(R.id.iv_avatar);
    }



    private void initView() {
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) mRootView.findViewById(
                R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mActivity.getString(R.string.app_name));
//        Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_contact_picture_holo_light);
        Bitmap bitmap = ((BitmapDrawable)titleBgPhoto.getDrawable()).getBitmap();
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int defaultColor = mActivity.getResources().getColor(android.R.color.holo_blue_bright);
                int defaultTitleColor = mActivity.getResources().getColor(android.R.color.white);
                int bgColor = palette.getDarkVibrantColor(defaultColor);
                int titleColor = palette.getLightVibrantColor(defaultTitleColor);

                collapsingToolbar.setContentScrimColor(bgColor);
                collapsingToolbar.setCollapsedTitleTextColor(titleColor);
                collapsingToolbar.setExpandedTitleColor(titleColor);
            }
        });
    }

    private void initAdapter(List<ContactBean> data) {
        mAdapter = new ContactsListAdapter(mActivity, mController, data);
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(mActivity, ROW_COUNT);
        GridLayoutManager.SpanSizeLookup lookUp = new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };

        lm.setSpanSizeLookup(lookUp);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpacesItemDecoration());
    }

    /**
     * @param data
     */
    public void setDataAndShow(@Nullable List<ContactBean> data) {
        mData = data;
        initAdapter(mData);
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        int padding = Utils.dp2px(mActivity, IMAGE_PADDING);

        public SpacesItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int adapterPosition = parent.getChildAdapterPosition(view);
            switch (adapterPosition % 2) {
                case 0:
                    outRect.left = padding;
                    outRect.right = padding / 2;
                    outRect.bottom = padding / 2;
                    outRect.top = padding / 2;
                    break;
                case 1:
                    outRect.left = padding / 2;
                    outRect.right = padding;
                    outRect.bottom = padding / 2;
                    outRect.top = padding / 2;
                    break;
            }
        }
    }
}
